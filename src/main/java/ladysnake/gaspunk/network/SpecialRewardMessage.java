package ladysnake.gaspunk.network;

import io.netty.buffer.ByteBuf;
import ladysnake.gaspunk.util.SpecialRewardChecker;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * This message is sent when a client connects to a server and after that when it changes the associated config
 */
public class SpecialRewardMessage implements IMessage {
    SpecialRewardChecker.GrenadeSkins selectedSkin;

    @SuppressWarnings("unused")
    public SpecialRewardMessage() {
        super();
    }

    public SpecialRewardMessage(SpecialRewardChecker.GrenadeSkins skin) {
        this.selectedSkin = skin;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int ordinal = buf.readInt();
        if (ordinal < SpecialRewardChecker.GrenadeSkins.VALUES.length)
            this.selectedSkin = SpecialRewardChecker.GrenadeSkins.VALUES[ordinal];
        else
            this.selectedSkin = SpecialRewardChecker.GrenadeSkins.NONE;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(selectedSkin != null ? selectedSkin.ordinal() : 0);
    }
}
