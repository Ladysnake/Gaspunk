package org.ladysnake.pathos.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.ladysnake.pathos.Pathos;
import org.ladysnake.pathos.api.SicknessHandler;
import org.ladysnake.pathos.init.PathosItems;

public class SyringeItem extends Item {

    //TODO custom damage source

    public SyringeItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 16;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack result = new ItemStack(PathosItems.BLOOD_SYRINGE);
        SicknessHandler handler = SicknessHandler.of(user);
        NbtCompound nbt = result.getOrCreateSubNbt(Pathos.MODID);
        NbtList list = new NbtList();
        handler.getActiveSicknesses().forEach(sickness -> list.add(sickness.serializeNBT()));
        nbt.put("afflictions", list);

        NbtCompound source = new NbtCompound();
        source.putString("type", Registries.ENTITY_TYPE.getId(user.getType()).toString());
        if (user instanceof PlayerEntity) {
            source.putUuid("uuid", user.getUuid());
        }
        nbt.put("source", source);

        // use heal instead of damage to avoid knockback
        if(user.getHealth() > 1.0F) {
            user.heal(-1.0F);
        }
        else {
            user.damage(world.getDamageSources().sting(user), 1.0F); // 0.5 hears
        }


        if (user instanceof PlayerEntity player) {
            return ItemUsage.exchangeStack(stack, player, result);
        }
        return result;
    }
}
