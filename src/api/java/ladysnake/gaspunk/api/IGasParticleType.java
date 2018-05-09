package ladysnake.gaspunk.api;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public interface IGasParticleType {

    /**
     * Returns the texture used by particles emanating from gas clouds of this type. <br>
     * The texture will be automatically registered in the sprite map
     */
    @Nonnull
    ResourceLocation getParticleTexture();

    /**
     * The amount of particles spawned at once by gas clouds of this type.
     */
    int getParticleAmount();

}
