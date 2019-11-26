package ladysnake.gaspunk.network;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.util.SpecialRewardChecker;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID, value = Side.CLIENT)
public class EventHandlerNetwork {

    private static boolean shouldSendSelectedSkin = false;

    @SubscribeEvent
    public static void onTickPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == Side.CLIENT && shouldSendSelectedSkin) {
            PacketHandler.NET.sendToServer(new SpecialRewardMessage(GasPunk.proxy.getSelectedSkin()));
            shouldSendSelectedSkin = false;
        }
    }

    @SubscribeEvent
    public static void onFMLNetworkClientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        // if you're not special, don't waste the server's time
        shouldSendSelectedSkin = SpecialRewardChecker.isSpecialPerson(Minecraft.getMinecraft().getSession().getProfile().getId());
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(GasPunk.MOD_ID)) {
            // send a new packet
            shouldSendSelectedSkin = SpecialRewardChecker.isSpecialPerson(Minecraft.getMinecraft().getSession().getProfile().getId());
        }
    }
}
