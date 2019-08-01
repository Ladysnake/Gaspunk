package ladysnake.gaspunk.common.item;

import ladysnake.gaspunk.common.GasPunk;
import ladysnake.gaspunk.api.customization.Skinnable;
import ladysnake.gaspunk.common.util.SpecialRewardChecker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SkinItem extends Item implements Skinnable {

    public static final Identifier CUSTOM_SKIN_PROPERTY = new Identifier(GasPunk.MOD_ID, "custom_skin");
    public static final ItemPropertyGetter CUSTOM_SKIN_GETTER = ((stack, worldIn, entityIn) ->
            stack.getTagCompound() != null ? stack.getTagCompound().getInt(TAG_CUSTOM_SKIN) : 0F);

    public SkinItem() {
        addPropertyOverride(CUSTOM_SKIN_PROPERTY, CUSTOM_SKIN_GETTER);
    }

    // this is only used by the diffuser item
    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        setSkin(stack, SpecialRewardChecker.getSelectedSkin(playerIn));
    }
}
