package ladysnake.gaspunk.gas.core;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ladysnake.gaspunk.GasPunk;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;

@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
public class GasDeserializer {

    private static final Gson GSON = new Gson();

    @SubscribeEvent
    public static void loadRecipes(RegistryEvent.Register<IRecipe> event) {
        ModContainer container = Loader.instance().activeModContainer();
        if (container != null) {
            CraftingHelper.findFiles(container, "assets/" + GasPunk.MOD_ID + "/gas_recipes", p -> true,
                    (root, file) -> {
                        String relative = root.relativize(file).toString();
                        if (!"json".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_"))
                            return true;

                        String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
                        ResourceLocation key = new ResourceLocation(GasPunk.MOD_ID, name);
                        try (BufferedReader reader = Files.newBufferedReader(file)) {
                            JsonObject json = JsonUtils.fromJson(GSON, reader, JsonObject.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }, true, true);

        }
    }

}
