package ladysnake.gaspunk.item;

import ladylib.misc.ItemUtil;
import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.api.IGas;
import ladysnake.gaspunk.api.customization.IHasSkin;
import ladysnake.gaspunk.entity.EntityGasCloud;
import ladysnake.gaspunk.entity.EntityGrenade;
import ladysnake.gaspunk.init.ModGases;
import ladysnake.gaspunk.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;


public class ItemGrenade extends ItemGasTube implements IHasSkin {

    public ItemGrenade() {
        super();
        addPropertyOverride(new ResourceLocation(GasPunk.MOD_ID, "unpinned"),
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
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null) {
            return nbt.getBoolean(ItemGrenadeBelt.NBT_TAG_BELT_STACK) ? 4 : 1;
        }
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
        if (!worldIn.isRemote) {
            worldIn.playSound(null, entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            ItemStack stack1;
            // don't shrink the stack if the player is in creative
            if (entityLivingBase instanceof EntityPlayer && ((EntityPlayer) entityLivingBase).isCreative())
                stack1 = stack.copy();
            else
                stack1 = stack.splitStack(1);
            EntityGrenade grenade = new EntityGrenade(worldIn, entityLivingBase, stack1);
            grenade.setCountdown(timeLeft);
            grenade.shoot(entityLivingBase, entityLivingBase.rotationPitch, entityLivingBase.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntity(grenade);
        }
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        if (!worldIn.isRemote) {
            EntityGasCloud cloud = explode((WorldServer) worldIn, entityLiving.getPositionVector(), stack);
            cloud.setEmitter(entityLiving);
        }

        ItemGrenade grenadeItem = (ItemGrenade) stack.getItem();
        ItemStack emptyGrenade = new ItemStack(ModItems.EMPTY_GRENADE);
        ((SkinItem)ModItems.EMPTY_GRENADE).setSkin(emptyGrenade, grenadeItem.getSkin(stack));
        return ItemUtil.turnItemIntoAnother(stack, entityLiving, emptyGrenade, false);
    }

    public EntityGasCloud explode(WorldServer worldIn, Vec3d pos, ItemStack stack) {
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.x, pos.y, pos.z, 20, 0.5, 0.5, 0.5, 0.2);
        EntityGasCloud cloud = new EntityGasCloud(worldIn, getContainedGas(stack));
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
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BLOCK;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (IGas gas : ModGases.REGISTRY.getValues()) {
                items.add(getItemStackFor(gas));
            }
        }
    }

}
