package ladysnake.gaspunk.gas.core;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.api.IGasParticleType;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum GasParticleTypes implements IGasParticleType {
    CHLORINE, LACHRYMATOR, SMOKE, VAPOR;

    private final ResourceLocation particleTexture;

    GasParticleTypes() {
        particleTexture = new ResourceLocation(GasPunk.MOD_ID, "entity/particle_" + this.name().toLowerCase(Locale.ENGLISH));
    }

    @Nonnull
    @Override
    public ResourceLocation getParticleTexture() {
        return particleTexture;
    }

    @Override
    public int getParticleAmount() {
        return 1;
    }
}
