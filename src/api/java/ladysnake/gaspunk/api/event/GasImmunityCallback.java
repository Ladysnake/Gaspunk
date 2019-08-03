package ladysnake.gaspunk.api.event;

import ladysnake.gaspunk.api.IBreathingHandler;
import ladysnake.gaspunk.api.IGas;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.LivingEntity;

/**
 * GasImmunityCallback is fired whenever an entity checks whether they're affected by surrounding gases
 * in {@link IBreathingHandler#isImmune(IGas, float)}. <br>
 */
@FunctionalInterface
public interface GasImmunityCallback {
    /**
     * This event works through a vote system. If more callbacks return {@link TriState#TRUE}, the event
     * returns {@code TRUE}, if more callbacks return {@link TriState#FALSE}, it returns {@code FALSE},
     * otherwise it returns {@link TriState#DEFAULT}.
     */
    Event<GasImmunityCallback> EVENT = EventFactory.createArrayBacked(GasImmunityCallback.class,
            callbacks -> (breather, breathHandler, gas, concentration) -> {
                int favour = 0;
                int against = 0;
                for (GasImmunityCallback callback : callbacks) {
                    TriState result = callback.checkImmunity(breather, breathHandler, gas, concentration);
                    switch (result) {
                        case TRUE: favour++; break;
                        case FALSE: against++; break;
                        default: break;
                    }
                }
                return favour > against ? TriState.TRUE : favour < against ? TriState.FALSE : TriState.TRUE;
            });

    TriState checkImmunity(LivingEntity entity, IBreathingHandler breathHandler, IGas gas, float concentration);
}
