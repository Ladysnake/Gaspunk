package ladysnake.gaspunk.crafting;

import com.google.gson.JsonObject;
import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.item.ItemGasTube;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;

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

            for (int i = 0; i < var1.getSizeInventory(); ++i) {
                ItemStack stack = var1.getStackInSlot(i);

                if (!stack.isEmpty()) {
                    if (stack.getItem() instanceof ItemGasTube) {
                        if (output.getItem() instanceof ItemGasTube) {
                            return ((ItemGasTube) output.getItem()).getItemStackFor(ItemGasTube.getContainedGas(stack));
                        }
                    }
                }
            }

            return output.copy();
        }
    }

}
