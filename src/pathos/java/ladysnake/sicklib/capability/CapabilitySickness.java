package ladysnake.sicklib.capability;

import ladysnake.sicklib.Pathos;
import ladysnake.sicklib.sickness.ISickness;
import ladysnake.sicklib.sickness.SicknessEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;

@Mod.EventBusSubscriber(modid = Pathos.MOD_ID)
public class CapabilitySickness {

    @CapabilityInject(ISicknessHandler.class)
    public static Capability<ISicknessHandler> CAPABILITY_SICKNESS;

    public static void register() {
        // Because apparently EventBusSubscriber is broken in this source set
        MinecraftForge.EVENT_BUS.register(CapabilitySickness.class);
        CapabilityManager.INSTANCE.register(ISicknessHandler.class, new Storage(), DefaultSicknessHandler::new);
    }

    public static Optional<ISicknessHandler> getHandler(Entity entity) {
        if (entity instanceof EntityLivingBase)
            return Optional.ofNullable(entity.getCapability(CAPABILITY_SICKNESS, null));
        return Optional.empty();
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityLivingBase)
            event.addCapability(new ResourceLocation(Pathos.MOD_ID, "sickness_cap"), new Provider((EntityLivingBase) event.getObject()));
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        getHandler(event.getEntity()).ifPresent(ISicknessHandler::tick);
    }

    public static class DefaultSicknessHandler implements ISicknessHandler {
        private EntityLivingBase carrier;
        private Map<ISickness, SicknessEffect> sicknesses = new HashMap<>();

        DefaultSicknessHandler() {
            super();
        }

        public DefaultSicknessHandler(EntityLivingBase carrier) {
            this.carrier = carrier;
        }

        @Override
        public boolean isSicknessActive(ISickness sickness) {
            return sicknesses.containsKey(sickness);
        }

        @Override
        public void addSickness(SicknessEffect effect, BiFunction<SicknessEffect, SicknessEffect, SicknessEffect> mergeFunction) {
            sicknesses.merge(effect.getSickness(), effect, mergeFunction);
        }

        @Override
        public void tick() {
            for (Iterator<Map.Entry<ISickness, SicknessEffect>> iterator = sicknesses.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<ISickness, SicknessEffect> entry = iterator.next();
                SicknessEffect sickness = entry.getValue();
                sickness.performEffect(carrier);
                if (sickness.getSeverity() == 0)    // consider that effect cured
                    iterator.remove();
            }
        }

        @Override
        public Collection<SicknessEffect> getActiveSicknesses() {
            return sicknesses.values();
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTTagList> {
        final ISicknessHandler instance;

        Provider(EntityLivingBase object) {
                this.instance = new DefaultSicknessHandler(object);
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CAPABILITY_SICKNESS;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == CAPABILITY_SICKNESS ? CAPABILITY_SICKNESS.cast(instance) : null;
        }

        @Override
        public NBTTagList serializeNBT() {
            return (NBTTagList) CAPABILITY_SICKNESS.getStorage().writeNBT(CAPABILITY_SICKNESS, instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagList nbt) {
            CAPABILITY_SICKNESS.getStorage().readNBT(CAPABILITY_SICKNESS, instance, null, nbt);
        }
    }

    public static class Storage implements Capability.IStorage<ISicknessHandler> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<ISicknessHandler> capability, ISicknessHandler instance, EnumFacing side) {
            NBTTagList nbtList = new NBTTagList();
            for (SicknessEffect effect : instance.getActiveSicknesses()) {
                nbtList.appendTag(effect.serializeNBT());
            }
            return nbtList;
        }

        @Override
        public void readNBT(Capability<ISicknessHandler> capability, ISicknessHandler instance, EnumFacing side, NBTBase nbt) {
            if (nbt instanceof NBTTagList) {
                for (NBTBase effect : ((NBTTagList)nbt)) {
                    if (effect instanceof NBTTagCompound)
                        instance.addSickness(new SicknessEffect((NBTTagCompound) effect));
                }
            }
        }
    }
}
