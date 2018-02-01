package ladysnake.gaspunk.client;

import ladysnake.gaspunk.CommonProxy;
import ladysnake.gaspunk.client.particle.ParticleGasSmoke;
import ladysnake.gaspunk.client.particle.ParticleManager;
import ladysnake.gaspunk.client.render.entity.LayerBelt;
import ladysnake.gaspunk.gas.core.IGas;
import ladysnake.gaspunk.init.ModItems;
import ladysnake.gaspunk.item.ItemGasTube;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
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
                ? ItemGasTube.getContainedGas(stack).getBottleColor()
                : Color.WHITE.getRGB()), ModItems.GAS_TUBE, ModItems.GRENADE);
        if (Loader.isModLoaded("baubles"))
            Minecraft.getMinecraft().getRenderManager().getSkinMap().forEach((s, render) -> render.addLayer(new LayerBelt()));
    }

    @Override
    public void makeSmoke(World world, double x, double y, double z, int color, int amount, int radX, int radY, IGas.ParticleTypes texture) {
        if (!world.isRemote) return;
        float b = (color & 0xFF) / 255F;
        float g = (color >> 8 & 0xFF) / 255F;
        float r = (color >> 16 & 0xFF) / 255F;
        float a = (color >> 24 & 0xFF) / 255F;
        // no need to spawn invisible particles
        if (a == 0) return;

        for (int i = 0; i < amount; i++) {
            particleCount += world.rand.nextInt(3);
            if (particleCount % (Minecraft.getMinecraft().gameSettings.particleSetting == 0 ? 1 : 2 * Minecraft.getMinecraft().gameSettings.particleSetting) == 0) {
                double posX = x + world.rand.nextGaussian() * radX % radX;
                double posY = y + world.rand.nextGaussian() * radY % radY;
                double posZ = z + world.rand.nextGaussian() * radX % radX;
                ParticleGasSmoke particle = new ParticleGasSmoke(world, posX, posY, posZ, r, g, b, a, (float) (55 + 20*world.rand.nextGaussian()));
                switch (texture) {
                    case CHLORINE: particle.setTexture(ParticleGasSmoke.CHLORINE_TEXTURE); break;
                    case TEARGAS: particle.setTexture(ParticleGasSmoke.TEARGAS_TEXTURE); break;
                    case SMOKE: particle.setTexture(ParticleGasSmoke.SMOKE_TEXTURE); break;
                    default: particle.setTexture(ParticleGasSmoke.VAPOR_TEXTURE);
                }
                ParticleManager.INSTANCE.addParticle(particle);
            }
        }
    }


}
