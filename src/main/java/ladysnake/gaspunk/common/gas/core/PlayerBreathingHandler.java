package ladysnake.gaspunk.common.gas.core;

import nerdhub.cardinal.components.api.util.sync.EntitySyncedComponent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;

public class PlayerBreathingHandler extends DefaultBreathingHandler implements EntitySyncedComponent {
    private final ServerPlayerEntity owner;

    public PlayerBreathingHandler(ServerPlayerEntity owner) {
        super(owner);
        this.owner = owner;
    }

    @Override
    public void setAirSupply(float airSupply) {
        super.setAirSupply(airSupply);
        this.markDirty();
    }

    @Override
    public ServerPlayerEntity getEntity() {
        return this.owner;
    }

    @Override
    public void syncWith(ServerPlayerEntity player) {
        if (player == this.owner) {
            EntitySyncedComponent.super.syncWith(player);
        }
    }

    @Override
    public void writeToPacket(PacketByteBuf buf) {
        buf.writeFloat(this.getAirSupply());
    }

    @Override
    public void readFromPacket(PacketByteBuf buf) {
        this.setAirSupply(buf.readFloat());
    }

}
