package ladysnake.gaspunk.entity;

import io.netty.buffer.ByteBuf;
import ladysnake.gaspunk.Configuration;
import ladysnake.gaspunk.gas.CapabilityBreathing;
import ladysnake.gaspunk.gas.Gas;
import ladysnake.gaspunk.init.ModGases;
import ladysnake.gaspunk.util.GasUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class EntityGasCloud extends Entity implements IEntityAdditionalSpawnData {

    public static final int MAX_PROPAGATION_DISTANCE = 12;
    public static final int MAX_PROPAGATION_DISTANCE_SQ = MAX_PROPAGATION_DISTANCE * MAX_PROPAGATION_DISTANCE;
    private static final DataParameter<Integer> CLOUD_AGE = EntityDataManager.createKey(EntityGasCloud.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> MAX_LIFESPAN = EntityDataManager.createKey(EntityGasCloud.class, DataSerializers.VARINT);

    private Gas gas = ModGases.AIR;

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
            int age = getCloudAge();
            this.setCloudAge(age + 1);
            if (age > getMaxLifeSpan()) {
                setDead();
                return;
            }
        }
        List<EntityLivingBase> entities;
        AxisAlignedBB box = new AxisAlignedBB(new BlockPos(this)).grow(10);
        if (world.isRemote) // just update the player
            entities = world.getPlayers(EntityPlayer.class, p -> p != null && getDistanceSq(p) < MAX_PROPAGATION_DISTANCE_SQ);
        else
            entities = world.getEntitiesWithinAABB(EntityLivingBase.class, box);
        for (EntityLivingBase entity : entities) {
            float distance = Configuration.fastGas
                    ? this.getDistance(entity)
                    : GasUtil.getPropagationDistance(world, new BlockPos(this), new BlockPos(entity));
            if (distance >= 0) {
                float concentration = (1 - getCloudAge() / (float) getMaxLifeSpan()) * (1 - distance / (float) MAX_PROPAGATION_DISTANCE);
                CapabilityBreathing.getHandler(entity).ifPresent(h -> h.setConcentration(gas, concentration));
            }
        }
    }

    public int getCloudAge() {
        return getDataManager().get(CLOUD_AGE);
    }

    public void setCloudAge(int cloudAge) {
        getDataManager().set(CLOUD_AGE, cloudAge);
    }

    public int getMaxLifeSpan() {
        return getDataManager().get(MAX_LIFESPAN);
    }

    public void setMaxLifespan(int lifespan) {
        getDataManager().set(MAX_LIFESPAN, lifespan);
    }

    @Override
    protected void entityInit() {
        getDataManager().register(CLOUD_AGE, 0);
        getDataManager().register(MAX_LIFESPAN, 100);
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        this.gas = ModGases.REGISTRY.getValue(new ResourceLocation(compound.getString("gas")));
        this.setMaxLifespan(compound.getInteger("max_lifespan"));
        this.setCloudAge(compound.getInteger("cloud_age"));
    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        if (gas != null)
            compound.setString("gas", Objects.requireNonNull(gas.getRegistryName()).toString());
        compound.setInteger("max_lifespan", getMaxLifeSpan());
        compound.setInteger("cloud_age", getCloudAge());
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        PacketBuffer buf = new PacketBuffer(buffer);
        buf.writeString(gas != null ? Objects.requireNonNull(gas.getRegistryName()).toString() : "");
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        PacketBuffer buf = new PacketBuffer(additionalData);
        this.gas = ModGases.REGISTRY.getValue(new ResourceLocation(buf.readString(64)));
    }
}
