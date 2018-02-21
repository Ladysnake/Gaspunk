package ladysnake.gaspunk.gas.core;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.awt.*;
import java.awt.image.ColorModel;

public interface IGas extends IForgeRegistryEntry<IGas> {

    IGasType getType();

    /**
     * @return true if this gas triggers asphyxia
     */
    default boolean isToxic() {
        return getType().isToxic();
    }

    /**
     * Called each tick to affect entities inside a gas cloud
     * @param entity the entity breathing this gas
     * @param handler the entity's breathing handler
     * @param concentration the concentration of this gas in the air breathed by the entity
     * @param firstTick true if this entity was not affected by this gas during the previous tick
     */
    default void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick) {
        // NO-OP
    }

    /**
     * Called the tick after an entity has stopped being affected directly by this gas
     * Can be used to clean up toggled effects
     * @param entity the entity that has stopped breathing this gas
     * @param handler the entity's breathing handler
     */
    default void onExitCloud(EntityLivingBase entity, IBreathingHandler handler) {
        // NO-OP
    }

    /**
     * Returns the RGB value representing the color of this gas in the default sRGB
     * {@link ColorModel}.
     * (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are
     * blue).
     *
     * @return the RGB value of the color in the default sRGB
     * <code>ColorModel</code>.
     * @see #getBottleColor()
     * @see java.awt.Color#getRGB
     */
    int getColor();

    /**
     * Returns the RGB value representing the color of the liquid associated with this gas
     * (What color the gas layer will be in the tube and grenades item)
     * @return the RGB value of the color in the default sRGB
     * <code>ColorModel</code>.
     * @see #getColor()
     * @see Color#getRGB()
     */
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
