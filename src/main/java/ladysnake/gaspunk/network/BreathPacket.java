package ladysnake.gaspunk.network;

import ladysnake.gaspunk.gas.CapabilityBreathing;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BreathPacket implements IMessageHandler<BreathMessage, IMessage> {

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(BreathMessage message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> CapabilityBreathing.getHandler(Minecraft.getMinecraft().player).ifPresent(handler -> handler.setAirSupply(message.breath)));
        return null;
    }
}
