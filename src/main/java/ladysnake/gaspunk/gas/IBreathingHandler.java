package ladysnake.gaspunk.gas;

public interface IBreathingHandler {

    void setConcentration(Gas gas, float concentration);

    void tick();

    float getAirSupply();

    void setAirSupply(float airSupply);

}
