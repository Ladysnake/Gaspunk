package ladysnake.gaspunk.event;

import baubles.api.BaublesApi;
import ladysnake.gaspunk.init.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BaublesCompatHandler {

    @SubscribeEvent
    public void onGasImmunity(GasEvent.GasImmunityEvent event) {
        if (event.getEntity() instanceof EntityPlayer && BaublesApi.isBaubleEquipped((EntityPlayer) event.getEntity(), ModItems.GAS_MASK) != -1)
            event.setImmune(true);
    }

}
