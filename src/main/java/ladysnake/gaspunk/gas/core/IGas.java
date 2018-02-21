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
        // NO-OP
    }

    default void onExitCloud(EntityLivingBase entity, IBreathingHandler handler) {
        // NO-OP
    }

    int getColor();

    default int getBottleColor() {
        return getColor();
    }

    default ParticleTypes getParticleType() {
        return getType().getParticleType();
    }

    @SideOnly(Side.CLIENT)
    void renderOverlay(float concentration, float partialTicks, ScaledResolution resolution);

    default String getUnlocalizedName() {
        // pattern : gas.<modid>.<name>
        return ("gas." + getRegistryName()).replace(':', '.');
    }

    enum ParticleTypes {
        CHLORINE, TEARGAS, SMOKE, VAPOR
    }
}
