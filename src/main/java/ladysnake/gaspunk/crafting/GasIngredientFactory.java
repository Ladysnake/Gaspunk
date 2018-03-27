package ladysnake.gaspunk.crafting;

import com.google.gson.JsonObject;
import ladysnake.gaspunk.gas.core.GasRecipeDeserializer;
import ladysnake.gaspunk.init.ModGases;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.IngredientNBT;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;

public class GasIngredientFactory implements IIngredientFactory {
    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json) {
        ItemStack bottle = GasRecipeDeserializer.getBottle(ModGases.REGISTRY.getValue(new ResourceLocation(JsonUtils.getString(json, "gas"))));
        return new GasIngredientNBT(bottle);
    }

    // Just because the IngredientNBT constructor is protected
    public static class GasIngredientNBT extends IngredientNBT {
        public GasIngredientNBT(ItemStack stack) {
            super(stack);
        }
    }
}
