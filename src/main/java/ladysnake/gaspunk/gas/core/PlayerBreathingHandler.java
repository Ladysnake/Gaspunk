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
        // check that the player is actually connected before sending an update packet
        if (owner.connection != null)
            PacketHandler.NET.sendTo(new BreathMessage(airSupply), owner);
    }
}
