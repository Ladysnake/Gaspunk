package ladysnake.gaspunk.api.event;

import ladysnake.gaspunk.api.IBreathingHandler;
import ladysnake.gaspunk.api.IGas;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

/**
 * Fired when an entity starts breathing a gas that it wasn't breathing the previous tick <br>
 * This event is fired in addition to {@link GasTickEvent}. It will always be fired before the latter.
 */
@FunctionalInterface
public interface GasEnterCallback {
    Event<GasEnterCallback> EVENT = EventFactory.createArrayBacked(GasEnterCallback.class,
            callbacks -> (breather, breathHandler, gas, concentration) -> {
                for (GasEnterCallback callback : callbacks) {
                    callback.onGasEnterEvent(breather, breathHandler, gas, concentration);
                }
            });

    void onGasEnterEvent(LivingEntity entity, IBreathingHandler breathHandler, IGas gas, float concentration);
}
