package ladysnake.gaspunk.common.crafting;

import com.google.gson.JsonObject;
import ladysnake.gaspunk.common.gas.core.GasRecipeDeserializer;
import ladysnake.gaspunk.common.gas.Gases;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.IngredientNBT;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;

public class GasIngredientFactory implements IIngredientFactory {
    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json) {
        ItemStack bottle = GasRecipeDeserializer.getBottle(Gases.GAS_REGISTRY.getValue(new Identifier(JsonUtils.getString(json, "gas"))));
        return new GasIngredientNBT(bottle);
    }

    // Just because the IngredientNBT constructor is protected
    public static class GasIngredientNBT extends IngredientNBT {
        public GasIngredientNBT(ItemStack stack) {
            super(stack);
        }
    }
}
