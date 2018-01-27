package ladysnake.gaspunk.client;

import ladysnake.gaspunk.CommonProxy;
import ladysnake.gaspunk.init.ModItems;
import ladysnake.gaspunk.item.ItemGasTube;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

import java.awt.*;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

    @Override
    public void postInit() {
        super.postInit();
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(((stack, tintIndex) -> tintIndex == 0
                ? ItemGasTube.getContainedGas(stack).getColor()
                : Color.WHITE.getRGB()), ModItems.GRENADE);
    }

    @Override
    public void makeSmoke(World world, double x, double y, double z, int color, int amount) {
        if (!world.isRemote) return;
        float b = (color & 0xFF) / 255F;
        float g = (color >> 8 & 0xFF) / 255F;
        float r = (color >> 16 & 0xFF) / 255F;
        float a = (color >> 24 & 0xFF) / 255F;
    }

}
