package ladysnake.gaspunk.gas;

import java.util.Map;

public interface IBreathingHandler {

    void setConcentration(Gas gas, float concentration);

    Map<Gas, Float> getGasConcentrations();

    void tick();

    float getAirSupply();

    void setAirSupply(float airSupply);

}
