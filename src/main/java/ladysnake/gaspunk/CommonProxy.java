package ladysnake.gaspunk;

import ladysnake.gaspunk.gas.CapabilityBreathing;
import ladysnake.gaspunk.network.PacketHandler;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

public class CommonProxy {

    public void preInit() {
        CapabilityBreathing.register();
    }

    public void init() {
        PacketHandler.initPackets();
    }

    public void postInit() {

    }

    public void makeSmoke(World world, double x, double y, double z, int color, int amount) {

    }

}
