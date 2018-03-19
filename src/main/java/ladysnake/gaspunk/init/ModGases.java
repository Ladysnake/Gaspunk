package ladysnake.gaspunk.init;

import com.google.common.collect.ImmutableList;
import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.api.IGas;
import ladysnake.gaspunk.gas.Gas;
import ladysnake.gaspunk.gas.GasAgents;
import ladysnake.gaspunk.gas.SuspendableGas;
import ladysnake.gaspunk.gas.core.GasFactories;
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
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
@GameRegistry.ObjectHolder(GasPunk.MOD_ID)
public class ModGases {

    public static IForgeRegistry<IGas> REGISTRY;


    /**
     * The default gas, does nothing. Is actually water vapor in game
     * Is initialized to null to replace null checks
     */
    public static final Gas AIR = null;
    public static final Gas HEALING_VAPOR = GasFactories.createGasPotion(MobEffects.REGENERATION, 219, 1);
    public static final Gas SARIN_GAS = new SuspendableGas(GasTypes.GAS, 0x00FFFFFF, GasAgents.NERVE, 0.8F);
    public static final Gas SMOKE = new Gas(GasTypes.SMOKE, 0xFFFFFFFF);
    public static final Gas TEAR_GAS = new Gas(GasTypes.SMOKE, 0xAACCCCCC, GasAgents.LACHRYMATOR, 0.2F);
    // 0.1 potency for the damage agent means 1 heart per hit
    public static final Gas TOXIC_SMOKE = new Gas(GasTypes.SMOKE, 0xFF000000, GasAgents.PULMONARY, 0.1F);
    public static final Gas MUSTARD_GAS = new Gas(GasTypes.GAS, 0xFFB4A000, GasAgents.NERVE, 0.2F);

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
        event.getRegistry().registerAll(
                new Gas(GasTypes.GAS, 0x99FFFFFF, 0xAA0033FF, ImmutableList.of()).setRegistryName("air"),
                SMOKE.setRegistryName("smoke"),
                HEALING_VAPOR.setRegistryName("healing_vapor"),
                TOXIC_SMOKE.setRegistryName("choke_smoke"),
                SARIN_GAS.setRegistryName("sarin_gas"),
                TEAR_GAS.setRegistryName("tear_gas"),
                MUSTARD_GAS.setRegistryName("mustard_gas")
        );
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
        addRecipe(AIR, new ItemStack(ModItems.SMOKE_POWDER), SMOKE);
        addRecipe(AIR, new ItemStack(Items.GHAST_TEAR), HEALING_VAPOR);
        addRecipe(AIR, new ItemStack(Items.POISONOUS_POTATO), SARIN_GAS);
        addRecipe(SMOKE, new ItemStack(ModItems.ASH), TOXIC_SMOKE);
        addRecipe(SMOKE, new ItemStack(Items.FERMENTED_SPIDER_EYE), TEAR_GAS);
        addRecipe(AIR, new ItemStack(ModItems.SULFUR), MUSTARD_GAS);
        for (EnumDyeColor color : EnumDyeColor.values()) {
            addRecipe(SMOKE, new ItemStack(Items.DYE, 1, color.getDyeDamage()), REGISTRY.getValue(new ResourceLocation(GasPunk.MOD_ID, "colored_smoke_" + color.getName())));
        }
    }

    public static void addRecipe(IGas prerequisite, ItemStack ingredient, IGas result) {
        ItemStack bottle;
        if (prerequisite == AIR)
            bottle = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER);
        else
            bottle = ((ItemGasTube) ModItems.GAS_TUBE).getItemStackFor(prerequisite);
        BrewingRecipeRegistry.addRecipe(new GasBrewingRecipe(bottle, ingredient, result));
    }

    public static class GasBrewingRecipe extends BrewingRecipe {

        public GasBrewingRecipe(ItemStack base, ItemStack reagent, IGas result) {
            super(base, reagent, ((ItemGasTube) ModItems.GAS_TUBE).getItemStackFor(result));
        }

        @Nonnull
        @Override
        public ItemStack getOutput(@Nonnull ItemStack input, @Nonnull ItemStack ingredient) {
            if (isInput(input) && isIngredient(ingredient) && ItemGasTube.getContainedGas(input) == ItemGasTube.getContainedGas(getInput()))
                return getOutput().copy();
            return ItemStack.EMPTY;
        }
    }
}
