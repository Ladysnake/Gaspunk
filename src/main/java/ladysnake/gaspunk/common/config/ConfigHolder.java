package ladysnake.gaspunk.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import ladysnake.gaspunk.common.GasPunk;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.UnaryOperator;

final class ConfigHolder {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Executor CONFIG_LOADER = Executors.newSingleThreadExecutor((r) -> new Thread(r, "Gaspunk Config Loader"));

    static GasPunkConfig config = new GasPunkCommonConfig();

    static <C extends GasPunkConfig> void loadConfig(Class<C> clazz, UnaryOperator<C> postProcessor) {
        CONFIG_LOADER.execute(() -> {
            try {
                // First, we load the config into a JsonObject, to preserve every property
                File configFile = new File(FabricLoader.getInstance().getConfigDirectory(), "gaspunk.json");
                JsonObject json;
                if (!configFile.exists()) {
                    Files.createFile(configFile.toPath());
                    json = new JsonObject();
                } else {
                    try (Reader reader = new FileReader(configFile)) {
                        json = GSON.fromJson(reader, JsonObject.class);
                    }
                }
                // Then, we load the json into the POJO and apply post processing
                config = postProcessor.apply(GSON.fromJson(json, clazz));
                // Finally, if the generated json has different properties, we save it
                JsonObject savedJson = new JsonObject();
                savedJson.entrySet().addAll(json.entrySet());   // preserve other properties that are not in the POJO
                savedJson.entrySet().addAll(((JsonObject) GSON.toJsonTree(config)).entrySet());
                if (!json.equals(savedJson)) {
                    try (Writer writer = new FileWriter(configFile)) {
                        GSON.toJson(json, writer);
                    } catch (IOException e) {
                        GasPunk.LOGGER.error("Failed to save gaspunk config", e);
                    }
                }
            } catch (IOException e) {
                GasPunk.LOGGER.error("Failed to load gaspunk config", e);
            }
        });
    }
}
