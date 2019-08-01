package ladysnake.pathos.item;

import ladysnake.pathos.Pathos;
import ladysnake.pathos.capability.CapabilitySickness;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.UseAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stat.Stats;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Objects;

public class SyringeItem extends Item {

    public static final String TAG_AFFLICTIONS = Pathos.MOD_ID + ":afflictions";

    @Nonnull
    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
        playerIn.setCurrentHand(handIn);
        return new TypedActionResult<>(ActionResult.SUCCESS, playerIn.getStackInHand(handIn));
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 16;
    }

    @Nonnull
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Nonnull
    @Override
    public ItemStack finishUsing(@Nonnull ItemStack stack, World worldIn, LivingEntity entityLiving) {
        ItemStack result = new ItemStack(ModItems.FILLED_SYRINGE);
        CompoundTag compound = new CompoundTag();
        // the capability's default storage directly gives a list of afflictions, which is what we want
        //noinspection ConstantConditions
        CapabilitySickness.getHandler(entityLiving).ifPresent(handler ->
                compound.put(TAG_AFFLICTIONS, CapabilitySickness.CAPABILITY_SICKNESS.writeNBT(handler, null))
        );
        result.setTag(compound);
        return this.turnSyringeIntoFilledSyringe(stack, entityLiving, result);
    }

    protected ItemStack turnSyringeIntoFilledSyringe(ItemStack emptyTubes, LivingEntity player, ItemStack stack) {
        emptyTubes.decrement(stack.getCount());
        if (player instanceof PlayerEntity) {
            ((PlayerEntity)player).incrementStat(Objects.requireNonNull(Stats.USED.getOrCreateStat(this)));
        }

        if (emptyTubes.isEmpty()) {
            return stack;
        } else {
            if (player instanceof PlayerEntity) {
                if (!((PlayerEntity)player).inventory.insertStack(stack)) {
                    ((PlayerEntity)player).dropItem(stack, false);
                }
            } else {
                player.dropStack(stack, 0);
            }

            return emptyTubes;
        }
    }

}
