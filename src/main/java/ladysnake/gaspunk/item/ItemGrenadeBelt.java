package ladysnake.gaspunk.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.render.IRenderBauble;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemGrenadeBelt extends Item implements IBauble, IRenderBauble {
    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.BELT;
    }

    @Override
    public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {

    }
}
