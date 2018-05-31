package ladysnake.gaspunk.gas.core;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.api.AbstractGas;
import ladysnake.gaspunk.api.IGas;
import ladysnake.gaspunk.api.IGasAgent;
import ladysnake.gaspunk.api.basetype.GasParticleTypes;
import ladysnake.gaspunk.api.basetype.GasTypes;
import ladysnake.gaspunk.gas.Gas;
import ladysnake.gaspunk.gas.GasAgents;
import ladysnake.gaspunk.init.ModGases;
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
import java.nio.file.StandardOpenOption;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
public class GasDeserializer extends TypeAdapter<AbstractGas> {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @SubscribeEvent
    public static void loadGases(RegistryEvent.Register<IGas> event) {
        ModContainer gaspunkContainer = Loader.instance().activeModContainer();
        Loader.instance().getActiveModList().forEach(GasDeserializer::loadGases);
        Loader.instance().setActiveModContainer(gaspunkContainer);
        File configFolder = new File(GasPunk.lib.getConfigFolder(), GasPunk.MOD_ID + "/custom_gases");
        // if the config folder was just created or couldn't be created, no need to search it
        try {
            if (!configFolder.mkdirs() && configFolder.exists()) {
                Files.walk(configFolder.toPath()).forEach(path -> loadGases(configFolder.toPath(), path));
            } else if (configFolder.exists()) {
                // generate an example gas file
                IGas exampleGas = new Gas.Builder()
                        .addAgent(GasAgents.getAgent(new ResourceLocation(GasPunk.MOD_ID, "lachrymator")), 0.5f)
                        .addAgent(GasAgents.getAgent(new ResourceLocation(GasPunk.MOD_ID, "nerve")), 0.375f)
                        .setColor(0x55CAFE66)
                        .setBottleColor(0x75DADD10)
                        .setType(GasTypes.GAS)
                        .addTooltipLine("For more examples, check the technical wiki:")
                        .addTooltipLine("https://github.com/Ladysnake/Gaspunk/wiki")
                        .build();
                Files.write(configFolder.toPath().resolve("_example.json"), GSON.toJson(exampleGas).getBytes(), StandardOpenOption.CREATE_NEW);
            }
        } catch (IOException e) {
            GasPunk.LOGGER.error("Error while loading gases from config", e);
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
    public void write(JsonWriter out, AbstractGas value) throws IOException {
        out.beginObject();
        if (value.getType() instanceof GasTypes) {
            GasTypes type = (GasTypes) value.getType();
            out.name("gasType");
            out.value(type.name());
        } else throw new RuntimeException("Cannot serialize unknown type " + value.getType());
        if (value.getParticleType() instanceof GasParticleTypes) {
            GasParticleTypes type = (GasParticleTypes) value.getParticleType();
            out.name("particleType");
            out.value(type.name());
        } else throw new RuntimeException("Cannot serialize unknown type " + value.getParticleType());
        out.name("color");
        out.value(Long.toString(value.getColor(), 16));
        out.name("bottleColor");
        out.value(Long.toString(value.getBottleColor(), 16));
        out.name("agents");
        out.beginArray();
        for (Gas.AgentEffect effect : value.getAgents()) {
            out.beginObject();
            out.name("agent");
            out.value(GasAgents.getId(effect.getAgent()).toString());
            out.name("potency");
            out.value(effect.getPotency());
            out.endObject();
        }
        out.endArray();
        String[] tooltipLines = value.getTooltipLines();
        if (tooltipLines.length > 0) {
            out.name("tooltipLines");
            out.beginArray();
            for (String s : value.getTooltipLines())
                out.value(s);
            out.endArray();
        }
        out.endObject();
    }

    @Override
    public AbstractGas read(JsonReader in) throws IOException {
        AbstractGas.Builder builder = AbstractGas.builder();
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
                    break;
                case "agents":
                    parseAgents(in, builder);
                    break;
                case "tooltipLines":
                    parseTooltip(in, builder);
                    break;
            }
        }
        in.endObject();
        return builder.build();
    }

    private void parseTooltip(JsonReader in, AbstractGas.Builder builder) throws IOException {
        in.beginArray();
        while (in.hasNext())
            builder.addTooltipLine(in.nextString());
        in.endArray();
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

    private void parseAgents(JsonReader in, AbstractGas.Builder builder) throws IOException {
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
