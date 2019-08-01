package ladysnake.gaspunk.common.item;

import ladysnake.gaspunk.common.gas.Gases;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ItemGlassTube extends Item {

    public ItemGlassTube() {
        this.setMaxStackSize(16);
    }

    @Nonnull
    @Override
    public TypedActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
        ItemStack itemstack = playerIn.getStackInHand(handIn);
        HitResult raytraceresult = this.rayTrace(worldIn, playerIn, true);

        //noinspection ConstantConditions
        if (raytraceresult == null) {
            return new TypedActionResult<>(ActionResult.PASS, itemstack);
        } else {
            if (raytraceresult.typeOfHit == HitResult.Type.field_1332) {
                BlockPos blockpos = raytraceresult.getBlockPos();

                if (!worldIn.isBlockModifiable(playerIn, blockpos) || !playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack)) {
                    return new TypedActionResult<>(ActionResult.PASS, itemstack);
                }

                if (worldIn.getBlockState(blockpos).getMaterial() == Material.WATER) {
                    worldIn.playSound(playerIn, playerIn.x, playerIn.y, playerIn.z, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.field_15254, 1.0F, 1.0F);
                    return new TypedActionResult<>(ActionResult.SUCCESS, this.turnBottleIntoItem(itemstack, playerIn, ((ItemGasTube) GasPunkItems.GAS_TUBE).getItemStackFor(Gases.AIR)));
                }
            }

            return new TypedActionResult<>(ActionResult.PASS, itemstack);
        }
    }

    protected ItemStack turnBottleIntoItem(ItemStack emptyTubes, PlayerEntity player, ItemStack stack) {
        emptyTubes.decrement(1);
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
