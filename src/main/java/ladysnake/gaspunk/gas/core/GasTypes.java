package ladysnake.gaspunk.gas.core;

import ladysnake.gaspunk.api.IGas;
import ladysnake.gaspunk.api.IGasType;

public enum GasTypes implements IGasType {
    GAS(true, IGas.ParticleTypes.VAPOR), SMOKE(false, IGas.ParticleTypes.SMOKE), VAPOR(false, IGas.ParticleTypes.VAPOR);

    private final boolean isToxic;
    private final IGas.ParticleTypes particle;

    GasTypes(boolean isToxic, IGas.ParticleTypes particle) {
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
    public IGas.ParticleTypes getParticleType() {
        return particle;
    }


}
