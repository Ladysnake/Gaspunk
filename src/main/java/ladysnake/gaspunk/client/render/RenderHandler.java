package ladysnake.gaspunk.client.render;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.gas.core.CapabilityBreathing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID, value = Side.CLIENT)
public class RenderHandler {

    public static final GuiIngameGaspunk GUI_INGAME_GASPUNK = new GuiIngameGaspunk(Minecraft.getMinecraft());

    public static final ResourceLocation TEXTURE = new ResourceLocation(GasPunk.MOD_ID, "textures/gui/noise.png");

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            CapabilityBreathing.getHandler(Minecraft.getMinecraft().player)
                    .ifPresent(handler -> handler.getGasConcentrations()
                            .forEach((gas, concentration) -> gas.renderOverlay(concentration, event.getPartialTicks(), event.getResolution())));
        }
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
            GUI_INGAME_GASPUNK.renderAir(event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight());
        }
    }


    @SubscribeEvent
    public static void onRenderGameOverlayPost(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            ScaledResolution scaledRes = event.getResolution();
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableAlpha();
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
            ShaderUtil.useShader(ShaderUtil.test);
            EntityPlayer player = Minecraft.getMinecraft().player;
            ShaderUtil.setUniform("playerPosition", new float[]{(float) player.posX, (float) player.posY + player.eyeHeight, (float) player.posZ});
            ShaderUtil.setUniform("center", new float[]{(float) player.posX, (float) player.posY + player.height / 2, (float) player.posZ});
            ShaderUtil.setUniform("radius", 3);
            ShaderUtil.setUniform("gasColor", new float[]{1, 1, 1, 1});
            ShaderUtil.setUniform("iTime", (int) System.currentTimeMillis());
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(0.0D, (double)scaledRes.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
            bufferbuilder.pos((double)scaledRes.getScaledWidth(), (double)scaledRes.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
            bufferbuilder.pos((double)scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
            bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
            tessellator.draw();
            ShaderUtil.revert();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
