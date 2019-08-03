package ladysnake.gaspunk.api;

import ladysnake.gaspunk.api.event.GasImmunityCallback;
import nerdhub.cardinal.components.api.component.Component;

import java.util.Map;

public interface IBreathingHandler extends Component {

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
     * @see GasImmunityCallback
     */
    boolean isImmune(IGas gas, float concentration);

    float getAirSupply();

    void setAirSupply(float airSupply);

}
