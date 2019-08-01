package ladysnake.gaspunk.api.customization;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

/**
 * Indicates that an item can have its appearance customized with one of the existing {@link GrenadeSkins}
 */
public interface Skinnable {

    String TAG_CUSTOM_SKIN = "gaspunk:grenade_skin";

    default void setSkin(ItemStack stack, GrenadeSkins skin) {
        stack.getOrCreateTag().putInt(TAG_CUSTOM_SKIN, skin.ordinal());
    }

    default GrenadeSkins getSkin(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        if (nbt == null) {
            return GrenadeSkins.NONE;
        }
        return GrenadeSkins.VALUES[nbt.getInt(TAG_CUSTOM_SKIN)];
    }

}
