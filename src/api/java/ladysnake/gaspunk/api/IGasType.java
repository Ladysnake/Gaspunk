package ladysnake.gaspunk.api;

public interface IGasType {

    /**
     * Whether this gas type suffocates entities by default (can be overridden by individual gases)
     */
    boolean isToxic();

    /**
     * The texture identifier for this gas type.
     */
    int getId();

    IGas.ParticleTypes getParticleType();

}
