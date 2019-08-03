package ladysnake.gaspunk.client.render;

import ladysnake.gaspunk.common.GasPunk;
import ladysnake.gaspunk.common.config.GasPunkConfig;
import ladysnake.gaspunk.common.gas.core.GasPunkComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID, value = Side.CLIENT)
public class RenderHandler {

    public static final GuiIngameGaspunk GUI_INGAME_GASPUNK = new GuiIngameGaspunk(MinecraftClient.getInstance());

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL && GasPunkConfig.get().shouldRenderGasOverlays()) {
            GasPunkComponents.BREATHING.get(MinecraftClient.getInstance().player).getGasConcentrations()
                            .forEach((gas, concentration) -> gas.renderOverlay(concentration, event.getPartialTicks(), event.getResolution()));
        }
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {
            GUI_INGAME_GASPUNK.renderAir(event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight());
        }
    }

}
