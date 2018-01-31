package ladysnake.gaspunk.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ShaderMessage implements IMessage {
    String shaderLoc;

    @SuppressWarnings("unused")
    public ShaderMessage() {
        super();
    }

    public ShaderMessage(String shaderLoc) {
        this.shaderLoc = shaderLoc;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        shaderLoc = new PacketBuffer(buf).readString(64);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        new PacketBuffer(buf).writeString(shaderLoc == null ? "" : shaderLoc);
    }
}
