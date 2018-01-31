package ladysnake.gaspunk.client.render;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.gas.core.CapabilityBreathing;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.AIR;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID, value = Side.CLIENT)
public class RenderHandler {

    private static final GuiIngameGaspunk guiIngame = new GuiIngameGaspunk(Minecraft.getMinecraft());

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
          guiIngame.renderAir(event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight());
      }
    }

}
