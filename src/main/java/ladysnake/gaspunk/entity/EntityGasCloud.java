package ladysnake.gaspunk.entity;

import ladysnake.gaspunk.Configuration;
import ladysnake.gaspunk.gas.CapabilityBreathing;
import ladysnake.gaspunk.gas.Gas;
import ladysnake.gaspunk.util.GasUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityGasCloud extends Entity {

    public static final int MAX_PROPAGATION_DISTANCE = 12;
    public static final int MAX_PROPAGATION_DISTANCE_SQ = MAX_PROPAGATION_DISTANCE * MAX_PROPAGATION_DISTANCE;
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityGasCloud.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> CLOUD_AGE = EntityDataManager.createKey(EntityGasCloud.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> MAX_LIFESPAN = EntityDataManager.createKey(EntityGasCloud.class, DataSerializers.VARINT);

    private Gas gas;

    public EntityGasCloud(World worldIn) {
        super(worldIn);
    }

    public EntityGasCloud(World worldIn, Gas gas) {
        this(worldIn);
        this.gas = gas;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            ((WorldServer) world).spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX, posY, posZ, 5, 0, 0, 0, 0.3);
        }
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(new BlockPos(this)).grow(10));
        for (EntityLivingBase entity : entities) {
            float distance = Configuration.fastGas
                    ? this.getDistance(entity)
                    : GasUtil.getPropagationDistance(world, new BlockPos(this), new BlockPos(entity));
            if (distance >= 0) {
                float concentration = (getCloudAge() / getMaxLifeSpan()) * (distance / MAX_PROPAGATION_DISTANCE);
                CapabilityBreathing.getHandler(entity).ifPresent(h -> h.setConcentration(gas, concentration));
            }
        }
    }

    public int getCloudAge() {
        return getDataManager().get(CLOUD_AGE);
    }

    public int getMaxLifeSpan() {
        return getDataManager().get(MAX_LIFESPAN);
    }

    @Override
    protected void entityInit() {
        getDataManager().register(COLOR, 0xFFFFFFFF);
        getDataManager().register(CLOUD_AGE, 0);
        getDataManager().register(MAX_LIFESPAN, 100);
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {

    }

}
