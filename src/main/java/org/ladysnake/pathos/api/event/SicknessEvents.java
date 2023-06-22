package org.ladysnake.pathos.api.event;

import net.minecraft.entity.LivingEntity;
import org.ladysnake.pathos.api.SicknessEffect;
import org.ladysnake.pathos.api.SicknessHandler;
import org.quiltmc.qsl.base.api.event.Event;

import java.util.function.BiFunction;

public class SicknessEvents {

    /**
     * Fired when an entity gets afflicted by a new sickness effect <br>
     * <p>
     * The merge function is used if another effect of the same type already afflicts the entity
     * <p>This event is cancelable. If it is cancelled, the sickness effect will not be added</p>
     *
     * @see SicknessHandler#addSickness(SicknessEffect, BiFunction)
     */
    public static final Event<SicknessAddEvent> ON_ADD = Event.create(SicknessAddEvent.class, (listeners) -> (sicknessHandler, affectedEntity, effect, mergeFunction) -> {
        for (SicknessAddEvent listener : listeners) {
            if(!listener.onSicknessAdd(sicknessHandler, affectedEntity, effect, mergeFunction)) {
                return false;
            }
        }
        return true;
    });


    public interface SicknessAddEvent {

        boolean onSicknessAdd(SicknessHandler sicknessHandler, LivingEntity affectedEntity, SicknessEffect effect, BiFunction<SicknessEffect, SicknessEffect, SicknessEffect> mergeFunction);
    }

    /**
     * Fired whenever a sickness is about to perform its effect on an entity
     *
     * <p> This event is cancelable. If it is cancelled, the api effect will not be performed for this tick</p>
     *
     * @see SicknessEffect#performEffect(net.minecraft.entity.LivingEntity)
     */
    public static final Event<SicknessTickEvent> ON_TICK = Event.create(SicknessTickEvent.class, (listeners) -> (sicknessHandler, affectedEntity, effect) -> {
        for (SicknessTickEvent listener : listeners) {
            if(!listener.onTick(sicknessHandler, affectedEntity, effect)) {
                return false;
            }
        }
        return true;
    });

    public interface SicknessTickEvent {

        boolean onTick(SicknessHandler sicknessHandler, LivingEntity affectedEntity, SicknessEffect effect);
    }

    /**
     * Fired when an entity is about to get cured from a api effect
     *
     * <p>This event is Cancelable. If it is cancelled, the api effect will not be cured for this tick</p>
     */
    public static final Event<SicknessCureEvent> ON_CURE = Event.create(SicknessCureEvent.class, (listeners) -> (sicknessHandler, affectedEntity, effect) -> {
        for (SicknessCureEvent listener : listeners) {
            if(!listener.onCure(sicknessHandler, affectedEntity, effect)) {
                return false;
            }
        }
        return true;
    });

    public interface SicknessCureEvent {

        boolean onCure(SicknessHandler sicknessHandler, LivingEntity affectedEntity, SicknessEffect effect);

    }

}
