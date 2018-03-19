package ladysnake.gaspunk.api;

import java.util.Map;

public interface IBreathingHandler {

    /**
     * Sets the concentration of the given gas in the target's environment for this tick
     *
     * @param gas           a gas being breathed by this handler
     * @param concentration a <code>float</code> between 0 and 1 representing the gas' concentration
     */
    void setConcentration(IGas gas, float concentration);

    Map<IGas, Float> getGasConcentrations();

    void tick();

    /**
     * Returns whether the entity is immune to the given gas.
     *
     * @param gas the type of gas affecting the entity
     * @param concentration the concentration of the gas in the air surrounding the entity
     * @return true if this handler is immune to the given gas under the given concentration
     * @see ladysnake.gaspunk.api.event.GasEvent.GasImmunityEvent
     */
    boolean isImmune(IGas gas, float concentration);

    float getAirSupply();

    void setAirSupply(float airSupply);

}
