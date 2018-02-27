package ladysnake.pathos.item;

import ladysnake.pathos.Pathos;
import ladysnake.pathos.capability.CapabilitySickness;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ItemSyringe extends Item {

    public static final String TAG_AFFLICTIONS = Pathos.MOD_ID + ":afflictions";

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        ItemStack result = new ItemStack(ModItems.FILLED_SYRINGE);
        NBTTagCompound compound = new NBTTagCompound();
        // the capability's default storage directly gives a list of afflictions, which is what we want
        //noinspection ConstantConditions
        CapabilitySickness.getHandler(playerIn).ifPresent(handler ->
                compound.setTag(TAG_AFFLICTIONS, CapabilitySickness.CAPABILITY_SICKNESS.writeNBT(handler, null))
        );
        result.setTagCompound(compound);
        return new ActionResult<>(EnumActionResult.SUCCESS, this.turnSyringeIntoFilledSyringe(itemstack, playerIn, result));
    }

    protected ItemStack turnSyringeIntoFilledSyringe(ItemStack emptyTubes, EntityPlayer player, ItemStack stack) {
        emptyTubes.shrink(stack.getCount());
        player.addStat(Objects.requireNonNull(StatList.getObjectUseStats(this)));

        if (emptyTubes.isEmpty()) {
            return stack;
        } else {
            if (!player.inventory.addItemStackToInventory(stack)) {
                player.dropItem(stack, false);
            }

            return emptyTubes;
        }
    }

}
