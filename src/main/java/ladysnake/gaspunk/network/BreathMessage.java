package ladysnake.gaspunk.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class BreathMessage implements IMessage {
    float breath;

    public BreathMessage() {
        super();
    }

    public BreathMessage(float breath) {
        this.breath = breath;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.breath = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(this.breath);
    }
}
