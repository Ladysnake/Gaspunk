package ladysnake.sicklib.item;

import ladysnake.sicklib.Pathos;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


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

}
