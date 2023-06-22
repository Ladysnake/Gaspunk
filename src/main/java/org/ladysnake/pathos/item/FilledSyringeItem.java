package org.ladysnake.pathos.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.pathos.Pathos;
import org.ladysnake.pathos.api.Sickness;
import org.ladysnake.pathos.api.SicknessEffect;
import org.ladysnake.pathos.api.SicknessHandler;
import org.ladysnake.pathos.init.PathosItems;

import java.util.List;

public class FilledSyringeItem extends SyringeItem {

    public FilledSyringeItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        SicknessHandler handler = SicknessHandler.of(user);
        NbtCompound nbt = stack.getOrCreateSubNbt(Pathos.MODID);
        NbtList list = nbt.getList("afflictions", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < list.size(); i++) {
            NbtCompound compound = list.getCompound(i);
            handler.addSickness(new SicknessEffect(compound));
        }
        handler.sync();

        ItemStack result = new ItemStack(PathosItems.SYRINGE);
        if(user instanceof PlayerEntity player) {
            return ItemUsage.exchangeStack(stack, player, result);
        }

        return result;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getSubNbt(Pathos.MODID);
        if (nbt != null) {
            NbtList afflictions = nbt.getList("afflictions", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < afflictions.size(); i++) {
                NbtCompound compound = afflictions.getCompound(i);
                Sickness sickness = SicknessEffect.getSickness(compound);
                if(sickness != null) {
                    tooltip.add(Text.translatable(sickness.getTranslationKey()));
                }
            }
        }
    }
}
