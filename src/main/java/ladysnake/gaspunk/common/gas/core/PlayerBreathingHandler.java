package ladysnake.gaspunk.common.gas.core;

import ladysnake.gaspunk.common.network.BreathMessage;
import ladysnake.gaspunk.common.network.PacketHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerBreathingHandler extends CapabilityBreathing.DefaultBreathingHandler {
    private final ServerPlayerEntity owner;

    public PlayerBreathingHandler(ServerPlayerEntity owner) {
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
