package ladysnake.gaspunk.gas.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.api.IGas;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
public class GasAgentDeserializer {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // need to register those before gases
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void loadGasAgents(RegistryEvent.Register<IGas> event) {
        ModContainer gaspunkContainer = Loader.instance().activeModContainer();
        Loader.instance().getActiveModList().forEach(GasAgentDeserializer::loadGasAgents);
        Loader.instance().setActiveModContainer(gaspunkContainer);
        File configFolder = new File(GasPunk.lib.getConfigFolder(), GasPunk.MOD_ID + "/custom_agents");
        // if the config folder was just created or couldn't be created, no need to search it
        try {
            if (!configFolder.mkdirs() && configFolder.exists()) {
                Files.walk(configFolder.toPath()).forEach(path -> loadGasAgents(configFolder.toPath(), path));
            } else if (configFolder.exists()) {
                JsonObject exampleAgent = new JsonObject();
                exampleAgent.addProperty("factory", "potion");
                exampleAgent.addProperty("duration", 30);
                exampleAgent.addProperty("amplifier", 2);
                exampleAgent.addProperty("potion", "minecraft:wither");
                Files.write(configFolder.toPath().resolve("_example.json"), GSON.toJson(exampleAgent).getBytes(), StandardOpenOption.CREATE_NEW);
            }
        } catch (IOException e) {
            GasPunk.LOGGER.error("Error while loading gases from config", e);
        }
    }

    private static void loadGasAgents(ModContainer container) {
        Loader.instance().setActiveModContainer(container);
        CraftingHelper.findFiles(container, "assets/" + container.getModId() + "/gaspunk_agents", p -> true,
                GasAgentDeserializer::loadGasAgents, true, true);
    }

    private static boolean loadGasAgents(Path root, Path file) {
        String relative = root.relativize(file).toString();
        if (!"json".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_"))
            return true;

        String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            GasAgentBuilder builder = new GasAgentBuilder(name);
            JsonReader in = new JsonReader(reader);
            in.beginObject();
            while (in.hasNext()) {
                builder.set(in.nextName(), in.nextString());
            }
            in.endObject();
            builder.make();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}
