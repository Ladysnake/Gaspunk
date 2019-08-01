package ladysnake.gaspunk.common.gas;

import com.google.common.collect.ImmutableList;
import ladysnake.gaspunk.api.IGas;
import ladysnake.gaspunk.api.basetype.GasParticleTypes;
import ladysnake.gaspunk.api.basetype.GasTypes;
import ladysnake.gaspunk.common.GasPunk;
import ladysnake.gaspunk.common.gas.Gas;
import ladysnake.gaspunk.common.gas.GasAgents;
import ladysnake.gaspunk.common.gas.SuspendableGas;
import ladysnake.gaspunk.common.gas.agent.CandyflossAgent;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;

import java.util.function.BiConsumer;

public final class Gases {

    public static final DefaultedRegistry<IGas> GAS_REGISTRY = new DefaultedRegistry<>("gaspunk:air");

    /**
     * The default gas, does nothing. Is actually water vapor in game
     */
    public static final Gas AIR = new Gas(GasTypes.GAS, GasParticleTypes.GAS, 0x99FFFFFF, 0xAA0033FF, ImmutableList.of(), new String[0]);

    // TODO add chemical burning property
    public static final Gas MUSTARD_GAS = AIR;

    public static void init() {
        Registry.register(Registry.REGISTRIES, GasPunk.id("gases"), GAS_REGISTRY);
        addGases();
    }

    public static void addGases() {
        Registry.register(GAS_REGISTRY, GasPunk.id("sarin_gas"), new SuspendableGas(GasTypes.GAS, 0x00FFFFFF, GasAgents.getAgent(GasPunk.id("nerve")), 0.8F));
        Registry.register(GAS_REGISTRY, GasPunk.id("air"), AIR);
        for (DyeColor color : DyeColor.values()) {
            Registry.register(GAS_REGISTRY, GasPunk.id("colored_smoke_" + color.getName()), new Gas(GasTypes.SMOKE,  0xFF000000 | color.getFireworkColor()));
        }
    }

    public static <T> void visitRegistry(Registry<T> registry, BiConsumer<Identifier, T> visitor) {
        registry.getIds().forEach(id -> visitor.accept(id, registry.get(id)));
        RegistryEntryAddedCallback.event(registry).register((index, identifier, entry) -> visitor.accept(identifier, entry));
    }
}
