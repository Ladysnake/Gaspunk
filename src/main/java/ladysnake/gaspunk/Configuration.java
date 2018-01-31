package ladysnake.gaspunk;

import net.minecraftforge.common.config.Config;

@Config(modid = GasPunk.MOD_ID)
public class Configuration {

    @Config.Comment("Disables gas clouds checking for a clear path to entities and make them only check straight distance instead")
    public static boolean fastGas = false;

    @Config.Comment("Makes ash require smelting nether wart instead of rotten flesh")
    public static boolean alternativeAshRecipe = false;

    public static Client client = new Client();

    public static class Client {
        public boolean useShaders = true;
    }
}
