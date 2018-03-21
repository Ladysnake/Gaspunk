package ladysnake.gaspunk.item;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.api.IGas;
import ladysnake.gaspunk.init.ModGases;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
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
        return ret == null ? ModGases.AIR : ret;
    }

    public ItemStack getItemStackFor(IGas gas) {
        ItemStack stack = new ItemStack(this);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString(NBT_CONTAINED_GAS, Objects.requireNonNull(gas.getRegistryName(), "Can't use an unregistered gas in grenade").toString());
        stack.setTagCompound(nbt);
        return stack;
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        // use the deprecated I18n as the method can be called on both sides
        return net.minecraft.util.text.translation.I18n.translateToLocalFormatted(
                getUnlocalizedName() + ".name",
                net.minecraft.util.text.translation.I18n.translateToLocalFormatted(getContainedGas(stack).getUnlocalizedName())
        );
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        getContainedGas(stack).addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        return new ActionResult<>(EnumActionResult.SUCCESS, this.turnTubeIntoItem(playerIn.getHeldItem(handIn), playerIn, new ItemStack(Items.GLASS_BOTTLE)));
    }

    protected ItemStack turnTubeIntoItem(ItemStack emptyTubes, EntityPlayer player, ItemStack stack) {
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

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (tab == GasPunk.CREATIVE_TAB) {
            for (IGas gas : ModGases.REGISTRY.getValues()) {
                items.add(getItemStackFor(gas));
            }
        }
    }
}
