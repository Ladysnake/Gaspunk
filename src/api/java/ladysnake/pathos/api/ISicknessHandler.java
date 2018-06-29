package ladysnake.pathos.api;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;

public interface ISicknessHandler {

    /**
     * @param sickness a registered sickness
     * @return true if this handler is afflicted with an effect of the same type as passed in argument
     */
    boolean isSicknessActive(ISickness sickness);

    /**
     * Adds a sickness effect.
     * In the default implementation, if a sickness effect of the same type already exists, the two severities are cumulated
     *
     * @param effect the effect to afflict the capability owner with
     * @see ladysnake.pathos.api.event.SicknessEvent.SicknessAddEvent
     */
    default void addSickness(SicknessEffect effect) {
        addSickness(effect, SicknessEffect::mergeSeverities);
    }

    /**
     * Adds a sickness effect. If one already exists, the given merge function is used.
     *
     * @param effect        the effect to afflict the capability owner with
     * @param mergeFunction the function to recompute a value if one is already present
     *                      first parameter will be the old effect, second parameter will be the new
     * @see ladysnake.pathos.api.event.SicknessEvent.SicknessAddEvent
     * @see Map#merge(Object, Object, BiFunction)
     */
    void addSickness(SicknessEffect effect, BiFunction<SicknessEffect, SicknessEffect, SicknessEffect> mergeFunction);

    /**
     * Ticks this handler, making all afflictions perform their effect
     */
    void tick();

    /**
     * @return all the sickness effects this handler is afflicted with
     */
    Collection<SicknessEffect> getActiveSicknesses();

    SicknessEffect getActiveEffect(ISickness sickness);

}
