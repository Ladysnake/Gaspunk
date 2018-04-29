package ladysnake.gaspunk.entity;

import ladylib.misc.ItemUtil;
import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.init.ModItems;
import ladysnake.gaspunk.item.ItemGrenade;
import ladysnake.gaspunk.item.SkinItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;

public class EntityGrenade extends EntityNonRetardedThrowable {
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityGrenade.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<Integer> COUNTDOWN = EntityDataManager.createKey(EntityGrenade.class, DataSerializers.VARINT);
    /**The lifespan of the cloud emitted by this grenade. Avoids tracking the cloud entity just to know when to pop off as an item.*/
    protected int cloudMaxLifeSpan;
    protected boolean canPickup;

    public EntityGrenade(World worldIn) {
        super(worldIn);
    }

    public EntityGrenade(World worldIn, ItemStack stack, double x, double y, double z) {
        super(worldIn, x, y, z);
        setItem(stack);
        canPickup = true;
    }

    public EntityGrenade(World worldIn, @Nonnull EntityLivingBase throwerIn, ItemStack stack) {
        super(worldIn, throwerIn);
        setItem(stack);
        canPickup = !(throwerIn instanceof EntityPlayer && ((EntityPlayer)throwerIn).isCreative());
    }

    @Override
    protected float getGravityVelocity() {
        return 0.05f;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        getDataManager().register(ITEM, new ItemStack(ModItems.GRENADE));
        getDataManager().register(COUNTDOWN, 60);
    }

    public void setCountdown(int countdown) {
        getDataManager().set(COUNTDOWN, countdown);
    }

    private int getCountdown() {
        return getDataManager().get(COUNTDOWN);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (world.isRemote) return;
        int countdown = getCountdown();
        if (countdown == 1)
            explode();
        setCountdown(getCountdown() - 1);
        // after exploding, the countdown is used to track the time before cloud expiration
        if (countdown <= -cloudMaxLifeSpan) {
            if (canPickup) {
                ItemStack stack = getGrenade();
                ItemGrenade grenadeItem = (ItemGrenade) stack.getItem();
                ItemStack emptyGrenade = new ItemStack(ModItems.EMPTY_GRENADE);
                ((SkinItem)ModItems.EMPTY_GRENADE).setSkin(emptyGrenade, grenadeItem.getSkin(stack));
                world.spawnEntity(new EntityItem(world, posX, posY, posZ, emptyGrenade));
            }
            setDead();
        }
    }

    @Nonnull
    public ItemStack getGrenade() {
        ItemStack itemstack = this.getDataManager().get(ITEM);

        if (!(itemstack.getItem() instanceof ItemGrenade)) {
            if (this.world != null) {
                GasPunk.LOGGER.error("{} {} has no item?!", this.getClass().getSimpleName(), this.getEntityId());
            }

            return new ItemStack(ModItems.GRENADE);
        } else {
            return itemstack;
        }
    }

    public void setItem(ItemStack item) {
        ItemUtil.getOrCreateCompound(item).setBoolean("isEntityGrenade", true);
        getDataManager().set(ITEM, item);
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult result) {
        if (result.typeOfHit == RayTraceResult.Type.BLOCK && world.getBlockState(result.getBlockPos()).isFullBlock() && this.ignoreTime-- <= 0) {
            Vec3i hitVector = result.sideHit.getDirectionVec();
            motionX *= hitVector.getX() * -0.4 + 0.2;
            if (Math.abs(motionX) < 0.2) motionX = 0;
            motionY *= hitVector.getY() * -0.4 + 0.2;
            if (Math.abs(motionY) < 0.3) motionY = 0;
            motionZ *= hitVector.getZ() * -0.4 + 0.2;
            if (Math.abs(motionZ) < 0.2) motionZ = 0;
            this.ignoreTime = 2;
            isAirBorne = true;
        } else if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
            this.motionX *= -0.05;
            this.motionY *= -0.05;
            this.motionZ *= -0.05;
            this.rotationYaw += 180.0F;
            this.prevRotationYaw += 180.0F;
        }
    }

    protected void explode() {
        if (this.world instanceof WorldServer) {
            EntityGasCloud cloud = ((ItemGrenade) this.getGrenade().getItem()).explode((WorldServer) world, this.getPositionVector(), getGrenade());
            cloud.setEmitter(this);
            cloudMaxLifeSpan = cloud.getMaxLifeSpan();
        }
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("countdown", getCountdown());
        compound.setInteger("cloudMaxLifeSpan", cloudMaxLifeSpan);
        compound.setTag("grenade", this.getGrenade().writeToNBT(new NBTTagCompound()));
        compound.setBoolean("canPickup", canPickup);
    }

    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setCountdown(compound.getInteger("countdown"));
        cloudMaxLifeSpan = compound.getInteger("cloudMaxLifeSpan");
        this.setItem(new ItemStack(compound.getCompoundTag("grenade")));
        canPickup = compound.getBoolean("canPickup");
    }
}
