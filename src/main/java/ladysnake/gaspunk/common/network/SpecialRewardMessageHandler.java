package ladysnake.gaspunk.common.network;

import ladysnake.gaspunk.common.util.SpecialRewardChecker;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class SpecialRewardMessageHandler implements IMessageHandler<SpecialRewardMessage, IMessage> {

    @Override
    @Environment(EnvType.CLIENT)
    public IMessage onMessage(final SpecialRewardMessage message, final MessageContext ctx) {
        // setSelectedGrenadeSkin is already thread-safe, no need to schedule the task
        SpecialRewardChecker.setSelectedGrenadeSkin(ctx.getServerHandler(), message.selectedSkin);
        return null;
    }
}
