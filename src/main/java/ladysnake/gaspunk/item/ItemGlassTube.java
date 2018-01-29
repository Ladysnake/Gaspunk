package ladysnake.gaspunk.item;

import ladysnake.gaspunk.init.ModGases;
import ladysnake.gaspunk.init.ModItems;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ItemGlassTube extends Item {

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);

        if (raytraceresult == null) {
            return new ActionResult<>(EnumActionResult.PASS, itemstack);
        } else {
            if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockpos = raytraceresult.getBlockPos();

                if (!worldIn.isBlockModifiable(playerIn, blockpos) || !playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack)) {
                    return new ActionResult<>(EnumActionResult.PASS, itemstack);
                }

                if (worldIn.getBlockState(blockpos).getMaterial() == Material.WATER) {
                    worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    return new ActionResult<>(EnumActionResult.SUCCESS, this.turnBottleIntoItem(itemstack, playerIn, ((ItemGasTube)ModItems.GAS_TUBE).getItemStackFor(ModGases.VAPOR)));
                }
            }

            return new ActionResult<>(EnumActionResult.PASS, itemstack);
        }
    }

    protected ItemStack turnBottleIntoItem(ItemStack emptyTubes, EntityPlayer player, ItemStack stack) {
        emptyTubes.shrink(1);
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
