package ladysnake.gaspunk.common.gas.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import ladysnake.gaspunk.common.GasPunk;
import ladysnake.gaspunk.api.IGas;
import ladysnake.gaspunk.common.gas.Gases;
import ladysnake.gaspunk.common.item.GasPunkItems;
import ladysnake.gaspunk.common.item.ItemGasTube;
import net.minecraft.item.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.brewing.BrewingOreRecipe;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.FilenameUtils;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
public class GasRecipeDeserializer {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @SubscribeEvent
    public static void loadRecipes(RegistryEvent.Register<Recipe> event) {
        ModContainer gaspunkContainer = Loader.instance().activeModContainer();
        Loader.instance().getActiveModList().forEach(GasRecipeDeserializer::loadRecipes);
        Loader.instance().setActiveModContainer(gaspunkContainer);
        File configFolder = new File(Loader.instance().getConfigDir(), GasPunk.MOD_ID + "/custom_recipes");
        // if the config folder was just created or couldn't be created, no need to search it
        try {
            if (!configFolder.mkdirs() && configFolder.exists()) {
                JsonContext context = new JsonContext(GasPunk.MOD_ID);
                try {
                    Files.walk(configFolder.toPath()).forEach(path -> loadRecipes(configFolder.toPath(), path, context));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (configFolder.exists()) {
                JsonObject recipe = new JsonObject();
                recipe.addProperty("result", "gaspunk:colored_smoke_red");
                JsonObject input = new JsonObject();
                input.addProperty("item", "minecraft:water_bucket");
                recipe.add("input", input);
                JsonObject ingredient = new JsonObject();
                ingredient.addProperty("type", "forge:ore_dict");
                ingredient.addProperty("ore", "dustRedstone");
                recipe.add("ingredient", ingredient);
                recipe.addProperty("result", "gaspunk:colored_smoke_red");
                Files.write(configFolder.toPath().resolve("_example.json"), GSON.toJson(recipe).getBytes(), StandardOpenOption.CREATE_NEW);
            }
        } catch (IOException e) {
            GasPunk.LOGGER.error("Error while loading gas recipes from config", e);
        }
    }

    private static void loadRecipes(ModContainer container) {
        Loader.instance().setActiveModContainer(container);
        JsonContext context = new JsonContext(container.getModId());
        CraftingHelper.findFiles(container, "assets/" + container.getModId() + "/gaspunk_recipes", p -> true,
                (root, file) -> loadRecipes(root, file, context), true, true);
    }

    private static boolean loadRecipes(Path root, Path file, JsonContext context) {
        String relative = root.relativize(file).toString();
        if (!"json".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_"))
            return true;
        String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
        Identifier key = new Identifier(context.getModId(), name);

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            JsonObject json = JsonUtils.fromJson(GSON, reader, JsonObject.class);
            if (json == null || json.has("conditions") && !CraftingHelper.processConditions(JsonUtils.getJsonArray(json, "conditions"), context))
                return true;
            deserializeRecipe(json, context);
        } catch (JsonParseException e) {
            GasPunk.LOGGER.error("Parsing error loading recipe {}", key, e);
            return false;
        } catch (IOException e) {
            GasPunk.LOGGER.error("Couldn't read recipe {} from {}", key, file, e);
            return false;
        }
        return true;
    }

    private static void deserializeRecipe(JsonObject json, JsonContext context) {
        String resultName = JsonUtils.getString(json, "result");
        IGas result = Gases.GAS_REGISTRY.getValue(new Identifier(resultName));
        if (result == null)
            throw new JsonParseException("Unrecognized gas: " + resultName);
        JsonObject jsInput = JsonUtils.getJsonObject(json, "input");
        ItemStack in;
        if (jsInput.has("gas"))
            in = getBottle(Gases.GAS_REGISTRY.getValue(new Identifier(JsonUtils.getString(jsInput, "gas"))));
        else
            in = CraftingHelper.getItemStack(jsInput, context);
        JsonObject jsIngredient = JsonUtils.getJsonObject(json, "ingredient");
        String type = JsonUtils.getString(jsIngredient, "type", "minecraft:item");
        if ("forge:ore_dict".equals(type)) {
            String ingredient = JsonUtils.getString(jsIngredient, "ore");
            BrewingRecipeRegistry.addRecipe(new GasBrewingOreRecipe(in, ingredient, ((ItemGasTube) GasPunkItems.GAS_TUBE).getItemStackFor(result)));
        } else if ("minecraft:item".equals(type)) {
            ItemStack ingredient = CraftingHelper.getItemStack(jsIngredient, context);
            BrewingRecipeRegistry.addRecipe(new GasBrewingRecipe(in, ingredient, ((ItemGasTube) GasPunkItems.GAS_TUBE).getItemStackFor(result)));
        }
    }

    public static ItemStack getBottle(IGas prerequisite) {
        if (prerequisite == Gases.AIR)
            return PotionUtil.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER);
        else
            return ((ItemGasTube) GasPunkItems.GAS_TUBE).getItemStackFor(prerequisite);
    }

    private static boolean isGasInput(@Nonnull ItemStack stack1, ItemStack stack2) {
        return ItemGasTube.getContainedGas(stack1) == ItemGasTube.getContainedGas(stack2);
    }

    public static class GasBrewingRecipe extends BrewingRecipe {
        public GasBrewingRecipe(@Nonnull ItemStack input, @Nonnull ItemStack ingredient, @Nonnull ItemStack output) {
            super(input, ingredient, output);
        }

        @Override
        public boolean isInput(@Nonnull ItemStack stack) {
            return super.isInput(stack) && isGasInput(getInput(), stack);
        }
    }

    public static class GasBrewingOreRecipe extends BrewingOreRecipe {
        public GasBrewingOreRecipe(@Nonnull ItemStack input, @Nonnull String ingredient, @Nonnull ItemStack output) {
            super(input, ingredient, output);
        }

        @Override
        public boolean isInput(@Nonnull ItemStack stack) {
            return super.isInput(stack) && isGasInput(getInput(), stack);
        }
    }
}
