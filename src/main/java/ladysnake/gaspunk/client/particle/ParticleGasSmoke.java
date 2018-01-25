package ladysnake.gaspunk.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleSmokeLarge;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.world.World;

public class ParticleGasSmoke extends ParticleSmokeNormal {
    public ParticleGasSmoke(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, float scale) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, motionX, motionY, motionZ, scale);
    }


}
