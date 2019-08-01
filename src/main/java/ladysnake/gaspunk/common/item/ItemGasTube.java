package ladysnake.gaspunk.common.item;

import ladysnake.gaspunk.common.GasPunk;
import ladysnake.gaspunk.api.IGas;
import ladysnake.gaspunk.common.gas.Gases;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemGasTube extends Item {

    public static final String NBT_CONTAINED_GAS = GasPunk.MOD_ID + ":contained_gas";

    public ItemGasTube() {
        super();
        this.setMaxStackSize(1);
        this.addPropertyOverride(new Identifier(GasPunk.MOD_ID, "gas_type"),
                ((stack, worldIn, entityIn) -> getContainedGas(stack).getType().getId()));
    }

    public static IGas getContainedGas(ItemStack stack) {
        IGas ret = null;
        if (stack.hasTagCompound()) {
            ret = Gases.GAS_REGISTRY.getValue(new Identifier(Objects.requireNonNull(stack.getTagCompound()).getString(NBT_CONTAINED_GAS)));
        }
        return ret == null ? Gases.AIR : ret;
    }

    public ItemStack getItemStackFor(IGas gas) {
        ItemStack stack = new ItemStack(this);
        CompoundTag nbt = new CompoundTag();
        nbt.putString(NBT_CONTAINED_GAS, Objects.requireNonNull(gas.getRegistryName(), "Can't use an unregistered gas in grenade").toString());
        stack.setTagCompound(nbt);
        return stack;
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        // use the deprecated I18n as the method can be called on both sides
        return net.minecraft.util.text.translation.I18n.translateToLocalFormatted(
                getTranslationKey() + ".name",
                net.minecraft.util.text.translation.I18n.translateToLocalFormatted(getContainedGas(stack).getUnlocalizedName())
        );
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, TooltipContext flagIn) {
        getContainedGas(stack).addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Nonnull
    @Override
    public TypedActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
        return new TypedActionResult<>(ActionResult.SUCCESS, this.turnTubeIntoItem(playerIn.getStackInHand(handIn), playerIn, new ItemStack(Items.GLASS_BOTTLE)));
    }

    protected ItemStack turnTubeIntoItem(ItemStack emptyTubes, PlayerEntity player, ItemStack stack) {
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

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull DefaultedList<ItemStack> items) {
        if (tab == GasPunk.CREATIVE_TAB) {
            for (IGas gas : Gases.GAS_REGISTRY.getValues()) {
                items.add(getItemStackFor(gas));
            }
        }
    }
}
