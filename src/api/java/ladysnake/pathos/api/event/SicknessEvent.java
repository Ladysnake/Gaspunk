package ladysnake.pathos.api.event;

import ladysnake.pathos.api.ISicknessHandler;
import ladysnake.pathos.api.SicknessEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.function.BiFunction;

/**
 * GasEvent is fired whenever an event involving sicknesses occurs.<br>
 * If a method utilizes this {@link Event} as its parameter, the method will
 * receive every child event of this class.<br>
 * <br>
 * All children of this event are fired on the {@link MinecraftForge#EVENT_BUS}.<br>
 */
public class SicknessEvent extends LivingEvent {
    protected final ISicknessHandler sicknessHandler;
    protected final SicknessEffect effect;

    public SicknessEvent(ISicknessHandler sicknessHandler, LivingEntity entity, SicknessEffect effect) {
        super(entity);
        this.sicknessHandler = sicknessHandler;
        this.effect = effect;
    }

    public SicknessEffect getEffect() {
        return effect;
    }

    /**
     * Fired when an entity gets afflicted by a new sickness effect <br>
     *
     * The merge function is used if another effect of the same type already afflicts the entity
     * <p>This event is {@link Cancelable}. If it is cancelled, the sickness effect will not be added</p>
     *
     * @see ladysnake.pathos.api.ISicknessHandler#addSickness(SicknessEffect, BiFunction)
     */
    @Cancelable
    public static class SicknessAddEvent extends SicknessEvent {

        private BiFunction<SicknessEffect, SicknessEffect, SicknessEffect> mergeFunction;

        public SicknessAddEvent(ISicknessHandler sicknessHandler, LivingEntity entity, SicknessEffect effect, BiFunction<SicknessEffect, SicknessEffect, SicknessEffect> mergeFunction) {
            super(sicknessHandler, entity, effect);
            this.mergeFunction = mergeFunction;
        }

        public BiFunction<SicknessEffect, SicknessEffect, SicknessEffect> getMergeFunction() {
            return mergeFunction;
        }

        public void setMergeFunction(BiFunction<SicknessEffect, SicknessEffect, SicknessEffect> mergeFunction) {
            this.mergeFunction = mergeFunction;
        }
    }

    /**
     * Fired whenever a sickness is about to perform its effect on an entity
     *
     * <p> This event is {@link Cancelable}. If it is cancelled, the sickness effect will not be performed for this tick</p>
     *
     * @see SicknessEffect#performEffect(LivingEntity)
     */
    public static class SicknessTickEvent extends SicknessEvent {

        public SicknessTickEvent(ISicknessHandler sicknessHandler, LivingEntity entity, SicknessEffect effect) {
            super(sicknessHandler, entity, effect);
        }
    }

    /**
     * Fired when an entity is about to get cured from a sickness effect
     *
     * <p>This event is {@link Cancelable}. If it is cancelled, the sickness effect will not be cured for this tick</p>
     */
    @Cancelable
    public static class SicknessCureEvent extends SicknessEvent {

        public SicknessCureEvent(ISicknessHandler sicknessHandler, LivingEntity entity, SicknessEffect effect) {
            super(sicknessHandler, entity, effect);
        }
    }

}
