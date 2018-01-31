package ladysnake.gaspunk.item;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.entity.EntityGasCloud;
import ladysnake.gaspunk.gas.core.IGas;
import ladysnake.gaspunk.init.ModGases;
import ladysnake.gaspunk.init.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemGasTube extends Item {

    public static final String NBT_CONTAINED_GAS = GasPunk.MOD_ID + ":contained_gas";

    public ItemGasTube() {
        super();
        this.setMaxStackSize(1);
        this.addPropertyOverride(new ResourceLocation(GasPunk.MOD_ID, "gas_type"),
                ((stack, worldIn, entityIn) -> getContainedGas(stack).getType().getId()));
    }

    public static IGas getContainedGas(ItemStack stack) {
        IGas ret = null;
        if (stack.hasTagCompound()) {
            ret = ModGases.REGISTRY.getValue(new ResourceLocation(Objects.requireNonNull(stack.getTagCompound()).getString(NBT_CONTAINED_GAS)));
        }
        return ret == null ? ModGases.VAPOR : ret;
    }

    public ItemStack getItemStackFor(IGas gas) {
        ItemStack stack = new ItemStack(this);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString(NBT_CONTAINED_GAS, Objects.requireNonNull(gas.getRegistryName(), "Can't use an unregistered gas in grenade").toString());
        stack.setTagCompound(nbt);
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(Objects.requireNonNull(getContainedGas(stack).getRegistryName()).toString());
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        return new ActionResult<>(EnumActionResult.SUCCESS, this.turnBottleIntoItem(playerIn.getHeldItem(handIn), playerIn, new ItemStack(ModItems.GLASS_TUBE)));
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

    /*@Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        if (!worldIn.isRemote) {
            EntityGasTube tube = new EntityGasTube(worldIn, playerIn, stack);
            tube.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntity(tube);
            stack.shrink(1);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }*/

    public EntityGasCloud explode(WorldServer worldIn, Vec3d pos, ItemStack stack) {
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.x, pos.y, pos.z, 20, 0.5, 0.5, 0.5, 0.2);
        EntityGasCloud cloud = new EntityGasCloud(worldIn, getContainedGas(stack));
        cloud.setPosition(pos.x, pos.y, pos.z);
        cloud.setMaxLifespan(100);
        worldIn.spawnEntity(cloud);
        return cloud;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (tab == GasPunk.CREATIVE_TAB) {
            //TODO use IForgeRegistry#getValuesCollection when ModOff is over
            for (IGas gas : ModGases.REGISTRY.getValues()) {
                items.add(getItemStackFor(gas));
            }
        }
    }
}
