package ladysnake.gaspunk.client;

import ladysnake.gaspunk.CommonProxy;
import ladysnake.gaspunk.client.particle.ParticleGasSmoke;
import ladysnake.gaspunk.client.particle.ParticleManager;
import ladysnake.gaspunk.init.ModItems;
import ladysnake.gaspunk.item.ItemGasTube;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {
    private int particleCount = 0;

    @Override
    public void postInit() {
        super.postInit();
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(((stack, tintIndex) -> tintIndex == 0
                ? ItemGasTube.getContainedGas(stack).getColor()
                : Color.WHITE.getRGB()), ModItems.GAS_TUBE, ModItems.GRENADE);
    }

    @Override
    public void makeSmoke(World world, double x, double y, double z, int color, int amount, int radX, int radY, int radZ) {
        if (!world.isRemote) return;
        float b = (color & 0xFF) / 255F;
        float g = (color >> 8 & 0xFF) / 255F;
        float r = (color >> 16 & 0xFF) / 255F;
        float a = (color >> 24 & 0xFF) / 255F;
        for (int i = 0; i < amount; i++) {
            particleCount += world.rand.nextInt(3);
            if (particleCount % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
                double posX = x + world.rand.nextGaussian() * radX % radX;
                double posY = y + world.rand.nextGaussian() * radY % radY;
                double posZ = z + world.rand.nextGaussian() * radZ % radZ;
                ParticleManager.INSTANCE.addParticle(new ParticleGasSmoke(world, posX, posY, posZ, r, g, b, a, (float) (6 + world.rand.nextGaussian())));
            }
        }
    }


}
