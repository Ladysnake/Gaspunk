package ladysnake.gaspunk.item;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.api.customization.IHasSkin;
import ladysnake.gaspunk.util.SpecialRewardChecker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class SkinItem extends Item implements IHasSkin {

    public static final ResourceLocation CUSTOM_SKIN_PROPERTY = new ResourceLocation(GasPunk.MOD_ID, "custom_skin");
    public static final IItemPropertyGetter CUSTOM_SKIN_GETTER = ((stack, worldIn, entityIn) ->
            stack.getTagCompound() != null ? stack.getTagCompound().getInteger(TAG_CUSTOM_SKIN) : 0F);

    public SkinItem() {
        addPropertyOverride(CUSTOM_SKIN_PROPERTY, CUSTOM_SKIN_GETTER);
    }

    // this is only used by the diffuser item
    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        setSkin(stack, SpecialRewardChecker.getSelectedSkin(playerIn));
    }
}
