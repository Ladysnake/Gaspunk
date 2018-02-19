package ladysnake.gaspunk.crafting;

import com.google.gson.JsonObject;
import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.item.ItemDiffuser;
import ladysnake.gaspunk.item.ItemGasTube;
import ladysnake.gaspunk.item.ItemGrenade;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;
import java.util.Objects;

@SuppressWarnings("unused")
public class GrenadeRecipeFactory implements IRecipeFactory {

    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
        ShapelessOreRecipe recipe = ShapelessOreRecipe.factory(context, json);

        return new GasContainerRecipe(new ResourceLocation(GasPunk.MOD_ID, "copy_nbt_crafting"), recipe.getIngredients(), recipe.getRecipeOutput());
    }

    public static class GasContainerRecipe extends ShapelessOreRecipe {
        public GasContainerRecipe(ResourceLocation group, NonNullList<Ingredient> input, @Nonnull ItemStack result) {
            super(group, input, result);
        }

        @Override
        @Nonnull
        public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
            ItemStack tube = null;
            ItemStack diffuser = null;
            for (int i = 0; i < var1.getSizeInventory(); ++i) {
                ItemStack stack = var1.getStackInSlot(i);

                if (!stack.isEmpty()) {
                    Item item = stack.getItem();
                    if (item instanceof ItemGasTube) {
                        tube = stack;
                    }
                    // If it's already a grenade, it's a refill recipe
                    if (item instanceof ItemDiffuser || item instanceof ItemGrenade) {
                        diffuser = stack;
                    }
                    // both the tube and the diffuser have been found
                    if (tube != null && diffuser != null)
                        break;
                }
            }
            if (tube != null && output.getItem() instanceof ItemGasTube) {
                ItemStack craftedStack = ((ItemGasTube) output.getItem()).getItemStackFor(ItemGasTube.getContainedGas(tube));
                if (diffuser != null && diffuser.getTagCompound() != null) {
                    Objects.requireNonNull(craftedStack.getTagCompound()).setFloat(ItemDiffuser.TAG_CUSTOM_SKIN, diffuser.getTagCompound().getFloat(ItemDiffuser.TAG_CUSTOM_SKIN));
                }
                return craftedStack;
            }

            return output.copy();
        }
    }

}
