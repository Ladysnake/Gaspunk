package ladysnake.gaspunk.gas;

public interface IGasType {

    /**
     * Whether this gas type suffocates entities by default (can be overriden by gases)
     */
    boolean isToxic();

    /**
     * The texture identifier for this gas type.
     */
    int getId();

}
