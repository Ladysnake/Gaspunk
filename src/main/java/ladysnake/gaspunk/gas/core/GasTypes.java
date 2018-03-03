package ladysnake.gaspunk.gas.core;

import ladysnake.gaspunk.api.IGasParticleType;
import ladysnake.gaspunk.api.IGasType;

public enum GasTypes implements IGasType {
    GAS(true, GasParticleTypes.VAPOR), SMOKE(false, GasParticleTypes.SMOKE), VAPOR(false, GasParticleTypes.VAPOR);

    private final boolean isToxic;
    private final IGasParticleType particle;

    GasTypes(boolean isToxic, IGasParticleType particle) {
        this.isToxic = isToxic;
        this.particle = particle;
    }

    public boolean isToxic() {
        return isToxic;
    }

    @Override
    public int getId() {
        return ordinal();
    }

    @Override
    public IGasParticleType getParticleType() {
        return particle;
    }


}
