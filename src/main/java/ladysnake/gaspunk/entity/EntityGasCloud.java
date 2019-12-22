package ladysnake.gaspunk.entity;

import io.netty.buffer.ByteBuf;
import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.GasPunkConfig;
import ladysnake.gaspunk.api.IGas;
import ladysnake.gaspunk.gas.core.CapabilityBreathing;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EntityGasCloud extends Entity implements IEntityAdditionalSpawnData {

    static int maxPropDistance = GasPunkConfig.maxPropagationDistance;
    public static final int MAX_PROPAGATION_DISTANCE = maxPropDistance;
    public static final int MAX_PROPAGATION_DISTANCE_SQ = MAX_PROPAGATION_DISTANCE * MAX_PROPAGATION_DISTANCE;
    private static final DataParameter<Integer> CLOUD_AGE = EntityDataManager.createKey(EntityGasCloud.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> MAX_LIFESPAN = EntityDataManager.createKey(EntityGasCloud.class, DataSerializers.VARINT);

    protected IGas gas = ModGases.AIR;
    @Nullable
    protected UUID emitterID;

    public EntityGasCloud(World worldIn) {
        super(worldIn);
    }

    public EntityGasCloud(World worldIn, IGas gas) {
        this(worldIn);
        this.gas = gas;
    }

    public void setEmitter(@Nullable Entity emitter) {
        this.emitterID = emitter == null ? null : emitter.getUniqueID();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        float ageRatio = 1 - getCloudAge() / (float) getMaxLifeSpan();
        int particleAmount = gas.getParticleType().getParticleAmount();
        int color = gas.getColor();
        if (this.getCloudAge() % 10 == 0)
            GasPunk.proxy.makeSmoke(world, posX, posY, posZ, color, particleAmount, MAX_PROPAGATION_DISTANCE-2, 2, gas.getParticleType());

        if (!world.isRemote) {
            // sync the cloud's position with the emitter's
            if (emitterID != null) {
                Entity emitter = ((WorldServer)world).getEntityFromUuid(emitterID);
                if (emitter != null) {
                    this.setPosition(emitter.posX, emitter.posY, emitter.posZ);
                }
            }
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
            float distance = GasPunkConfig.fastGas
                    ? this.getDistance(entity)
                    : GasUtil.getPropagationDistance(world, new BlockPos(this), new BlockPos(entity), MAX_PROPAGATION_DISTANCE);
            if (distance >= 0) {
                float concentration = ageRatio * (1 - distance / (float) MAX_PROPAGATION_DISTANCE);
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
        if (compound.hasKey("emitter_idLeast")) {
            emitterID = compound.getUniqueId("emitter_id");
        }
    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        if (gas != null)
            compound.setString("gas", Objects.requireNonNull(gas.getRegistryName()).toString());
        compound.setInteger("max_lifespan", getMaxLifeSpan());
        compound.setInteger("cloud_age", getCloudAge());
        if (emitterID != null)
            compound.setUniqueId("emitter_id", emitterID);
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
