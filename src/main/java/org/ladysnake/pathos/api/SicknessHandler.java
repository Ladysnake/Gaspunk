package org.ladysnake.pathos.api;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.pathos.Pathos;
import org.ladysnake.pathos.api.event.SicknessEvents;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;

public interface SicknessHandler extends AutoSyncedComponent, ServerTickingComponent {

    ComponentKey<SicknessHandler> COMPONENT_KEY = ComponentRegistry.getOrCreate(Pathos.id("sickness_handler"), SicknessHandler.class);

    static SicknessHandler of(LivingEntity entity) {
        return COMPONENT_KEY.get(entity);
    }

    LivingEntity getEntity();

    /**
     * @param sickness a registered api
     * @return true if this handler is afflicted with an effect of the same type as passed in argument
     */
    boolean isSicknessActive(Sickness sickness);

    /**
     * Adds a api effect.
     * In the default implementation, if a api effect of the same type already exists, the two severities are cumulated
     *
     * @param effect the effect to afflict the component owner with
     * @see SicknessEvents.SicknessAddEvent
     */
    default void addSickness(SicknessEffect effect) {
        addSickness(effect, SicknessEffect::mergeSeverities);
    }

    void cure(Sickness sickness);

    /**
     * Adds a sickness effect. If one already exists, the given merge function is used.
     *
     * @param effect        the effect to afflict the capability owner with
     * @param mergeFunction the function to recompute a value if one is already present
     *                      first parameter will be the old effect, second parameter will be the new
     * @see SicknessEvents.SicknessAddEvent
     * @see Map#merge(Object, Object, BiFunction)
     */
    void addSickness(SicknessEffect effect, BiFunction<SicknessEffect, SicknessEffect, SicknessEffect> mergeFunction);

    /**
     * @return all the sickness effects this handler is afflicted with
     */
    Collection<SicknessEffect> getActiveSicknesses();

    @Nullable
    SicknessEffect getActiveEffect(Sickness sickness);

    default void sync() {
        COMPONENT_KEY.sync(getEntity());
    }
}
