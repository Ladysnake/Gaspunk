package ladysnake.gaspunk.compat;

import baubles.api.BaublesApi;
import ladysnake.gaspunk.event.GasEvent;
import ladysnake.gaspunk.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaublesCompatHandler {

    @SubscribeEvent
    public void onGasImmunity(GasEvent.GasImmunityEvent event) {
        if (event.getEntity() instanceof EntityPlayer && BaublesApi.isBaubleEquipped((EntityPlayer) event.getEntity(), ModItems.GAS_MASK) != -1)
            event.setImmune(true);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HELMET) {
            if (BaublesApi.isBaubleEquipped(Minecraft.getMinecraft().player, ModItems.GAS_MASK) != -1)
                ModItems.GAS_MASK.renderHelmetOverlay(null, Minecraft.getMinecraft().player, event.getResolution(), event.getPartialTicks());
        }
    }

}
