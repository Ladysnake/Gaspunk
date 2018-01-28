package ladysnake.gaspunk.gas;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.item.ItemGasMask;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
public class CapabilityBreathing {

    private static final MethodHandle entityLivingBase$decreaseAirSupply;

    static {
        MethodHandle temp = null;
        try {
            Method method = ReflectionHelper.findMethod(EntityLivingBase.class, "decreaseAirSupply", "func_70682_h(I)I", int.class);
            temp = MethodHandles.lookup().unreflect(method);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        entityLivingBase$decreaseAirSupply = temp;
    }

    @CapabilityInject(IBreathingHandler.class)
    public static Capability<IBreathingHandler> CAPABILITY_BREATHING;

    public static void register() {
        CapabilityManager.INSTANCE.register(IBreathingHandler.class, new Storage(), DefaultBreathingHandler::new);
    }

    public static Optional<IBreathingHandler> getHandler(Entity entity) {
        if (entity instanceof EntityPlayer)
            return Optional.ofNullable(entity.getCapability(CAPABILITY_BREATHING, null));
        return Optional.empty();
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityLivingBase)
            event.addCapability(new ResourceLocation(GasPunk.MOD_ID, "breathing_cap"), new Provider(event.getObject()));
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        getHandler(event.getEntity()).ifPresent(IBreathingHandler::tick);
    }

    public static class DefaultBreathingHandler implements IBreathingHandler {
        /**
         * a value between 0 and 300, emulating the entity air stat with greater precision
         */
        private float airSupply = 300;
        private int ticksSinceToxicInhalation = 0;
        private EntityLivingBase owner;
        private Map<Gas, Float> concentrations = new HashMap<>();

        DefaultBreathingHandler() {
            this(null);
        }

        public DefaultBreathingHandler(EntityLivingBase owner) {
            this.owner = owner;
        }

        /**
         * Sets a gas concentration surrounding an entity for this tick
         *
         * @param gas           the gas suffocating this entity
         * @param concentration a value between 0 and 1 representing the gas' concentration in the entity space
         */
        @Override
        public void setConcentration(Gas gas, float concentration) {
            if (concentrations.getOrDefault(gas, 0f) < concentration)
                concentrations.put(gas, concentration);
        }

        @Override
        public Map<Gas, Float> getGasConcentrations() {
            return concentrations;
        }

        @Override
        public void tick() {
            if (!owner.world.isRemote) {
                boolean appliedAirReduction = false;
                if (!isImmune()) {
                    float entityModifier = 1;
                    if (entityLivingBase$decreaseAirSupply != null) {
                        try {
                            // entities like iron golems get a chance to nullify the effect
                            entityModifier = airSupply - (int) entityLivingBase$decreaseAirSupply.invoke(owner, (int) airSupply);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                    for (Map.Entry<Gas, Float> gasEffect : concentrations.entrySet()) {
                        Gas gas = gasEffect.getKey();
                        float concentration = gasEffect.getValue() * entityModifier;
                        if (gas.isToxic() && airSupply > 0) {
                            if (!appliedAirReduction) {
                                float finalEntityModifier = entityModifier;
                                // take all gases into account for the breathing penalty
                                float totalGasConcentration = (float) Math.max(1, concentrations.entrySet().stream()
                                        .filter(e -> e.getKey().isToxic())
                                        .mapToDouble(Map.Entry::getValue)
                                        .map(c -> c * finalEntityModifier).sum());
                                // air supply decreases 4x as fast as underwater under worse conditions
                                this.setAirSupply(airSupply - 4 * totalGasConcentration);
                                appliedAirReduction = true;
                            }
                        }
                        gas.applyEffect(owner, this, concentration);
                    }
                }
                // regenerate air supply if no toxic gas was inhaled
                if (!appliedAirReduction && airSupply < 300) {
                    this.setAirSupply(airSupply + ticksSinceToxicInhalation++);
                }
            }
            // invalidate concentration values for next tick
            concentrations.clear();
        }

        public boolean isImmune() {
            return owner.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemGasMask;
        }

        @Override
        public float getAirSupply() {
            return airSupply;
        }

        @Override
        public void setAirSupply(float airSupply) {
            this.airSupply = airSupply;
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
        IBreathingHandler instance;

        Provider(Entity object) {
            this.instance = object instanceof EntityPlayerMP
                    ? new PlayerBreathingHandler((EntityPlayerMP) object)
                    : new DefaultBreathingHandler((EntityLivingBase) object);
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CAPABILITY_BREATHING;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == CAPABILITY_BREATHING ? CAPABILITY_BREATHING.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) CAPABILITY_BREATHING.getStorage().writeNBT(CAPABILITY_BREATHING, instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            CAPABILITY_BREATHING.getStorage().readNBT(CAPABILITY_BREATHING, instance, null, nbt);
        }
    }

    public static class Storage implements Capability.IStorage<IBreathingHandler> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IBreathingHandler> capability, IBreathingHandler instance, EnumFacing side) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setFloat("breath", instance.getAirSupply());
            return nbt;
        }

        @Override
        public void readNBT(Capability<IBreathingHandler> capability, IBreathingHandler instance, EnumFacing side, NBTBase nbt) {
            instance.setAirSupply(((NBTTagCompound) nbt).getFloat("breath"));
        }
    }
}
