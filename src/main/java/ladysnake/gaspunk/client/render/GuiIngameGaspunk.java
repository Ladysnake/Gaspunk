package ladysnake.gaspunk.client.render;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.gas.core.CapabilityBreathing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.GuiIngameForge;

public class GuiIngameGaspunk extends GuiIngameForge {
    public static final ResourceLocation CUSTOM_ICONS = new ResourceLocation(GasPunk.MOD_ID, "textures/gui/icons.png");

    public GuiIngameGaspunk(Minecraft mc) {
        super(mc);
    }

    protected void renderAir(int width, int height) {
        mc.profiler.startSection("air");
        EntityPlayer player = (EntityPlayer) this.mc.getRenderViewEntity();
        if (player == null || player.isCreative()) return;
        float air = CapabilityBreathing.getHandler(player).orElseThrow(IllegalStateException::new).getAirSupply();
        if (air >= 300) return;
        mc.renderEngine.bindTexture(CUSTOM_ICONS);
        GlStateManager.enableBlend();
        int left = width / 2 + 91;
        int top = height - GuiIngameForge.right_height;

        int full = MathHelper.ceil((air - 2) * 10.0D / 300.0D);
        int partial = MathHelper.ceil((double) air * 10.0D / 300.0D) - full;

        for (int i = 0; i < full + partial; ++i) {
            drawTexturedModalRect(left - i * 8 - 9, top, (i < full ? 0 : 9), 0, 9, 9);
        }
        right_height += 10;

        GlStateManager.disableBlend();
        mc.profiler.endSection();
    }

}
