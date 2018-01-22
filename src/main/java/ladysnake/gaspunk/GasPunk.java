package ladysnake.gaspunk;

import ladysnake.gaspunk.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = GasPunk.MOD_ID,
        name = GasPunk.MOD_NAME,
        version = GasPunk.VERSION
)
public class GasPunk {

    public static final String MOD_ID = "gaspunk";
    public static final String MOD_NAME = "GasPunk";
    public static final String VERSION = "1.0-SNAPSHOT";

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static GasPunk INSTANCE;

    public static Logger LOGGER;

    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(MOD_ID) {
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem() {
            return new ItemStack(ModItems.GRENADE);
        }
    };

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
