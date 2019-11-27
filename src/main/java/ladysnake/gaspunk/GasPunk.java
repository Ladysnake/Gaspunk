package ladysnake.gaspunk;

import ladylib.LLibContainer;
import ladylib.LadyLib;
import ladysnake.gaspunk.api.customization.IHasSkin;
import ladysnake.gaspunk.compat.ThaumcraftCompat;
import ladysnake.gaspunk.gas.Gas;
import ladysnake.gaspunk.init.ModItems;
import ladysnake.gaspunk.util.SpecialRewardChecker;
import ladysnake.pathos.Pathos;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod(
        modid = GasPunk.MOD_ID,
        name = GasPunk.MOD_NAME,
        version = GasPunk.VERSION,
        dependencies = GasPunk.DEPENDENCIES,
        guiFactory = "ladysnake.gaspunk.client.config.GasPunkConfigFactory",
        certificateFingerprint = "@FINGERPRINT@"
)
public class GasPunk {

    public static final String MOD_ID = "gaspunk";
    public static final String MOD_NAME = "GasPunk";
    // Automagically replaced by gradle during the build
    public static final String VERSION = "@VERSION@";

    public static final String DEPENDENCIES =
            "required-after:forge@[14.23.3.2669,);required-after:ladylib;after:jei;after:baubles;after:thaumcraft";

    @SidedProxy(
            modId = GasPunk.MOD_ID,
            serverSide = "ladysnake.gaspunk.CommonProxy",
            clientSide = "ladysnake.gaspunk.client.ClientProxy"
    )
    public static CommonProxy proxy;

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @LadyLib.LLInstance(GasPunk.MOD_ID)
    private static LLibContainer gaspunkLib;
    @LadyLib.LLInstance(Pathos.MOD_ID)
    private static LLibContainer pathosLib;

    public static final CreativeTabs CREATIVE_TAB = new GasPunkTabs();

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     * @param event the event signaling pre-initialization
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        gaspunkLib.setCreativeTab(CREATIVE_TAB);
        pathosLib.setCreativeTab(CREATIVE_TAB);
        proxy.preInit(event);
        Gas.classInit();
    }

    /**
     * This is the second initialization event. Register custom recipes
     * @param event the event signaling initialization
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
//        Pathos.init(event);
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     * @param event the event signaling post-initialization
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
        if (Loader.isModLoaded("thaumcraft"))
            ThaumcraftCompat.registerAspects();
    }

    @Mod.EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
        if (LadyLib.isDevEnv())
            LOGGER.info("Ignoring invalid fingerprint as we are in a development environment");
        else
            LOGGER.info("The fingerpring is invalid and will not be officially supported by Ladysnake. Please contact @sschr15 for possible fixes...");
            //LOGGER.warn("Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
    }

    public static class GasPunkTabs extends CreativeTabs {
        public GasPunkTabs() {
            super(MOD_ID);
        }

        @Nonnull
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(ModItems.GRENADE);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> itemList) {
            super.displayAllRelevantItems(itemList);
            // set the skin of every item that supports it to the one currently selected
            itemList.stream()
                    .filter(stack -> stack.getItem() instanceof IHasSkin)
                    .forEach(stack -> ((IHasSkin)stack.getItem()).setSkin(stack, SpecialRewardChecker.getSelectedSkin(Minecraft.getMinecraft().player)));
        }
    }
}
