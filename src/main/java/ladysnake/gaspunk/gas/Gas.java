package ladysnake.gaspunk.gas;

import ladysnake.gaspunk.GasPunk;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Gas extends IForgeRegistryEntry.Impl<Gas> {
    public static final ResourceLocation GAS_TEX_PATH = new ResourceLocation(GasPunk.MOD_ID, "textures/gui/gas_overlay.png");

    public boolean isToxic() {
        return false;
    }

    public void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration) {

    }

    public int getColor() {
        return 0xFFFFFF00;
    }

    @SideOnly(Side.CLIENT)
    public void renderOverlay(float concentration, float partialTicks, ScaledResolution resolution) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        int color = getColor();
        int a = (color & 0xFF) / 0xFF;
        int b = (color >> 8 & 0xFF) / 0xFF;
        int g = (color >> 16 & 0xFF) / 0xFF;
        int r = (color >> 24 & 0xFF) / 0xFF;
        GlStateManager.color(r, g, b, concentration * a);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Minecraft.getMinecraft().getTextureManager().bindTexture(GAS_TEX_PATH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, (double)resolution.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
        bufferbuilder.pos((double)resolution.getScaledWidth(), (double)resolution.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos((double)resolution.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

}
