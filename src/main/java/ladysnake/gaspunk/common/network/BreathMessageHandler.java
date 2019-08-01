package ladysnake.gaspunk.common.network;

import ladysnake.gaspunk.common.gas.core.CapabilityBreathing;
import net.minecraft.client.MinecraftClient;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class BreathMessageHandler implements IMessageHandler<BreathMessage, IMessage> {

    @Override
    @Environment(EnvType.CLIENT)
    public IMessage onMessage(BreathMessage message, MessageContext ctx) {
        MinecraftClient.getInstance().addScheduledTask(() -> CapabilityBreathing.getHandler(MinecraftClient.getInstance().player).ifPresent(handler -> handler.setAirSupply(message.breath)));
        return null;
    }
}
