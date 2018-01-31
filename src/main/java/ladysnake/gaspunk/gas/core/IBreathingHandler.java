package ladysnake.gaspunk.gas.core;

import ladysnake.gaspunk.gas.Gas;

import java.util.Map;

public interface IBreathingHandler {

    void setConcentration(Gas gas, float concentration);

    Map<Gas, Float> getGasConcentrations();

    void tick();

    boolean isImmune();

    float getAirSupply();

    void setAirSupply(float airSupply);

}
