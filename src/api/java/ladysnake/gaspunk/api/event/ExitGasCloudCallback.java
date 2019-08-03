package ladysnake.gaspunk.api.event;

import ladysnake.gaspunk.api.IBreathingHandler;
import ladysnake.gaspunk.api.IGas;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

@FunctionalInterface
public interface ExitGasCloudCallback {

    Event<ExitGasCloudCallback> EVENT = EventFactory.createArrayBacked(ExitGasCloudCallback.class,
            callbacks -> (breather, breathHandler, gas, concentration) -> {
                for (ExitGasCloudCallback callback : callbacks) {
                    if (callback.onExitGasCloudEvent(breather, breathHandler, gas, concentration)) {
                        return true;
                    }
                }
                return false;
            });
    /**
     * Fired when an entity is no longer affected by a gas.
     *
     * @param breather the entity affected by the gas
     * @param breathHandler the entity's breathing component
     * @param gas the gas affecting the entity
     * @param concentration the concentration of the gas in the last tick it got breathed
     * @return {@code true} if the gas effect should persist for one more tick, {@code false} otherwise
     */
    boolean onExitGasCloudEvent(LivingEntity breather, IBreathingHandler breathHandler, IGas gas, float concentration);
}
