package ladysnake.pathos.item;

import ladysnake.pathos.api.ISickness;
import ladysnake.pathos.capability.CapabilitySickness;
import ladysnake.pathos.sickness.Sickness;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemFilledSyringe extends ItemSyringe {

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null && nbt.hasKey(TAG_AFFLICTIONS))
            CapabilitySickness.getHandler(entityLiving).ifPresent(h -> CapabilitySickness.CAPABILITY_SICKNESS.readNBT(h, null, nbt.getTagList(TAG_AFFLICTIONS, 10)));
        return turnSyringeIntoFilledSyringe(stack, entityLiving, new ItemStack(ModItems.EMPTY_SYRINGE));
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
