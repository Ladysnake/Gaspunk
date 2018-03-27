package ladysnake.gaspunk.init;

import com.google.common.collect.ImmutableList;
import ladylib.registration.AutoRegister;
import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.api.IGas;
import ladysnake.gaspunk.crafting.GasIngredientFactory;
import ladysnake.gaspunk.crafting.GrenadeRecipeFactory;
import ladysnake.gaspunk.gas.Gas;
import ladysnake.gaspunk.gas.GasAgents;
import ladysnake.gaspunk.gas.SuspendableGas;
import ladysnake.gaspunk.gas.core.GasFactories;
import ladysnake.gaspunk.gas.core.GasParticleTypes;
import ladysnake.gaspunk.gas.core.GasTypes;
import ladysnake.gaspunk.item.ItemGasTube;
import ladysnake.gaspunk.item.ItemGrenade;
import ladysnake.pathos.api.ISickness;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
@GameRegistry.ObjectHolder(GasPunk.MOD_ID)
public class ModGases {

    public static IForgeRegistry<IGas> REGISTRY;


    /**
     * The default gas, does nothing. Is actually water vapor in game
     * Is initialized to null to replace null checks
     */
    public static final Gas AIR = null;
    @AutoRegister(GasPunk.MOD_ID)
    public static final Gas HEALING_VAPOR = GasFactories.createGasPotion(MobEffects.REGENERATION, 230, 1);
    @AutoRegister(GasPunk.MOD_ID)
    public static final Gas SARIN_GAS = new SuspendableGas(GasTypes.GAS, 0x00FFFFFF, GasAgents.NERVE, 0.8F);

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
    public static void addGases(RegistryEvent.Register<IGas> event) {
        event.getRegistry().register(new Gas(GasTypes.GAS, GasParticleTypes.GAS, 0x99FFFFFF, 0xAA0033FF, ImmutableList.of()).setRegistryName("air"));
        for (EnumDyeColor color : EnumDyeColor.values()) {
            // this is probably illegal in 53 states but I didn't want to parse the value back from the table
            event.getRegistry().register(new Gas(GasTypes.SMOKE,  0xFF000000 | (int) ReflectionHelper.getPrivateValue(EnumDyeColor.class, color, "colorValue", "field_193351_w")).setRegistryName("colored_smoke_" + color.getName()));
        }
    }

    @SubscribeEvent
    public static void addRecipes(RegistryEvent.Register<IRecipe> event) {
        ResourceLocation group = new ResourceLocation(GasPunk.MOD_ID, "grenades");
        for (IGas gas : REGISTRY.getValues()) {
            event.getRegistry().register(new GrenadeRecipeFactory.GasContainerRecipe(
                    group,
                    NonNullList.from(Ingredient.EMPTY, Ingredient.fromItem(ModItems.DIFFUSER), new GasIngredientFactory.GasIngredientNBT(((ItemGasTube)ModItems.GAS_TUBE).getItemStackFor(gas))),
                    ((ItemGrenade)ModItems.GRENADE).getItemStackFor(gas)
            ).setRegistryName(Objects.requireNonNull(gas.getRegistryName()).getResourceDomain() + "_grenade"));
        }
    }

    @SubscribeEvent
    public static void addPotions(RegistryEvent.Register<ISickness> event) {
        GasAgents.LINGERING_EFFECTS.values().forEach(event.getRegistry()::register);
    }

    @SubscribeEvent
    public static void onRegistryMissingMappings(RegistryEvent.MissingMappings<IGas> event) {
        for (RegistryEvent.MissingMappings.Mapping<IGas> mapping : event.getMappings()) {
            if (mapping.key.equals(new ResourceLocation("gaspunk:toxic_smoke")))
                mapping.remap(CHOKE_SMOKE);
        }
    }
}
