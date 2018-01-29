package ladysnake.gaspunk.init;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.gas.Gas;
import ladysnake.gaspunk.gas.GasHealingVapor;
import ladysnake.gaspunk.gas.GasTypes;
import ladysnake.gaspunk.gas.ToxicGas;
import ladysnake.gaspunk.item.ItemGasTube;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingOreRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
@GameRegistry.ObjectHolder(GasPunk.MOD_ID)
public class ModGases {

    public static final Gas VAPOR = new Gas(GasTypes.VAPOR, 0x00FFFFFF);
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
                new ToxicGas(GasTypes.GAS, 0x00FFFFFF).setRegistryName("sarin_gas")
        );
    }

    public static void initRecipes() {
        BrewingRecipeRegistry.addRecipe(
                ((ItemGasTube)ModItems.GAS_TUBE).getItemStackFor(VAPOR),
                new ItemStack(ModItems.SMOKE_POWDER),
                ((ItemGasTube)ModItems.GAS_TUBE).getItemStackFor(SMOKE));
    }
}
