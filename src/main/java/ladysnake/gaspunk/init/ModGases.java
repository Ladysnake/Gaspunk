package ladysnake.gaspunk.init;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.gas.Gas;
import ladysnake.gaspunk.gas.GasTear;
import ladysnake.gaspunk.gas.GasToxic;
import ladysnake.gaspunk.gas.core.GasHealingVapor;
import ladysnake.gaspunk.gas.core.GasTypes;
import ladysnake.gaspunk.gas.core.IGas;
import ladysnake.gaspunk.gas.core.ILingeringGas;
import ladysnake.gaspunk.item.ItemGasTube;
import ladysnake.sicklib.sickness.ISickness;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
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
     */
    public static final Gas AIR = new Gas(GasTypes.VAPOR, 0x99FFFFFF, 0xAA0033FF);
    public static final Gas HEALING_VAPOR = new GasHealingVapor();
    public static final Gas SARIN_GAS = new GasToxic(GasTypes.GAS, 0x00FFFFFF);
    public static final Gas SMOKE = new Gas(GasTypes.SMOKE, 0xFFFFFFFF);
    public static final Gas TEAR_GAS = new GasTear(0xAACCCCCC);
    public static final Gas TOXIC_SMOKE = new GasToxic(GasTypes.SMOKE, 0xFF000000);

    @SubscribeEvent
    public static void addRegistries(RegistryEvent.NewRegistry event) {
        REGISTRY = new RegistryBuilder<IGas>()
                .setType(IGas.class)
                .setName(new ResourceLocation(GasPunk.MOD_ID, "gases"))
                .setDefaultKey(new ResourceLocation("air"))
                .add((ILingeringGas::onRegistryAddGas))
                .create();
    }

    @SubscribeEvent
    public static void addGases(RegistryEvent.Register<IGas> event) {
        event.getRegistry().registerAll(
                AIR.setRegistryName("air"),
                SMOKE.setRegistryName("smoke"),
                HEALING_VAPOR.setRegistryName("healing_vapor"),
                TOXIC_SMOKE.setRegistryName("toxic_smoke"),
                SARIN_GAS.setRegistryName("sarin_gas"),
                TEAR_GAS.setRegistryName("tear_gas")
        );
        for (EnumDyeColor color : EnumDyeColor.values()) {
            // this is probably illegal in 53 states but I didn't want to parse the value back from the table
            event.getRegistry().register(new Gas(GasTypes.SMOKE,  0xFF000000 | (int) ReflectionHelper.getPrivateValue(EnumDyeColor.class, color, "colorValue", "field_193351_w")).setRegistryName("colored_smoke_" + color.getName()));
        }
    }

    @SubscribeEvent
    public static void addPotions(RegistryEvent.Register<ISickness> event) {
        ILingeringGas.LINGERING_EFFECTS.values().forEach(event.getRegistry()::register);
    }

    public static void initRecipes() {
        addRecipe(AIR, new ItemStack(ModItems.SMOKE_POWDER), SMOKE);
        addRecipe(AIR, new ItemStack(Items.GHAST_TEAR), HEALING_VAPOR);
        addRecipe(AIR, new ItemStack(ModItems.ASH), TOXIC_SMOKE);
        addRecipe(AIR, new ItemStack(Items.POISONOUS_POTATO), SARIN_GAS);
        addRecipe(SMOKE, new ItemStack(Items.FERMENTED_SPIDER_EYE), TEAR_GAS);
        for (EnumDyeColor color : EnumDyeColor.values()) {
            addRecipe(SMOKE, new ItemStack(Items.DYE, 1, color.getDyeDamage()), REGISTRY.getValue(new ResourceLocation(GasPunk.MOD_ID, "colored_smoke_" + color.getName())));
        }
    }

    public static void addRecipe(IGas prerequisite, ItemStack ingredient, IGas result) {
        BrewingRecipeRegistry.addRecipe(new GasBrewingRecipe(prerequisite, ingredient, result));
    }

    public static class GasBrewingRecipe extends BrewingRecipe {

        private final IGas base;

        public GasBrewingRecipe(IGas base, ItemStack reagent, IGas result) {
            super(((ItemGasTube) ModItems.GAS_TUBE).getItemStackFor(base), reagent, ((ItemGasTube) ModItems.GAS_TUBE).getItemStackFor(result));
            this.base = base;
        }

        @Nonnull
        @Override
        public ItemStack getOutput(@Nonnull ItemStack input, @Nonnull ItemStack ingredient) {
            if (isInput(input) && isIngredient(ingredient) && ItemGasTube.getContainedGas(input) == base)
                return getOutput().copy();
            return ItemStack.EMPTY;
        }
    }
}
