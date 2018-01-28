package ladysnake.gaspunk.gas;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
