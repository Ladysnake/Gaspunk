package ladysnake.gaspunk.init;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.gas.Gas;
import ladysnake.gaspunk.gas.GasTear;
import ladysnake.gaspunk.gas.core.GasHealingVapor;
import ladysnake.gaspunk.gas.core.GasTypes;
import ladysnake.gaspunk.gas.GasToxic;
import ladysnake.gaspunk.item.ItemGasTube;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.AbstractBrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
@GameRegistry.ObjectHolder(GasPunk.MOD_ID)
public class ModGases {

    public static final Gas VAPOR = new Gas(GasTypes.VAPOR, 0x00FFFFFF);
    public static final Gas HEALING_VAPOR = VAPOR;
    public static final Gas SMOKE = VAPOR;

    public static IForgeRegistry<Gas> REGISTRY;

    @SubscribeEvent
    public static void addRegistries(RegistryEvent.NewRegistry event) {
        REGISTRY = new RegistryBuilder<Gas>()
                .setType(Gas.class)
                .setName(new ResourceLocation(GasPunk.MOD_ID, "gases"))
                .setMaxID(255)
                .create();
    }

    @SubscribeEvent
    public static void addGases(RegistryEvent.Register<Gas> event) {
        event.getRegistry().registerAll(
                VAPOR.setRegistryName("air"),
                new Gas(GasTypes.SMOKE, 0xFFFFFFFF).setRegistryName("smoke"),
                new GasHealingVapor().setRegistryName("healing_vapor"),
                new GasToxic(GasTypes.GAS, 0x00FFFFFF).setRegistryName("sarin_gas"),
                new GasTear(0xAA0033FF).setRegistryName("tear_gas")
        );
    }

    public static void initRecipes() {
      addRecipe(VAPOR, new ItemStack(ModItems.SMOKE_POWDER), SMOKE);
      addRecipe(VAPOR, new ItemStack(Items.GHAST_TEAR), HEALING_VAPOR);
    }

    public static void addRecipe(Gas prerequisite, ItemStack ingredient, Gas result) {
      BrewingRecipeRegistry.addRecipe(new GasBrewingRecipe(prerequisite, ingredient, result));
    }

    public static class GasBrewingRecipe extends BrewingRecipe {

        private final Gas base;

        public GasBrewingRecipe(Gas base, ItemStack reagent, Gas result) {
            super(((ItemGasTube)ModItems.GAS_TUBE).getItemStackFor(base), reagent, ((ItemGasTube)ModItems.GAS_TUBE).getItemStackFor(result));
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
