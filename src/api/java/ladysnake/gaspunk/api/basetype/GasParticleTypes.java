package ladysnake.gaspunk.api.basetype;

import ladysnake.gaspunk.api.IGasParticleType;
import net.minecraft.util.Identifier;
import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * Default particle types available for gases. Those are the only ones usable normally in json.
 */
public enum GasParticleTypes implements IGasParticleType {
    SMOKE(5), GAS(1), CANDYFLOSS(5);

    private final Identifier particleTexture;
    private int particleAmount;

    GasParticleTypes(int particleAmount) {
        this.particleAmount = particleAmount;
        particleTexture = new Identifier("gaspunk", "entity/particle_" + this.name().toLowerCase(Locale.ENGLISH));
    }

    /**
     * Returns the texture used by particles emanating from gas clouds of this type. <br>
     * The texture will be automatically registered in the sprite map
     */
    @Nonnull
    @Override
    public Identifier getParticleTexture() {
        return particleTexture;
    }

    /**
     * The amount of particles spawned at once by gas clouds of this type.
     */
    @Override
    public int getParticleAmount() {
        return this.particleAmount;
    }
}
