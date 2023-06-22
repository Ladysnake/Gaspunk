package org.ladysnake.gaspunk.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.ladysnake.gaspunk.GrenadeEntity;

public class GrenadeItem extends Item {

    public GrenadeItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            GrenadeEntity grenade = new GrenadeEntity(user, world);
            grenade.setProperties(user, user.getPitch(), user.getYaw(), -20.0F, 0.5F, 1.0F);
            grenade.setItem(itemStack);
            world.spawnEntity(grenade);
        }
        //TODO play grenade pin removed sound

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
