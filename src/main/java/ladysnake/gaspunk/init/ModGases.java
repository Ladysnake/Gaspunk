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
import net.minecraft.entity.passive.EntitySheep;
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


    public static final IGas VAPOR = new Gas(GasTypes.VAPOR, 0x00FFFFFF);
    public static final IGas HEALING_VAPOR = VAPOR;
    public static final IGas SMOKE = VAPOR;

    @SubscribeEvent
    public static void addRegistries(RegistryEvent.NewRegistry event) {
        REGISTRY = new RegistryBuilder<IGas>()
                .setType(IGas.class)
                .setName(new ResourceLocation(GasPunk.MOD_ID, "gases"))
                .setMaxID(255)
                .add((ILingeringGas::onRegistryAddGas))
                .create();
    }

    @SubscribeEvent
    public static void addGases(RegistryEvent.Register<IGas> event) {
        event.getRegistry().registerAll(
                VAPOR.setRegistryName(new ResourceLocation(GasPunk.MOD_ID, "air")),
                new Gas(GasTypes.SMOKE, 0xFFFFFFFF).setRegistryName("smoke"),
                new GasHealingVapor().setRegistryName("healing_vapor"),
                new GasToxic(GasTypes.SMOKE, 0xFF000000).setRegistryName("toxic_smoke"),
                new GasToxic(GasTypes.GAS, 0x00FFFFFF).setRegistryName("sarin_gas"),
                new GasTear(0xAAAACCCC).setRegistryName("tear_gas")
        );
        for (EnumDyeColor color : EnumDyeColor.values()) {
            // this is probably illegal in 53 states but I didn't want to parse the value back from the table
            event.getRegistry().register(new Gas(GasTypes.SMOKE,  0xFF000000 | (int) ReflectionHelper.getPrivateValue(EnumDyeColor.class, color, "colorValue", "field_193351_w")).setRegistryName("colored_smoke_" + color.getName()));
        }
    }

    @SubscribeEvent
    public static void addPotions(RegistryEvent.Register<Potion> event) {
        ILingeringGas.LINGERING_EFFECTS.values().forEach(event.getRegistry()::register);
    }

    public static void initRecipes() {
        addRecipe(VAPOR, new ItemStack(ModItems.SMOKE_POWDER), SMOKE);
        addRecipe(VAPOR, new ItemStack(Items.GHAST_TEAR), HEALING_VAPOR);
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
