package ladysnake.gaspunk.common.compat;

import baubles.api.BaublesApi;
import ladysnake.gaspunk.api.event.GasEvent;
import ladysnake.gaspunk.common.item.GasPunkItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public final class WearablesCompatHandler {

    public static void init() {

    }

    @SubscribeEvent
    public void onGasImmunity(GasEvent.GasImmunityEvent event) {
        if (event.getEntity() instanceof PlayerEntity && BaublesApi.isBaubleEquipped((PlayerEntity) event.getEntity(), GasPunkItems.GAS_MASK) != -1)
            event.setImmune(true);
    }

    @SubscribeEvent
    @Environment(EnvType.CLIENT)
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HELMET) {
            if (BaublesApi.isBaubleEquipped(MinecraftClient.getInstance().player, GasPunkItems.GAS_MASK) != -1)
                GasPunkItems.GAS_MASK.renderHelmetOverlay(null, MinecraftClient.getInstance().player, event.getResolution(), event.getPartialTicks());
        }
    }

}
