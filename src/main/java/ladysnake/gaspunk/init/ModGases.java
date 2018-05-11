package ladysnake.gaspunk.init;

import com.google.common.collect.ImmutableList;
import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.api.IGas;
import ladysnake.gaspunk.api.basetype.GasParticleTypes;
import ladysnake.gaspunk.api.basetype.GasTypes;
import ladysnake.gaspunk.api.event.AgentRegistryEvent;
import ladysnake.gaspunk.gas.Gas;
import ladysnake.gaspunk.gas.GasAgents;
import ladysnake.gaspunk.gas.SuspendableGas;
import ladysnake.gaspunk.gas.agent.CandyflossAgent;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
@GameRegistry.ObjectHolder(GasPunk.MOD_ID)
public class ModGases {

    public static IForgeRegistry<IGas> REGISTRY;


    /**
     * The default gas, does nothing. Is actually water vapor in game
     * Is initialized to null to replace null checks
     */
    public static final Gas AIR = null;

    /* The gases below are generated in json files */
    public static final Gas SMOKE = AIR;
    public static final Gas TEAR_GAS = AIR;
    public static final Gas CHOKE_SMOKE = AIR;
    // TODO add chemical burning property
    public static final Gas MUSTARD_GAS = AIR;

    @SubscribeEvent
    public static void addRegistries(RegistryEvent.NewRegistry event) {
        REGISTRY = new RegistryBuilder<IGas>()
                .setType(IGas.class)
                .setName(new ResourceLocation(GasPunk.MOD_ID, "gases"))
                .setDefaultKey(new ResourceLocation("air"))
                .create();
    }

    @SubscribeEvent
    public static void addAgents(AgentRegistryEvent event) {
        event.register("candyfloss", new CandyflossAgent());
    }

    @SubscribeEvent
    public static void addGases(RegistryEvent.Register<PotionType> event) {
        REGISTRY.register(new SuspendableGas(GasTypes.GAS, 0x00FFFFFF, GasAgents.getAgent(new ResourceLocation(GasPunk.MOD_ID, "nerve")), 0.8F).setRegistryName("sarin_gas"));
        REGISTRY.register(new Gas(GasTypes.GAS, GasParticleTypes.GAS, 0x99FFFFFF, 0xAA0033FF, ImmutableList.of(), new String[0]).setRegistryName("air"));
        for (EnumDyeColor color : EnumDyeColor.values()) {
            // this is probably illegal in 53 states but I didn't want to parse the value back from the table
            REGISTRY.register(new Gas(GasTypes.SMOKE,  0xFF000000 | (int) ReflectionHelper.getPrivateValue(EnumDyeColor.class, color, "colorValue", "field_193351_w")).setRegistryName("colored_smoke_" + color.getName()));
        }
    }

    @SubscribeEvent
    public static void onRegistryMissingMappings(RegistryEvent.MissingMappings<IGas> event) {
        for (RegistryEvent.MissingMappings.Mapping<IGas> mapping : event.getMappings()) {
            if (mapping.key.equals(new ResourceLocation("gaspunk:toxic_smoke")))
                mapping.remap(CHOKE_SMOKE);
        }
    }
}
