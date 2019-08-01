package ladysnake.pathos.item;

import ladylib.registration.AutoRegister;
import ladysnake.pathos.Pathos;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Pathos.MOD_ID)
@Mod.EventBusSubscriber(modid = Pathos.MOD_ID)
@AutoRegister(Pathos.MOD_ID)
public class ModItems {

    public static final Item EMPTY_SYRINGE = new SyringeItem();
    public static final Item FILLED_SYRINGE = new FilledSyringeItem();

//    @Environment(EnvType.CLIENT)
//    @SubscribeEvent
//    public static void registerRenders(ModelRegistryEvent event) {
//        ModelLoader.setCustomModelResourceLocation(EMPTY_SYRINGE, 0, new ModelResourceLocation(new ResourceLocation("pathos:empty_syringe"), "inventory"));
//        ModelLoader.setCustomModelResourceLocation(FILLED_SYRINGE, 0, new ModelResourceLocation(new ResourceLocation("pathos:filled_syringe"), "inventory"));
//    }
//
//    @Environment(EnvType.CLIENT)
//    private static void registerRender(Item item) {
//        registerRender(item, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()).toString()));
//    }
//
//    @Environment(EnvType.CLIENT)
//    private static void registerRender(Item item, ModelResourceLocation loc) {
//        ModelLoader.setCustomModelResourceLocation(item, 0, loc);
//    }

}
