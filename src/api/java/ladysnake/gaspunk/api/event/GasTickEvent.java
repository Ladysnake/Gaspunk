package ladysnake.gaspunk.api.event;

import ladysnake.gaspunk.api.IBreathingHandler;
import ladysnake.gaspunk.api.IGas;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

@FunctionalInterface
public interface GasTickEvent {
    Event<GasTickEvent> EVENT = EventFactory.createArrayBacked(GasTickEvent.class,
            callbacks -> (breather, breathHandler, gas, concentration) -> {
                for (GasTickEvent callback : callbacks) {
                    if (callback.tickGas(breather, breathHandler, gas, concentration)) {
                        return true;
                    }
                }
                return false;
            });

    /**
     * Fired when a gas is ticked on an entity.
     *
     * <p> The tick can be canceled by returning {@code true}. This prevents the gas effect
     * from being applied this tick.
     *
     * @param breather the entity affected by the gas
     * @param breathHandler the entity's breathing component
     * @param gas the gas affecting the entity
     * @param concentration the concentration of the gas in the last tick it got breathed
     * @return {@code true} if the gas tick should be skipped, {@code false} otherwise
     */
    boolean tickGas(LivingEntity entity, IBreathingHandler breathHandler, IGas gas, float concentration);
}
