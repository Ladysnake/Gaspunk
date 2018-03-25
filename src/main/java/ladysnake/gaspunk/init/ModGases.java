package ladysnake.gaspunk.init;

import com.google.common.collect.ImmutableList;
import ladylib.registration.AutoRegister;
import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.api.IGas;
import ladysnake.gaspunk.gas.Gas;
import ladysnake.gaspunk.gas.GasAgents;
import ladysnake.gaspunk.gas.SuspendableGas;
import ladysnake.gaspunk.gas.core.GasFactories;
import ladysnake.gaspunk.gas.core.GasParticleTypes;
import ladysnake.gaspunk.gas.core.GasTypes;
import ladysnake.gaspunk.item.ItemGasTube;
import ladysnake.pathos.api.ISickness;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingOreRecipe;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
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
    public static void addPotions(RegistryEvent.Register<ISickness> event) {
        GasAgents.LINGERING_EFFECTS.values().forEach(event.getRegistry()::register);
    }

    public static void initRecipes() {
        addRecipe(AIR, new ItemStack(Items.POISONOUS_POTATO), SARIN_GAS);
        addRecipe(SMOKE, new ItemStack(ModItems.ASH), CHOKE_SMOKE);
        addRecipe(SMOKE, new ItemStack(Items.FERMENTED_SPIDER_EYE), TEAR_GAS);
//        addOreRecipe(AIR, "dustSulfur", MUSTARD_GAS);
        for (EnumDyeColor color : EnumDyeColor.values()) {
            addRecipe(SMOKE, new ItemStack(Items.DYE, 1, color.getDyeDamage()), REGISTRY.getValue(new ResourceLocation(GasPunk.MOD_ID, "colored_smoke_" + color.getName())));
        }
    }

    public static void addRecipe(IGas prerequisite, ItemStack ingredient, IGas result) {
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(getBottle(prerequisite), ingredient, ((ItemGasTube) ModItems.GAS_TUBE).getItemStackFor(result)));
    }

    public static void addOreRecipe(IGas prerequisite, String ingredient, IGas result) {
        BrewingRecipeRegistry.addRecipe(new BrewingOreRecipe(getBottle(prerequisite), ingredient, ((ItemGasTube) ModItems.GAS_TUBE).getItemStackFor(result)));
    }

    public static ItemStack getBottle(IGas prerequisite) {
        if (prerequisite == AIR)
            return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER);
        else
            return ((ItemGasTube) ModItems.GAS_TUBE).getItemStackFor(prerequisite);
    }

    @SubscribeEvent
    public static void onRegistryMissingMappings(RegistryEvent.MissingMappings<IGas> event) {
        for (RegistryEvent.MissingMappings.Mapping<IGas> mapping : event.getMappings()) {
            if (mapping.key.equals(new ResourceLocation("gaspunk:toxic_smoke")))
                mapping.remap(CHOKE_SMOKE);
        }
    }
}
