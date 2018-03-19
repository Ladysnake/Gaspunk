package ladysnake.gaspunk.crafting;

import com.google.gson.JsonObject;
import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.api.customization.IHasSkin;
import ladysnake.gaspunk.item.ItemGasTube;
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
                    if (item instanceof IHasSkin) {
                        diffuser = stack;
                    }
                    // both the tube and the diffuser have been found
                    if (tube != null && diffuser != null)
                        break;
                }
            }
            if (tube != null && output.getItem() instanceof ItemGasTube) {
                ItemStack craftedStack = ((ItemGasTube) output.getItem()).getItemStackFor(ItemGasTube.getContainedGas(tube));
                if (diffuser != null && craftedStack.getItem() instanceof IHasSkin)
                    ((IHasSkin) craftedStack.getItem()).setSkin(craftedStack, ((IHasSkin)diffuser.getItem()).getSkin(diffuser));
                return craftedStack;
            }

            return output.copy();
        }
    }

}
