package ladysnake.gaspunk.api;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public interface IGasParticleType {

    /**
     * Getter for the texture used by particles emanating from gas clouds of this type. <br>
     * The texture will be automatically registered in the sprite map
     * @return the particle texture for gas clouds
     */
    @Nonnull
    ResourceLocation getParticleTexture();

    /**
     * @return The amount of particles spawned at once by gas clouds of this type.
     */
    int getParticleAmount();

}
