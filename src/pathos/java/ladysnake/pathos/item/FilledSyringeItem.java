package ladysnake.pathos.item;

import ladysnake.pathos.api.ISickness;
import ladysnake.pathos.capability.CapabilitySickness;
import ladysnake.pathos.sickness.Sickness;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class FilledSyringeItem extends SyringeItem {

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World worldIn, LivingEntity entityLiving) {
        CompoundTag nbt = stack.getTag();
        if (nbt != null && nbt.containsKey(TAG_AFFLICTIONS))
            CapabilitySickness.getHandler(entityLiving).ifPresent(h -> CapabilitySickness.CAPABILITY_SICKNESS.readNBT(h, null, nbt.getList(TAG_AFFLICTIONS, 10)));
        return turnSyringeIntoFilledSyringe(stack, entityLiving, new ItemStack(ModItems.EMPTY_SYRINGE));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        CompoundTag nbt = stack.getTag();
        if (nbt != null && nbt.containsKey(TAG_AFFLICTIONS)) {
            ListTag afflictions = nbt.getList(TAG_AFFLICTIONS, 10);
            for (Tag affliction : afflictions) {
                ISickness sickness = Sickness.REGISTRY.getValue(new Identifier(((CompoundTag)affliction).getString("effect")));
                if (sickness != Sickness.NONE) {
                    tooltip.add(new TranslatableText(sickness.getUnlocalizedName()));
                }
            }
        }
    }
}
