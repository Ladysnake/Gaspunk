package ladysnake.pathos.item;

import ladysnake.pathos.capability.CapabilitySickness;
import ladysnake.pathos.api.ISickness;
import ladysnake.pathos.sickness.Sickness;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemFilledSyringe extends ItemSyringe {

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 16;
    }

    @Nonnull
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null && nbt.hasKey(TAG_AFFLICTIONS))
            CapabilitySickness.getHandler(entityLiving).ifPresent(h -> CapabilitySickness.CAPABILITY_SICKNESS.readNBT(h, null, nbt.getTagList(TAG_AFFLICTIONS, 10)));
        if (entityLiving instanceof EntityPlayer)
            return turnSyringeIntoFilledSyringe(stack, (EntityPlayer) entityLiving, new ItemStack(ModItems.EMPTY_SYRINGE));
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null && nbt.hasKey(TAG_AFFLICTIONS)) {
            NBTTagList afflictions = nbt.getTagList(TAG_AFFLICTIONS, 10);
            for (NBTBase affliction : afflictions) {
                ISickness sickness = Sickness.REGISTRY.getValue(new ResourceLocation(((NBTTagCompound)affliction).getString("effect")));
                if (sickness != Sickness.NONE) {
                    tooltip.add(I18n.format(sickness.getUnlocalizedName()));
                }
            }
        }
    }
}
