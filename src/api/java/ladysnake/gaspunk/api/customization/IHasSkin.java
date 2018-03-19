package ladysnake.gaspunk.api.customization;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Indicates that an item can have its appearance customized with one of the existing {@link GrenadeSkins}
 */
public interface IHasSkin {

    String TAG_CUSTOM_SKIN = "grenade_skin";

    default void setSkin(ItemStack stack, GrenadeSkins skin) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) {
            nbt = new NBTTagCompound();
            stack.setTagCompound(nbt);
        }
        nbt.setInteger(TAG_CUSTOM_SKIN, skin.ordinal());
    }

    default GrenadeSkins getSkin(ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) {
            return GrenadeSkins.NONE;
        }
        return GrenadeSkins.VALUES[nbt.getInteger(TAG_CUSTOM_SKIN)];
    }

}
