package ladysnake.gaspunk.client.render;

import ladysnake.gaspunk.GasPunkConfig;
import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.gas.core.CapabilityBreathing;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID, value = Side.CLIENT)
public class RenderHandler {

    public static final GuiIngameGaspunk GUI_INGAME_GASPUNK = new GuiIngameGaspunk(Minecraft.getMinecraft());

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL && GasPunkConfig.client.renderGasOverlays) {
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
    public void onRenderPlayer(RenderPlayerEvent.Post event) {
//        ModelBandoulier.BANDOULIER_LAYER.doRender(event.getEntityPlayer(), event.getX(), event.getY(), event.getZ(), event.getEntity().rotationYaw, event.getPartialRenderTick());
    }
}
