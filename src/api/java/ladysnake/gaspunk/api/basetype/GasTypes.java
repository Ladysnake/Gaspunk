package ladysnake.gaspunk.api.basetype;

import ladysnake.gaspunk.api.IGasParticleType;
import ladysnake.gaspunk.api.IGasType;

/**
 * Default types available for gases. Those are the only ones usable normally in json.
 */
public enum GasTypes implements IGasType {
    GAS(GasParticleTypes.GAS), SMOKE(GasParticleTypes.SMOKE);

    private final IGasParticleType particle;

    GasTypes(IGasParticleType particle) {
        this.particle = particle;
    }

    /**
     * The texture identifier for this gas type.
     * <p>
     * This id is used by the item model for gas tubes and grenades.
     * </p>
     */
    @Override
    public int getId() {
        return ordinal();
    }

    /**
     * Returns the particle type that will be used if the gas does not specify its own.
     * @return the default particle type to be used by gases using this gas type
     */
    @Override
    public IGasParticleType getParticleType() {
        return particle;
    }


}
