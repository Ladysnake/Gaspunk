package ladysnake.gaspunk.gas.core;

import java.util.Map;

public interface IBreathingHandler {

    void setConcentration(IGas gas, float concentration);

    Map<IGas, Float> getGasConcentrations();

    void tick();

    boolean isImmune();

    float getAirSupply();

    void setAirSupply(float airSupply);

}
