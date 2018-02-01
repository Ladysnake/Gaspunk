package ladysnake.gaspunk.gas.core;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IGas extends IForgeRegistryEntry<IGas> {
    IGasType getType();

    default boolean isToxic() {
        return getType().isToxic();
    }

    default void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick) {
    }

    default void onExitCloud(EntityLivingBase entity, IBreathingHandler handler) {
    }

    int getColor();

    ParticleTypes getParticleType();

    @SideOnly(Side.CLIENT)
    void renderOverlay(float concentration, float partialTicks, ScaledResolution resolution);

    enum ParticleTypes {
        CHLORINE, TEARGAS, SMOKE, VAPOR
    }
}
