package ladysnake.gaspunk.common.entity;

import ladysnake.gaspunk.common.GasPunk;
import ladysnake.gaspunk.common.item.GasPunkItems;
import ladysnake.gaspunk.common.item.ItemGrenade;
import ladysnake.gaspunk.common.item.SkinItem;
import ladysnake.gaspunk.mixin.ThrownEntityAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.api.EnvironmentInterfaces;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

@EnvironmentInterfaces({@EnvironmentInterface(
        value = EnvType.CLIENT,
        itf = FlyingItemEntity.class
)})
public class GrenadeEntity extends ThrownEntity implements CustomizableCollider, FlyingItemEntity {
    private static final TrackedData<ItemStack> ITEM = DataTracker.registerData(GrenadeEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    private static final TrackedData<Integer> COUNTDOWN = DataTracker.registerData(GrenadeEntity.class, TrackedDataHandlerRegistry.INTEGER);
    /**The lifespan of the cloud emitted by this grenade. Avoids tracking the cloud entity just to know when to pop off as an item.*/
    protected int cloudMaxLifeSpan;
    protected boolean canPickup;

    public GrenadeEntity(EntityType<? extends GrenadeEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public GrenadeEntity(EntityType<? extends GrenadeEntity> type, World worldIn, ItemStack stack, double x, double y, double z) {
        super(type, x, y, z, worldIn);
        setStack(stack);
        canPickup = true;
    }

    public GrenadeEntity(EntityType<? extends GrenadeEntity> type, World worldIn, @Nonnull LivingEntity throwerIn, ItemStack stack) {
        super(type, throwerIn, worldIn);
        setStack(stack);
        canPickup = !(throwerIn instanceof PlayerEntity && ((PlayerEntity)throwerIn).isCreative());
    }

    @Override
    protected float getGravity() {
        return 0.05f;
    }

    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(ITEM, new ItemStack(GasPunkItems.GRENADE));
        this.getDataTracker().startTracking(COUNTDOWN, 60);
    }

    public void setCountdown(int countdown) {
        getDataTracker().set(COUNTDOWN, countdown);
    }

    private int getCountdown() {
        return getDataTracker().get(COUNTDOWN);
    }

    @Override
    public void tick() {
        super.tick();
        if (world.isClient) return;
        int countdown = this.getCountdown();
        if (countdown == 1) {
            this.explode();
        }
        this.setCountdown(this.getCountdown() - 1);
        // after exploding, the countdown is used to track the time before cloud expiration
        if (countdown <= -cloudMaxLifeSpan) {
            if (canPickup) {
                ItemStack stack = this.getStack();
                ItemGrenade grenadeItem = (ItemGrenade) stack.getItem();
                ItemStack emptyGrenade = new ItemStack(GasPunkItems.EMPTY_GRENADE);
                ((SkinItem) GasPunkItems.EMPTY_GRENADE).setSkin(emptyGrenade, grenadeItem.getSkin(stack));
                world.spawnEntity(new ItemEntity(world, x, y, z, emptyGrenade));
            }
            this.remove();
        }
    }

    @Override
    public RayTraceContext.ShapeType getRayTracingShapeType() {
        return RayTraceContext.ShapeType.COLLIDER;
    }

    @Nonnull
    public ItemStack getStack() {
        ItemStack itemstack = this.getDataTracker().get(ITEM);

        if (!(itemstack.getItem() instanceof ItemGrenade)) {
            if (this.world != null) {
                GasPunk.LOGGER.error("{} {} has no item?!", this.getClass().getSimpleName(), this.getEntityId());
            }

            return new ItemStack(GasPunkItems.GRENADE);
        } else {
            return itemstack;
        }
    }

    public void setStack(ItemStack item) {
        item.getOrCreateTag().putBoolean("isEntityGrenade", true);
        getDataTracker().set(ITEM, item);
    }

    @Override
    protected void onCollision(@Nonnull HitResult result) {
        if (result.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = ((BlockHitResult) result).getBlockPos();
            if (!world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty() && ((ThrownEntityAccessor) this).getGhostTicks() <= 0) {
                Vec3i hitVector = ((BlockHitResult) result).getSide().getVector();
                Vec3d currentVelocity = this.getVelocity();
                double motionX = currentVelocity.x * hitVector.getX() * -0.4 + 0.2;
                if (Math.abs(motionX) < 0.2) motionX = 0;
                double motionY = currentVelocity.y * hitVector.getY() * -0.4 + 0.2;
                if (Math.abs(motionY) < 0.3) motionY = 0;
                double motionZ = currentVelocity.z * hitVector.getZ() * -0.4 + 0.2;
                if (Math.abs(motionZ) < 0.2) motionZ = 0;
                this.setVelocity(motionX, motionY, motionZ);
                ((ThrownEntityAccessor) this).setGhostTicks(2);
                velocityDirty = true;
            } else {
                ((ThrownEntityAccessor) this).setGhostTicks(((ThrownEntityAccessor) this).getGhostTicks() - 1);
            }
        } else if (result.getType() == HitResult.Type.ENTITY) {
            this.setVelocity(this.getVelocity().multiply(-0.05));
            this.yaw += 180.0F;
            this.prevYaw += 180.0F;
        }
    }

    protected void explode() {
        if (this.world instanceof ServerWorld) {
            GasCloudEntity cloud = ((ItemGrenade) this.getStack().getItem()).explode((ServerWorld) world, this.getPos(), getStack());
            cloud.setEmitter(this);
            cloudMaxLifeSpan = cloud.getMaxLifeSpan();
        }
    }

    @Override
    public void writeCustomDataToTag(@Nonnull CompoundTag compound) {
        super.writeCustomDataToTag(compound);
        compound.putInt("countdown", getCountdown());
        compound.putInt("cloudMaxLifeSpan", cloudMaxLifeSpan);
        compound.put("grenade", this.getStack().toTag(new CompoundTag()));
        compound.putBoolean("canPickup", canPickup);
    }

    @Override
    public void readCustomDataFromTag(@Nonnull CompoundTag compound) {
        super.readCustomDataFromTag(compound);
        setCountdown(compound.getInt("countdown"));
        cloudMaxLifeSpan = compound.getInt("cloudMaxLifeSpan");
        this.setStack(ItemStack.fromTag(compound.getCompound("grenade")));
        canPickup = compound.getBoolean("canPickup");
    }
}
