package ladysnake.gaspunk.entity;

import ladysnake.gaspunk.init.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityGrenade extends EntityGasTube {
    private static final DataParameter<Integer> COUNTDOWN = EntityDataManager.createKey(EntityGrenade.class, DataSerializers.VARINT);

    public EntityGrenade(World worldIn) {
        super(worldIn);
    }

    public EntityGrenade(World worldIn, EntityLivingBase throwerIn, ItemStack stack) {
        super(worldIn, throwerIn, stack);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.05f;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
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
        int countdown = getCountdown();
        if (countdown < 1)
            explode();
        else
            setCountdown(getCountdown() - 1);
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult result) {
        if (result.typeOfHit == RayTraceResult.Type.BLOCK && world.getBlockState(result.getBlockPos()).isFullBlock()) {
            Vec3i hitVector = result.sideHit.getDirectionVec();
            motionX *= hitVector.getX() * -0.4 + 0.2;
            if (Math.abs(motionX) < 0.2) motionX = 0;
            motionY *= hitVector.getY() * -0.4 + 0.2;
            if (Math.abs(motionY) < 0.2) motionY = 0;
            motionZ *= hitVector.getZ() * -0.4 + 0.2;
            if (Math.abs(motionZ) < 0.2) motionZ = 0;
            isAirBorne = true;
        } else if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
            this.motionX *= -0.05;
            this.motionY *= -0.05;
            this.motionZ *= -0.05;
            this.rotationYaw += 180.0F;
            this.prevRotationYaw += 180.0F;
        }
    }

    @Override
    protected void explode() {
        super.explode();
        if (!world.isRemote)
            world.spawnEntity(new EntityItem(world, posX, posY, posZ, new ItemStack(ModItems.GRENADE)));
    }
}
