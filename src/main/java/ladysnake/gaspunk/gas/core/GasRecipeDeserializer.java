package ladysnake.gaspunk.gas.core;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.api.IGas;
import ladysnake.gaspunk.init.ModGases;
import ladysnake.gaspunk.init.ModItems;
import ladysnake.gaspunk.item.ItemGasTube;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingOreRecipe;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
public class GasRecipeDeserializer {
    private static final Gson GSON = new Gson();

    @SubscribeEvent
    public static void loadRecipes(RegistryEvent.Register<IRecipe> event) {
        ModContainer gaspunkContainer = Loader.instance().activeModContainer();
        Loader.instance().getActiveModList().forEach(GasRecipeDeserializer::loadRecipes);
        Loader.instance().setActiveModContainer(gaspunkContainer);
        File configFolder = new File(GasPunk.lib.getConfigFolder(), GasPunk.MOD_ID + "/custom_recipes");
        // if the config folder was just created or couldn't be created, no need to search it
        if (!configFolder.mkdirs() && configFolder.exists()) {
            JsonContext context = new JsonContext(GasPunk.MOD_ID);
            try {
                Files.walk(configFolder.toPath()).forEach(path -> loadRecipes(configFolder.toPath(), path, context));
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        ResourceLocation key = new ResourceLocation(context.getModId(), name);

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            JsonObject json = JsonUtils.fromJson(GSON, reader, JsonObject.class);
            deserializeRecipe(json, context);
        } catch (JsonParseException e) {
            FMLLog.log.error("Parsing error loading recipe {}", key, e);
            return false;
        } catch (IOException e) {
            FMLLog.log.error("Couldn't read recipe {} from {}", key, file, e);
            return false;
        }
        return true;
    }

    private static void deserializeRecipe(JsonObject json, JsonContext context) {
        IGas result = ModGases.REGISTRY.getValue(new ResourceLocation(JsonUtils.getString(json, "result")));
        JsonObject input = JsonUtils.getJsonObject(json, "input");
        ItemStack in;
        if (input.has("gas"))
            in = ModGases.getBottle(ModGases.REGISTRY.getValue(new ResourceLocation(JsonUtils.getString(input, "gas"))));
        else
            in = CraftingHelper.getItemStack(input, context);
        JsonObject ingredient = JsonUtils.getJsonObject(json, "ingredient");
        String type = JsonUtils.getString(ingredient, "type", "minecraft:item");
        if ("forge:ore_dict".equals(type)) {
            String ingr = JsonUtils.getString(ingredient, "ore");
            BrewingRecipeRegistry.addRecipe(new BrewingOreRecipe(in, ingr, ((ItemGasTube) ModItems.GAS_TUBE).getItemStackFor(result)));
        } else if ("minecraft:item".equals(type)) {
            ItemStack ingr = CraftingHelper.getItemStack(json, context);
            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(in, ingr, ((ItemGasTube) ModItems.GAS_TUBE).getItemStackFor(result)));
        }
    }
}
