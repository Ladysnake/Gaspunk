package ladysnake.pathos.network;

import io.netty.buffer.ByteBuf;
import ladysnake.pathos.api.ISickness;
import ladysnake.pathos.api.SicknessEffect;
import ladysnake.pathos.capability.CapabilitySickness;
import ladysnake.pathos.sickness.Sickness;
import net.minecraft.client.MinecraftClient;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class SicknessMessage implements IMessage{
    private SicknessEffect effect;

    @SuppressWarnings("unused")
    public SicknessMessage() {
        super();
    }

    public SicknessMessage(SicknessEffect effect) {
        this.effect = effect;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        ISickness sickness = Sickness.REGISTRY.getValue(buf.readInt());
        float severity = buf.readFloat();
        int ticksSinceBeginning = buf.readInt();
        int ticksSinceLastPerform = buf.readInt();
        effect = new SicknessEffect(sickness, severity, ticksSinceBeginning, ticksSinceLastPerform);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        // sickness id
        buf.writeInt(Sickness.REGISTRY.getID(effect.getSickness().getRegistryName()));
        // effect properties
        buf.writeFloat(effect.getSeverity());
        buf.writeInt(effect.getTicksSinceBeginning());
        buf.writeInt(effect.getTicksSinceLastPerform());
    }

    public static class SicknessMessageHandler implements IMessageHandler<SicknessMessage, IMessage> {

        @Override
        @Environment(EnvType.CLIENT)
        public IMessage onMessage(SicknessMessage message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(
                    () -> CapabilitySickness.getHandler(MinecraftClient.getInstance().player).ifPresent(
                            // replace the existing effect instead of adding them
                            handler -> handler.addSickness(message.effect, (e1, e2) -> e2)
                    )
            );
            return null;
        }
    }
}
