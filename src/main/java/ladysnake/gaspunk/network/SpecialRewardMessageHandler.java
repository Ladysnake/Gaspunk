package ladysnake.gaspunk.network;

import ladysnake.gaspunk.util.SpecialRewardChecker;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpecialRewardMessageHandler implements IMessageHandler<SpecialRewardMessage, IMessage> {

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(final SpecialRewardMessage message, final MessageContext ctx) {
        // setSelectedGrenadeSkin is already thread-safe, no need to schedule the task
        SpecialRewardChecker.setSelectedGrenadeSkin(ctx.getServerHandler(), message.selectedSkin);
        return null;
    }
}
