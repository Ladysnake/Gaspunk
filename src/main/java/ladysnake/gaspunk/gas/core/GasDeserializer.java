package ladysnake.gaspunk.gas.core;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.api.IGas;
import ladysnake.gaspunk.api.IGasAgent;
import ladysnake.gaspunk.gas.Gas;
import ladysnake.gaspunk.gas.GasAgents;
import ladysnake.gaspunk.init.ModGases;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
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
import java.util.Objects;

@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
public class GasDeserializer extends TypeAdapter<Gas> {

    private static final Gson GSON = new Gson();

    @SubscribeEvent
    public static void loadGases(RegistryEvent.Register<IGas> event) {
        ModContainer gaspunkContainer = Loader.instance().activeModContainer();
        Loader.instance().getActiveModList().forEach(GasDeserializer::loadGases);
        Loader.instance().setActiveModContainer(gaspunkContainer);
        File configFolder = new File(GasPunk.lib.getConfigFolder(), GasPunk.MOD_ID + "/custom_gases");
        // if the config folder was just created or couldn't be created, no need to search it
        if (!configFolder.mkdirs() && configFolder.exists()) {
            try {
                Files.walk(configFolder.toPath()).forEach(path -> loadGases(configFolder.toPath(), path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadGases(ModContainer container) {
        Loader.instance().setActiveModContainer(container);
        CraftingHelper.findFiles(container, "assets/" + container.getModId() + "/gaspunk_gases", p -> true,
                GasDeserializer::loadGases, true, true);
    }

    private static boolean loadGases(Path root, Path file) {
        String relative = root.relativize(file).toString();
        if (!"json".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_"))
            return true;

        String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            Gas gas = GSON.fromJson(reader, Gas.class);
            ModGases.REGISTRY.register(gas.setRegistryName(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void write(JsonWriter out, Gas value) throws IOException {
        out.beginObject();
        if (value.getType() instanceof GasTypes) {
            GasTypes type = (GasTypes) value.getType();
            out.name("gasType");
            out.value(type.name());
        } else throw new JsonException("Cannot serialize unknown type " + value.getType());
        if (value.getParticleType() instanceof GasParticleTypes) {
            GasParticleTypes type = (GasParticleTypes) value.getParticleType();
            out.name("particleType");
            out.value(type.name());
        } else throw new JsonException("Cannot serialize unknown type " + value.getParticleType());
        out.name("color");
        out.value(value.getColor());
        out.name("bottleColor");
        out.value(value.getBottleColor());
        out.name("agents");
        out.beginArray();
        for (Gas.AgentEffect effect: value.getAgents()) {
            out.beginObject();
            out.name("agent");
            out.value(GasAgents.getId(effect.getAgent()).toString());
            out.name("potency");
            out.value(effect.getPotency());
            out.endObject();
        }
        out.endArray();
        out.endObject();
    }

    @Override
    public Gas read(JsonReader in) throws IOException {
        Gas.Builder builder = new Gas.Builder();
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "gasType":
                    builder.setType(GasTypes.valueOf(in.nextString()));
                    break;
                case "particleType":
                    builder.setParticleType(GasParticleTypes.valueOf(in.nextString()));
                    break;
                case "color":
                    builder.setColor(parseInt(in, "color"));
                    break;
                case "bottleColor":
                    builder.setBottleColor(parseInt(in, "bottleColor"));
                case "agents":
                    parseAgents(in, builder);
            }
        }
        in.endObject();
        return builder.build();
    }

    private int parseInt(JsonReader in, String memberName) throws IOException {
        String value = in.nextString();
        try {
            // decode as a long because ARGB color values tend to overflow
            return Long.decode(value).intValue();
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException("Expected " + memberName + " to be a valid Int representation, was " + value, e);
        }
    }

    private void parseAgents(JsonReader in, Gas.Builder builder) throws IOException {
        in.beginArray();
        while (in.hasNext()) {
            IGasAgent agent = null;
            double potency = 0;
            in.beginObject();
            while (in.hasNext()) {
                String name = in.nextName();
                switch (name) {
                    case "agent":
                        String id = in.nextString();
                        agent = GasAgents.getAgent(new ResourceLocation(id));
                        if (agent == null)
                            throw new JsonParseException("Invalid agent provided: " + id + " " + in.getPath());
                        break;
                    case "potency":
                        potency = in.nextDouble();
                        break;
                }
            }
            in.endObject();
            builder.addAgent(Objects.requireNonNull(agent, "no agent type provided"), (float) potency);
        }
        in.endArray();
    }
}
