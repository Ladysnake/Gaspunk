package ladysnake.gaspunk.item;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.entity.EntityGasCloud;
import ladysnake.gaspunk.entity.EntityGrenade;
import ladysnake.gaspunk.init.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;


public class ItemGrenade extends ItemGasTube {

    public ItemGrenade() {
        super();
        addPropertyOverride(new ResourceLocation(GasPunk.MOD_ID, "pinned"),
                ((stack, worldIn, entityIn) -> entityIn != null && entityIn.getActiveItemStack() == stack ? 1 : 0));
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
        worldIn.playSound(null, entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote) {
            EntityGrenade grenade = new EntityGrenade(worldIn, entityLivingBase, stack.splitStack(1));
            grenade.setCountdown(timeLeft);
            grenade.shoot(entityLivingBase, entityLivingBase.rotationPitch, entityLivingBase.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntity(grenade);
            if (entityLivingBase instanceof EntityPlayer) {
                ItemStack diffuser = new ItemStack(ModItems.DIFFUSER);
                if(!((EntityPlayer) entityLivingBase).addItemStackToInventory(diffuser))
                    ((EntityPlayer) entityLivingBase).dropItem(diffuser, false);
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        stack.shrink(1);
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            ItemStack diffuser = new ItemStack(ModItems.DIFFUSER);
            if (!player.addItemStackToInventory(diffuser)) {
                player.dropItem(diffuser, false);
            }

        }
        if (!worldIn.isRemote)
            explode((WorldServer) worldIn, entityLiving.getPositionVector(), stack);
        return stack;
    }

    public EntityGasCloud explode(WorldServer worldIn, Vec3d pos, ItemStack stack) {
        EntityGasCloud cloud = super.explode(worldIn, pos, stack);
        cloud.setMaxLifespan(400);
        if (!worldIn.isRemote)
            worldIn.spawnEntity(new EntityItem(worldIn, pos.x, pos.y, pos.z, new ItemStack(ModItems.EMPTY_GRENADE)));
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
}
