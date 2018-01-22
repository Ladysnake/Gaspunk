package ladysnake.gaspunk.init;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.item.ItemGrenade;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@GameRegistry.ObjectHolder(GasPunk.MOD_ID)
@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
public final class ModItems {

    public static final Item GRENADE = Items.AIR;

    static Set<Item> allItems = new HashSet<>();

    @SuppressWarnings("unchecked")
    private static <T extends Item> T name(T item, String name) {
        return (T) item.setUnlocalizedName(GasPunk.MOD_ID + "." + name).setRegistryName(GasPunk.MOD_ID, name);
    }

    @SubscribeEvent
    public static void addItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> reg = event.getRegistry();
        Collections.addAll(allItems,
                name(new ItemBlock(ModBlocks.GAS_FACTORY), "air"),
                name(new ItemGrenade(), "grenade"));
        for (Item item : allItems) {
            item.setCreativeTab(GasPunk.CREATIVE_TAB);
            reg.register(item);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event) {
        allItems.forEach(ModItems::registerRender);
    }

    @SideOnly(Side.CLIENT)
    private static void registerRender(Item item) {
        registerRender(item, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()).toString()));
    }

    @SideOnly(Side.CLIENT)
    private static void registerRender(Item item, ModelResourceLocation loc) {
        ModelLoader.setCustomModelResourceLocation(item, 0, loc);
    }
}
