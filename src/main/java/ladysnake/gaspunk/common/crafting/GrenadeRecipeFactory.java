package ladysnake.gaspunk.common.crafting;

import com.google.gson.JsonObject;
import ladysnake.gaspunk.common.GasPunk;
import ladysnake.gaspunk.api.customization.Skinnable;
import ladysnake.gaspunk.common.item.ItemGasTube;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class GrenadeRecipeFactory implements IRecipeFactory {

    @Override
    public Recipe parse(JsonContext context, JsonObject json) {
        ShapelessOreRecipe recipe = ShapelessOreRecipe.factory(context, json);

        return new GasContainerRecipe(new Identifier(GasPunk.MOD_ID, "copy_nbt_crafting"), recipe.getIngredients(), recipe.getRecipeOutput());
    }

    public static class GasContainerRecipe extends ShapelessOreRecipe {
        public GasContainerRecipe(Identifier group, DefaultedList<Ingredient> input, @Nonnull ItemStack result) {
            super(group, input, result);
        }

        @Override
        @Nonnull
        public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
            // the item stack that determines the gas contained in the resulting item
            ItemStack tube = null;
            // the item stack that determines the skin of the resulting item
            ItemStack diffuser = null;
            for (int i = 0; i < var1.getSizeInventory(); ++i) {
                ItemStack stack = var1.getStackInSlot(i);

                if (!stack.isEmpty()) {     // most slots are empty, no need to do other checks if that's the case
                    Item item = stack.getItem();
                    if (item instanceof ItemGasTube) {
                        tube = stack;
                    }
                    if (item instanceof Skinnable) {
                        diffuser = stack;
                    }
                    // both the tube and the diffuser have been found
                    if (tube != null && diffuser != null)
                        break;
                }
            }
            ItemStack craftedStack;
            // transfer the gas
            if (tube != null && output.getItem() instanceof ItemGasTube)
                craftedStack = ((ItemGasTube) output.getItem()).getItemStackFor(ItemGasTube.getContainedGas(tube));
            else
                craftedStack = output.copy();
            // transfer the skin
            if (diffuser != null && craftedStack.getItem() instanceof Skinnable)
                ((Skinnable) craftedStack.getItem()).setSkin(craftedStack, ((Skinnable)diffuser.getItem()).getSkin(diffuser));
            return craftedStack;
        }
    }

}
