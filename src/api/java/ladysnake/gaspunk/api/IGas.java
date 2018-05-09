package ladysnake.gaspunk.api;

import net.minecraft.block.Block;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.image.ColorModel;
import java.util.List;

/**
 * A gas that can be used in a gas grenade and affect entities that breath it.
 * <p>A gas has a {@link IGasType} which defines graphical properties for the tube and the cloud,<br>
 * a {@link IGasParticleType} which defines particle texture and density used by the cloud</p>
 * <p>Implementations should extend {@link IForgeRegistryEntry.Impl}. <br>
 * For an existing implementation you can extend, see <code>ladysnake.gaspunk.Gas</code>.
 */
public interface IGas extends IForgeRegistryEntry<IGas> {

    IGasType getType();

    /**
     * @return true if this gas triggers asphyxia
     */
    boolean isToxic();

    /**
     * Called each tick to affect entities inside a gas cloud.
     * Delegates to {@link #applyEffect(EntityLivingBase, IBreathingHandler, float, boolean, boolean)}.
     *
     * @param entity        the entity breathing this gas
     * @param handler       the entity's breathing handler
     * @param concentration the concentration of this gas in the air breathed by the entity
     * @param firstTick     true if this entity was not affected by this gas during the previous tick
     */
    default void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick) {
        applyEffect(entity, handler, concentration, firstTick, false);
    }

    /**
     * Called each tick to affect entities inside a gas cloud
     *
     * @param entity        the entity breathing this gas
     * @param handler       the entity's breathing handler
     * @param concentration the concentration of this gas in the air breathed by the entity
     * @param firstTick     true if this entity was not affected by this gas during the previous tick
     * @param forced        true if this gas should apply its effect without checking any prerequisite
     */
    default void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick, boolean forced) {
        // NO-OP
    }

    /**
     * Called the tick after an entity has stopped being affected directly by this gas
     * Can be used to clean up toggled effects
     *
     * @param entity  the entity that has stopped breathing this gas
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
     *
     * @return the RGB value of the color in the default sRGB
     * <code>ColorModel</code>.
     * @see #getColor()
     * @see Color#getRGB()
     */
    default int getBottleColor() {
        return getColor();
    }

    default IGasParticleType getParticleType() {
        return getType().getParticleType();
    }

    /**
     * Called during {@link net.minecraftforge.client.event.RenderGameOverlayEvent.Pre} if the <tt>renderGasOverlay</tt>
     * configuration option is set to true
     */
    @SideOnly(Side.CLIENT)
    void renderOverlay(float concentration, float partialTicks, ScaledResolution resolution);

    /**
     * @return the unlocalized name for this gas
     * @see Item#getUnlocalizedName()
     * @see Block#getUnlocalizedName()
     */
    default String getUnlocalizedName() {
        // pattern : gas.<modid>.<name>
        return ("gas." + getRegistryName()).replace(':', '.');
    }

    /**
     * Can be used to control tooltips on item stacks containing this gas
     *
     * @see Item#addInformation(ItemStack, World, List, ITooltipFlag)
     */
    @SideOnly(Side.CLIENT)
    default void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        // NO-OP
    }


}
