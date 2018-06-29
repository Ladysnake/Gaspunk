package ladysnake.gaspunk.api.event;

import ladysnake.gaspunk.api.IBreathingHandler;
import ladysnake.gaspunk.api.IGas;
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
    protected final IGas gas;
    protected final float concentration;

    public GasEvent(EntityLivingBase entity, IBreathingHandler breathHandler, IGas gas, float concentration) {
        super(entity);
        this.breathHandler = breathHandler;
        this.gas = gas;
        this.concentration = concentration;
    }

    public IBreathingHandler getBreathHandler() {
        return breathHandler;
    }

    public IGas getGas() {
        return gas;
    }

    /**
     * Gets the concentration of the gas in the air breathed by the entity
     * @return a gas concentration normally between 0 and 1
     */
    public float getConcentration() {
        return concentration;
    }

    /**
     * Fired when an entity starts breathing a gas that it wasn't breathing the previous tick <br>
     * This event is fired in addition to {@link GasTickEvent}. It will always be fired before the latter.
     */
    public static class GasEnterEvent extends GasEvent {
        public GasEnterEvent(EntityLivingBase entity, IBreathingHandler breathHandler, IGas gas, float concentration) {
            super(entity, breathHandler, gas, concentration);
        }
    }

    /**
     * Fired when an entity gets affected by a gas <br>
     * This event is {@link Cancelable}. If it is cancelled, the gas effect will not be applied this tick <br>
     */
    @Cancelable
    public static class GasTickEvent extends GasEvent {
        public GasTickEvent(EntityLivingBase entity, IBreathingHandler breathHandler, IGas gas, float concentration) {
            super(entity, breathHandler, gas, concentration);
        }
    }

    /**
     * Fired when an entity is no longer affected by a gas <br>
     * This event is {@link Cancelable}. If it is cancelled, the gas effect will persist for one more tick
     */
    @Cancelable
    public static class ExitGasCloudEvent extends GasEvent {
        public ExitGasCloudEvent(EntityLivingBase entity, IBreathingHandler breathHandler, IGas gas, float concentration) {
            super(entity, breathHandler, gas, concentration);
        }
    }

    /**
     * GasImmunityEvent is fired whenever an entity checks whether they're affected by surrounding gases
     * in {@link IBreathingHandler#isImmune(IGas, float)}. <br>
     */
    public static class GasImmunityEvent extends GasEvent {
        private boolean immune;

        public GasImmunityEvent(EntityLivingBase entity, IBreathingHandler breathHandler, IGas gas, float concentration, boolean immune) {
            super(entity, breathHandler, gas, concentration);
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
