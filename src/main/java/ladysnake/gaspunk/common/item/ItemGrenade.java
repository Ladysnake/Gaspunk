package ladysnake.gaspunk.common.item;

import ladylib.misc.ItemUtil;
import ladysnake.gaspunk.common.GasPunk;
import ladysnake.gaspunk.api.IGas;
import ladysnake.gaspunk.api.customization.Skinnable;
import ladysnake.gaspunk.common.entity.GasCloudEntity;
import ladysnake.gaspunk.common.entity.GrenadeEntity;
import ladysnake.gaspunk.common.gas.Gases;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.UseAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.ServerWorld;

import javax.annotation.Nonnull;


public class ItemGrenade extends ItemGasTube implements Skinnable {

    public ItemGrenade() {
        super();
        addPropertyOverride(new Identifier(GasPunk.MOD_ID, "unpinned"),
                ((stack, worldIn, entityIn) ->
                        entityIn != null && entityIn.getActiveItemStack() == stack
                                || stack.getTagCompound() != null && stack.getTagCompound().getBoolean("isEntityGrenade")
                                ? 1
                                : 0));
        addPropertyOverride(SkinItem.CUSTOM_SKIN_PROPERTY, SkinItem.CUSTOM_SKIN_GETTER);
        this.setMaxStackSize(1);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        CompoundTag nbt = stack.getTagCompound();
        if (nbt != null) {
            return nbt.getBoolean(ItemGrenadeBelt.NBT_TAG_BELT_STACK) ? 4 : 1;
        }
        return super.getItemStackLimit(stack);
    }

    @Nonnull
    public TypedActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
        ItemStack itemstack = playerIn.getStackInHand(handIn);
        playerIn.setActiveHand(handIn);
        return new TypedActionResult<>(ActionResult.SUCCESS, itemstack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLivingBase, int timeLeft) {
        if (!worldIn.isClient) {
            worldIn.playSound(null, entityLivingBase.x, entityLivingBase.y, entityLivingBase.z, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.field_15254, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
            ItemStack stack1;
            // don't decrement the stack if the player is in creative
            if (entityLivingBase instanceof PlayerEntity && ((PlayerEntity) entityLivingBase).isCreative())
                stack1 = stack.copy();
            else
                stack1 = stack.splitStack(1);
            GrenadeEntity grenade = new GrenadeEntity(worldIn, entityLivingBase, stack1);
            grenade.setCountdown(timeLeft);
            grenade.shoot(entityLivingBase, entityLivingBase.pitch, entityLivingBase.yaw, 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntity(grenade);
        }
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isClient) {
            GasCloudEntity cloud = explode((ServerWorld) worldIn, entityLiving.getPositionVector(), stack);
            cloud.setEmitter(entityLiving);
        }

        ItemGrenade grenadeItem = (ItemGrenade) stack.getItem();
        ItemStack emptyGrenade = new ItemStack(GasPunkItems.EMPTY_GRENADE);
        ((SkinItem) GasPunkItems.EMPTY_GRENADE).setSkin(emptyGrenade, grenadeItem.getSkin(stack));
        return ItemUtil.turnItemIntoAnother(stack, entityLiving, emptyGrenade, false);
    }

    public GasCloudEntity explode(ServerWorld worldIn, Vec3d pos, ItemStack stack) {
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.x, pos.y, pos.z, 20, 0.5, 0.5, 0.5, 0.2);
        GasCloudEntity cloud = new GasCloudEntity(worldIn, getContainedGas(stack));
        cloud.setPosition(pos.x, pos.y, pos.z);
        cloud.setMaxLifespan(600);
        worldIn.spawnEntity(cloud);
        return cloud;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 60;
    }

    @Nonnull
    @Override
    public UseAction getItemUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull DefaultedList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (IGas gas : Gases.GAS_REGISTRY.getValues()) {
                items.add(getItemStackFor(gas));
            }
        }
    }

}
