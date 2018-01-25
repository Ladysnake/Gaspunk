package ladysnake.gaspunk.client;

import ladysnake.gaspunk.CommonProxy;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

    @Override
    public void makeSmoke(World world, double x, double y, double z, int color, int amount) {
        if (!world.isRemote) return;
        int a = (color & 0xFF) / 0xFF;
        int b = (color >> 8 & 0xFF) / 0xFF;
        int g = (color >> 16 & 0xFF) / 0xFF;
        int r = (color >> 24 & 0xFF) / 0xFF;

    }

}
