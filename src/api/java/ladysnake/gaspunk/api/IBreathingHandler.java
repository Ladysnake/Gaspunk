package ladysnake.gaspunk.api;

import java.util.Map;

public interface IBreathingHandler {

    /**
     * Sets the concentration of the given gas in the target's environment for this tick
     * @param gas a gas being breathed by this handler
     * @param concentration a <code>float</code> between 0 and 1 representing the gas' concentration
     */
    void setConcentration(IGas gas, float concentration);

    Map<IGas, Float> getGasConcentrations();

    void tick();

    /**
     * Returns whether the entity is immune to gases. Currently, this prevents every gas from ticking.
     * <b>The exact effect of this method is likely to change in future updates</b>
     * @return true if this handler is wearing a gas mask
     * @see ladysnake.gaspunk.api.event.GasEvent.GasImmunityEvent
     */
    boolean isImmune();

    float getAirSupply();

    void setAirSupply(float airSupply);

}
