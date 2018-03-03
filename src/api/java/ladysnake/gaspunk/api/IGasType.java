package ladysnake.gaspunk.api;

public interface IGasType {

    /**
     * The texture identifier for this gas type.
     * <p>
     * This id is used by the item model for gas tubes and grenades.
     * If a value that does not exist in the default <code>GasTypes</code> is given, the model file will need to be overridden.
     * </p>
     */
    int getId();

    /**
     * Returns the particle type that will be used if the gas does not specify its own.
     * @return the default particle type to be used by gases using this gas type
     */
    IGasParticleType getParticleType();

}
