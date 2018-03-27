package ladysnake.gaspunk.compat;

import ladysnake.gaspunk.crafting.GrenadeRecipeFactory;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class GrenadeRecipeWrapper implements ICraftingRecipeWrapper {
    private final IJeiHelpers jeiHelpers;
    protected final GrenadeRecipeFactory.GasContainerRecipe recipe;

    public GrenadeRecipeWrapper(IJeiHelpers jeiHelpers, GrenadeRecipeFactory.GasContainerRecipe recipe) {
        this.jeiHelpers = jeiHelpers;
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ItemStack recipeOutput = recipe.getRecipeOutput();
        IStackHelper stackHelper = jeiHelpers.getStackHelper();

        List<List<ItemStack>> inputLists = stackHelper.expandRecipeItemStackInputs(recipe.getIngredients());
        ingredients.setInputLists(ItemStack.class, inputLists);
        ingredients.setOutput(ItemStack.class, recipeOutput);
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return recipe.getRegistryName();
    }
}
