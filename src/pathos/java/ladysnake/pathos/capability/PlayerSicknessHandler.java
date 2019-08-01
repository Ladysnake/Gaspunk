package ladysnake.pathos.capability;

import ladysnake.pathos.api.SicknessEffect;
import ladysnake.pathos.network.PacketHandler;
import ladysnake.pathos.network.SicknessMessage;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.BiFunction;

public class PlayerSicknessHandler extends CapabilitySickness.DefaultSicknessHandler {
    private final ServerPlayerEntity owner;

    public PlayerSicknessHandler(ServerPlayerEntity owner) {
        super(owner);
        this.owner = owner;
    }

    @Override
    public void addSickness(SicknessEffect effect, BiFunction<SicknessEffect, SicknessEffect, SicknessEffect> mergeFunction) {
        super.addSickness(effect, mergeFunction);
        if (owner.networkHandler != null && effect.getSickness().isSynchronized())
            PacketHandler.NET.sendTo(new SicknessMessage(getActiveEffect(effect.getSickness())), owner);
    }

    @Override
    protected void updateEffect(SicknessEffect effect) {
        super.updateEffect(effect);
        if (owner.networkHandler != null && effect.getTicksSinceBeginning() % 600 == 0 && effect.getSickness().isSynchronized())
            PacketHandler.NET.sendTo(new SicknessMessage(effect), owner);
    }
}
