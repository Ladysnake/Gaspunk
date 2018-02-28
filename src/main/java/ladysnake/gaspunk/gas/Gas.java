package ladysnake.gaspunk.gas;

import ladysnake.gaspunk.GasPunkConfig;
import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.client.render.ShaderUtil;
import ladysnake.gaspunk.api.IGas;
import ladysnake.gaspunk.api.IGasType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.awt.image.ColorModel;

public class Gas extends IForgeRegistryEntry.Impl<IGas> implements IGas {
    public static final ResourceLocation GAS_TEX_PATH = new ResourceLocation(GasPunk.MOD_ID, "textures/gui/vapor_overlay.png");
    public static final ResourceLocation NOISE_TEX_PATH = new ResourceLocation(GasPunk.MOD_ID, "textures/gui/noise.png");

    protected final IGasType type;
    protected int color, bottleColor;

    public Gas(IGasType type, int color) {
        this(type, color, color);
    }

    public Gas(IGasType type, int color, int bottleColor) {
        this.type = type;
        this.color = color;
        this.bottleColor = bottleColor;
    }

    @Override
    public IGasType getType() {
        return type;
    }

    /**
     * Returns the RGB value representing the color in the default sRGB
     * {@link ColorModel}.
     * (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are
     * blue).
     *
     * @return the RGB value of the color in the default sRGB
     * <code>ColorModel</code>.
     * @see java.awt.Color#getRGB
     */
    @Override
    public int getColor() {
        return color;
    }

    @Override
    public int getBottleColor() {
        return bottleColor;
    }

    protected ResourceLocation getOverlayTexture() {
        return GasPunkConfig.client.useShaders ? NOISE_TEX_PATH : GAS_TEX_PATH;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderOverlay(float concentration, float partialTicks, ScaledResolution resolution) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        int color = getColor();
        float b = (color & 0xFF) / 255F;
        float g = (color >> 8 & 0xFF) / 255F;
        float r = (color >> 16 & 0xFF) / 255F;
        float a = (color >> 24 & 0xFF) / 255F;
        GlStateManager.color(r, g, b, concentration * a);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Minecraft.getMinecraft().getTextureManager().bindTexture(getOverlayTexture());
        ShaderUtil.useShader(ShaderUtil.test);
        ShaderUtil.setUniform("gasColor", new float[]{r, g, b, a});
        ShaderUtil.setUniform("iTime", (int) System.currentTimeMillis());
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, (double) resolution.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
        bufferbuilder.pos((double) resolution.getScaledWidth(), (double) resolution.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos((double) resolution.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        ShaderUtil.revert();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

}
