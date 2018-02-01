package ladysnake.gaspunk.gas.core;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.network.BreathMessage;
import ladysnake.gaspunk.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public class PlayerBreathingHandler extends CapabilityBreathing.DefaultBreathingHandler {
    private final EntityPlayerMP owner;

    public PlayerBreathingHandler(EntityPlayerMP owner) {
        super(owner);
        this.owner = owner;
    }

    @Override
    public void setAirSupply(float airSupply) {
        super.setAirSupply(airSupply);
        try {
            if (!owner.world.isRemote)
                PacketHandler.NET.sendTo(new BreathMessage(airSupply), owner);
        } catch (NullPointerException e) {
            GasPunk.LOGGER.trace("Hey there's this exception I should take care of one day and you have the right to insult me", e);
        }
    }
}
