package ladysnake.gaspunk.common.entity;

import io.netty.buffer.ByteBuf;
import ladysnake.gaspunk.client.GasPunkClient;
import ladysnake.gaspunk.api.IGas;
import ladysnake.gaspunk.common.config.GasPunkConfig;
import ladysnake.gaspunk.common.gas.core.GasPunkComponents;
import ladysnake.gaspunk.common.gas.Gases;
import ladysnake.gaspunk.common.util.GasUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GasCloudEntity extends Entity implements IEntityAdditionalSpawnData {

    public static final int MAX_PROPAGATION_DISTANCE = 10;
    public static final int MAX_PROPAGATION_DISTANCE_SQ = MAX_PROPAGATION_DISTANCE * MAX_PROPAGATION_DISTANCE;
    private static final TrackedData<Integer> CLOUD_AGE = DataTracker.registerData(GasCloudEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> MAX_LIFESPAN = DataTracker.registerData(GasCloudEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected IGas gas = Gases.AIR;
    @Nullable
    protected UUID emitterID;

    public GasCloudEntity(EntityType<? extends GasCloudEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public GasCloudEntity(EntityType<? extends GasCloudEntity> type, World worldIn, IGas gas) {
        this(type, worldIn);
        this.gas = gas;
    }

    public void setEmitter(@Nullable Entity emitter) {
        this.emitterID = emitter == null ? null : emitter.getUuid();
    }

    @Override
    public void tick() {
        super.tick();

        float ageRatio = 1 - getCloudAge() / (float) getMaxLifeSpan();
        int particleAmount = gas.getParticleType().getParticleAmount();
        int color = gas.getColor();
        if (this.world.isClient && this.getCloudAge() % 10 == 0) {
            GasPunkClient.INSTANCE.makeSmoke(world, x, y, z, color, particleAmount, MAX_PROPAGATION_DISTANCE-2, 2, gas.getParticleType());
        }

        if (!world.isClient) {
            // sync the cloud's position with the emitter's
            if (emitterID != null) {
                Entity emitter = ((ServerWorld)world).getEntity(emitterID);
                if (emitter != null) {
                    this.setPosition(emitter.x, emitter.y, emitter.z);
                }
            }
            int age = getCloudAge();
            this.setCloudAge(age + 1);
            if (age > getMaxLifeSpan()) {
                this.remove();
                return;
            }
        }
        List<? extends LivingEntity> entities;
        Box box = new Box(new BlockPos(this)).expand(10);
        if (world.isClient) {
            // just update the player
            entities = world.getPlayers();
        } else {
            entities = world.getEntities(LivingEntity.class, box);
        }
        for (LivingEntity entity : entities) {
            float distance = GasPunkConfig.get().useFastGas()
                    ? this.distanceTo(entity)
                    : GasUtil.getPropagationDistance(world, new BlockPos(this), new BlockPos(entity), MAX_PROPAGATION_DISTANCE);
            if (distance >= 0) {
                float concentration = ageRatio * (1 - distance / (float) MAX_PROPAGATION_DISTANCE);
                GasPunkComponents.BREATHING.get(entity).setConcentration(gas, concentration);
            }
        }
    }

    public int getCloudAge() {
        return getDataTracker().get(CLOUD_AGE);
    }

    public void setCloudAge(int cloudAge) {
        getDataTracker().set(CLOUD_AGE, cloudAge);
    }

    public int getMaxLifeSpan() {
        return getDataTracker().get(MAX_LIFESPAN);
    }

    public void setMaxLifespan(int lifespan) {
        getDataTracker().set(MAX_LIFESPAN, lifespan);
    }

    @Override
    protected void initDataTracker() {
        getDataTracker().startTracking(CLOUD_AGE, 0);
        getDataTracker().startTracking(MAX_LIFESPAN, 100);
    }

    @Override
    protected void readCustomDataFromTag(@Nonnull CompoundTag compound) {
        this.gas = Gases.GAS_REGISTRY.get(new Identifier(compound.getString("gas")));
        this.setMaxLifespan(compound.getInt("max_lifespan"));
        this.setCloudAge(compound.getInt("cloud_age"));
        if (compound.containsKey("emitter_idLeast")) {
            emitterID = compound.getUuid("emitter_id");
        }
    }

    @Override
    protected void writeCustomDataToTag(@Nonnull CompoundTag compound) {
        if (gas != null) {
            compound.putString("gas", Objects.requireNonNull(gas.getRegistryName()).toString());
        }
        compound.putInt("max_lifespan", getMaxLifeSpan());
        compound.putInt("cloud_age", getCloudAge());
        if (emitterID != null)
            compound.putUuid("emitter_id", emitterID);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        PacketByteBuf buf = new PacketByteBuf(buffer);
        buf.writeString(gas != null ? Objects.requireNonNull(gas.getRegistryName()).toString() : "");
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        PacketByteBuf buf = new PacketByteBuf(additionalData);
        this.gas = Gases.GAS_REGISTRY.getValue(new Identifier(buf.readString(64)));
    }
}
