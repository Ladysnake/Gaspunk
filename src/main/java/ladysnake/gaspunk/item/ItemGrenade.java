package ladysnake.gaspunk.item;

import ladysnake.gaspunk.entity.EntityGasCloud;
import ladysnake.gaspunk.entity.EntityGrenade;
import ladysnake.gaspunk.util.GasUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;

public class ItemGrenade extends Item {

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return super.getItemStackLimit(stack);
    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLivingBase, int timeLeft) {
        worldIn.playSound(null, entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote) {
            EntityGrenade grenade = new EntityGrenade(worldIn, entityLivingBase, stack);
            grenade.setCountdown(timeLeft);
            grenade.shoot(entityLivingBase, entityLivingBase.rotationPitch, entityLivingBase.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntity(grenade);
        }
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        stack.shrink(1);
        if (!worldIn.isRemote)
            explode((WorldServer) worldIn, entityLiving.getPositionVector(), stack);
        return stack;
    }

    public void explode(WorldServer worldIn, Vec3d pos, ItemStack stack) {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.x, pos.y, pos.z, 20, 0.5, 0.5, 0.5, 0.2);
            EntityGasCloud cloud = new EntityGasCloud(worldIn, GasUtil.getContainedGas(stack));
            cloud.setPosition(pos.x, pos.y, pos.z);
            worldIn.spawnEntity(cloud);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 60;
    }

    @Nonnull
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BLOCK;
    }
}
