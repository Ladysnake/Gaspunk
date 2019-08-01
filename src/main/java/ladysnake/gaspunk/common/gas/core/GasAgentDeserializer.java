package ladysnake.gaspunk.common.gas.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import ladysnake.gaspunk.common.GasPunk;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class GasAgentDeserializer {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final PathMatcher JSON_FILES = FileSystems.getDefault().getPathMatcher("glob:*.json");

    // need to register those before gases
    public static void loadGasAgents() {
        FabricLoader.getInstance().getAllMods().forEach(GasAgentDeserializer::loadGasAgents);
        File configFolder = new File(FabricLoader.getInstance().getConfigDirectory(), GasPunk.MOD_ID + "/custom_agents");
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
        Path rootPath = container.getPath("gamedata/" + container.getMetadata().getId() + "/gaspunk_agents");
        try (Stream<Path> paths = Files.walk(rootPath)) {
            paths.parallel().filter(JSON_FILES::matches).forEach(p -> loadGasAgents(rootPath, p));
        } catch (IOException e) {
            GasPunk.LOGGER.error("Failed to read gaspunk agents from {}", container.getMetadata().getId());
        }
    }

    private static void loadGasAgents(Path root, Path file) {
        String relative = root.relativize(file).toString();
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
    }

}
