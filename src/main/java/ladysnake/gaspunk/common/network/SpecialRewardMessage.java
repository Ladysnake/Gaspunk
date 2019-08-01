package ladysnake.gaspunk.common.network;

import io.netty.buffer.ByteBuf;
import ladysnake.gaspunk.api.customization.GrenadeSkins;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * This message is sent when a client connects to a server and after that when it changes the associated config
 */
public class SpecialRewardMessage implements IMessage {
    GrenadeSkins selectedSkin;

    @SuppressWarnings("unused")
    public SpecialRewardMessage() {
        super();
    }

    public SpecialRewardMessage(GrenadeSkins skin) {
        this.selectedSkin = skin;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int ordinal = buf.readInt();
        if (ordinal < GrenadeSkins.VALUES.length)
            this.selectedSkin = GrenadeSkins.VALUES[ordinal];
        else
            this.selectedSkin = GrenadeSkins.NONE;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(selectedSkin != null ? selectedSkin.ordinal() : 0);
    }
}
