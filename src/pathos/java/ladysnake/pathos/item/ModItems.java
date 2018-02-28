package ladysnake.pathos.item;

import ladysnake.pathos.Pathos;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

@GameRegistry.ObjectHolder(Pathos.MOD_ID)
@Mod.EventBusSubscriber(modid = Pathos.MOD_ID)
public class ModItems {

    public static final Item EMPTY_SYRINGE = Items.AIR;
    public static final Item FILLED_SYRINGE = Items.AIR;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new ItemSyringe().setRegistryName("empty_syringe").setUnlocalizedName("pathos.syringe"),
                new ItemFilledSyringe().setRegistryName("filled_syringe").setUnlocalizedName("pathos.filled_syringe")
        );
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(EMPTY_SYRINGE, 0, new ModelResourceLocation(new ResourceLocation("pathos:empty_syringe"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(FILLED_SYRINGE, 0, new ModelResourceLocation(new ResourceLocation("pathos:filled_syringe"), "inventory"));
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
