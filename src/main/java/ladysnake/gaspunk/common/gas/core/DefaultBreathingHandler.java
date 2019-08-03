package ladysnake.gaspunk.common.gas.core;

import ladysnake.gaspunk.api.IBreathingHandler;
import ladysnake.gaspunk.api.IGas;
import ladysnake.gaspunk.api.event.ExitGasCloudCallback;
import ladysnake.gaspunk.api.event.GasEnterCallback;
import ladysnake.gaspunk.api.event.GasImmunityCallback;
import ladysnake.gaspunk.api.event.GasTickEvent;
import ladysnake.gaspunk.common.config.GasPunkConfig;
import ladysnake.gaspunk.common.item.ItemGasMask;
import ladysnake.gaspunk.mixin.LivingEntityAccessor;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.extension.TypeAwareComponent;
import nerdhub.cardinal.components.api.util.sync.EntitySyncedComponent;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class DefaultBreathingHandler implements IBreathingHandler, TypeAwareComponent {
    /**
     * a value between 0 and 300, emulating the entity air stat with greater precision
     */
    protected float airSupply;
    protected final LivingEntity owner;
    protected final Map<IGas, Float> prevConcentrations = new HashMap<>();
    protected final Map<IGas, Float> concentrations = new HashMap<>();

    public DefaultBreathingHandler(LivingEntity owner) {
        this.owner = owner;
        this.airSupply = getMaxAirSupply();
    }

    @Override
    public ComponentType<IBreathingHandler> getComponentType() {
        return GasPunkComponents.BREATHING;
    }

    /**
     * Sets a gas concentration surrounding an entity for this tick
     *
     * @param gas           the gas suffocating this entity
     * @param concentration a value between 0 and 1 representing the gas' concentration in the entity space
     */
    @Override
    public void setConcentration(IGas gas, float concentration) {
        if (concentrations.getOrDefault(gas, 0f) < concentration)
            concentrations.put(gas, concentration);
    }

    @Override
    public Map<IGas, Float> getGasConcentrations() {
        return concentrations;
    }

    @Override
    public void tick() {
        if (!owner.world.isClient) {
            prevConcentrations.forEach((gas, concentration) -> {
                if (!concentrations.containsKey(gas))
                    if (ExitGasCloudCallback.EVENT.invoker().onExitGasCloudEvent(owner, this, gas, concentration)) {
                        concentrations.put(gas, concentration);
                    } else {
                        gas.onExitCloud(owner, this);
                    }
            });
            boolean appliedAirReduction = false;
            float entityModifier = 1;
            // entities like iron golems get a chance to nullify the effect
            entityModifier = airSupply - ((LivingEntityAccessor) owner).invokeGetNextBreathInWater((int)airSupply);
            for (Map.Entry<IGas, Float> gasEffect : concentrations.entrySet()) {
                IGas gas = gasEffect.getKey();
                float concentration = gasEffect.getValue() * entityModifier;
                if (!isImmune(gas, concentration)) {
                    // first tick of breathing the gas
                    boolean firstTick = !prevConcentrations.containsKey(gasEffect.getKey());
                    if (firstTick) {
                        GasEnterCallback.EVENT.invoker().onGasEnterEvent(owner, this, gas, concentration);
                    }

                    if (GasTickEvent.EVENT.invoker().tickGas(owner, this, gas, concentration)) {
                        continue;
                    }
                    if (gas.isToxic()) {
                        if (airSupply > 0 && !appliedAirReduction) {
                            this.setAirSupply(airSupply - 2);
                        }
                        appliedAirReduction = true;
                    }
                    gas.applyEffect(owner, this, concentration, firstTick);
                }
            }
            prevConcentrations.clear();
            prevConcentrations.putAll(concentrations);
            // regenerate air supply if no toxic gas was inhaled
            if (!appliedAirReduction && airSupply < getMaxAirSupply()) {
                this.setAirSupply(airSupply + 2);
            }
        }
        // invalidate concentration values for next tick
        concentrations.clear();
    }

    @Override
    public boolean isImmune(IGas gas, float concentration) {
        Item helmet = owner.getEquippedStack(EquipmentSlot.HEAD).getItem();
        boolean immune = helmet instanceof ItemGasMask;
        for (String alt : GasPunkConfig.get().getOtherGasMasks()) {
            String[] suit = alt.split("&");
            boolean fullSuit = true;
            switch (suit.length) {
                case 4: fullSuit =  suit[3].equals("*") || suit[3].equals(String.valueOf(Registry.ITEM.getId(owner.getEquippedStack(EquipmentSlot.FEET).getItem())));
                case 3: fullSuit &= suit[2].equals("*") || suit[2].equals(String.valueOf(Registry.ITEM.getId(owner.getEquippedStack(EquipmentSlot.LEGS).getItem())));
                case 2: fullSuit &= suit[1].equals("*") || suit[1].equals(String.valueOf(Registry.ITEM.getId(owner.getEquippedStack(EquipmentSlot.CHEST).getItem())));
                case 1: fullSuit &= suit[0].equals("*") || suit[0].equals(String.valueOf(Registry.ITEM.getId(owner.getEquippedStack(EquipmentSlot.HEAD).getItem())));
            }
            immune |= fullSuit;
        }
        TriState immunityResult = GasImmunityCallback.EVENT.invoker().checkImmunity(owner, this, gas, concentration);
        return immunityResult != TriState.FALSE && immune || immunityResult == TriState.TRUE;
    }

    @Override
    public float getAirSupply() {
        return airSupply;
    }

    @Override
    public void setAirSupply(float airSupply) {
        this.airSupply = Math.min(airSupply, getMaxAirSupply());
    }

    protected float getMaxAirSupply() {
        return (float) owner.getAttributeInstance(GasPunkComponents.MAX_AIR_SUPPLY).getValue();
    }

    @Override
    public CompoundTag toTag(CompoundTag nbt) {
        nbt.putFloat("breath", this.getAirSupply());
        return nbt;
    }

    @Override
    public void fromTag(CompoundTag nbt) {
        this.setAirSupply(nbt.getFloat("breath"));
    }

}
