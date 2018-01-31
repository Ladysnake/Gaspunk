package ladysnake.gaspunk.network;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ShaderMessageHandler implements IMessageHandler<ShaderMessage, IMessage> {
    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(ShaderMessage message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            if (message.shaderLoc.isEmpty())
                Minecraft.getMinecraft().entityRenderer.stopUseShader();
            else
                Minecraft.getMinecraft().entityRenderer.loadShader(new ResourceLocation(message.shaderLoc));
        });
        return null;
    }
}
