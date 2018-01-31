package ladysnake.gaspunk.event;

import ladysnake.gaspunk.gas.core.CapabilityBreathing;
import ladysnake.gaspunk.gas.core.IBreathingHandler;
import ladysnake.gaspunk.gas.core.IGas;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * GasEvent is fired whenever an event involving gases occurs.<br>
 * If a method utilizes this {@link Event} as its parameter, the method will
 * receive every child event of this class.<br>
 * <br>
 * All children of this event are fired on the {@link MinecraftForge#EVENT_BUS}.<br>
 **/
public class GasEvent extends LivingEvent {
    protected final IBreathingHandler breathHandler;

    public GasEvent(EntityLivingBase entity, IBreathingHandler breathHandler) {
        super(entity);
        this.breathHandler = breathHandler;
    }

    public IBreathingHandler getBreathHandler() {
        return breathHandler;
    }

    /**
     * Fired when an entity gets affected by a gas <br/>
     * This event is {@link Cancelable}. If it is cancelled, the gas effect will not be applied this tick <br/>
     */
    @Cancelable
    public static class GasTickEvent extends GasEvent {
        private final IGas gas;
        private final float concentration;
        private final boolean firstBreathingTick;

        public GasTickEvent(EntityLivingBase entity, IBreathingHandler breathHandler, IGas gas, float concentration, boolean firstBreathingTick) {
            super(entity, breathHandler);
            this.gas = gas;
            this.concentration = concentration;
            this.firstBreathingTick = firstBreathingTick;
        }

        /**
         * @return true if the entity was not breathing this gas the previous tick
         */
        public boolean isFirstBreathingTick() {
            return firstBreathingTick;
        }

        public float getConcentration() {
            return concentration;
        }

        public IGas getGas() {
            return gas;
        }
    }

    /**
     * Fired when an entity is no longer affected by a gas <br/>
     * This event is {@link Cancelable]. If it is cancelled, the gas effect will persist for one more tick
     */
    @Cancelable
    public static class ExitGasCloudEvent extends GasEvent {
        private final IGas gas;
        private final float concentration;

        public ExitGasCloudEvent(EntityLivingBase entity, IBreathingHandler breathHandler, IGas gas, float concentration) {
            super(entity, breathHandler);
            this.gas = gas;
            this.concentration = concentration;
        }

        public float getConcentration() {
            return concentration;
        }

        public IGas getGas() {
            return gas;
        }
    }

    /**
     * GasImmunityEvent is fired whenever an entity checks whether they're affected by surrounding gases
     * in {@link CapabilityBreathing.DefaultBreathingHandler#isImmune()}.<br>
     * <p>
     * <br>
     */
    public static class GasImmunityEvent extends GasEvent {
        private boolean immune;

        public GasImmunityEvent(EntityLivingBase entity, IBreathingHandler breathHandler, boolean immune) {
            super(entity, breathHandler);
            this.immune = immune;
        }

        public boolean isImmune() {
            return immune;
        }

        public void setImmune(boolean immune) {
            this.immune = immune;
        }
    }
}
