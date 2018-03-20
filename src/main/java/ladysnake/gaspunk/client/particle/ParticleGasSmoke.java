package ladysnake.gaspunk.client.particle;

import ladylib.client.particle.SpecialParticle;
import ladysnake.gaspunk.gas.core.GasParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleGasSmoke extends SpecialParticle {

    public float particleMaxAlpha;

    public ParticleGasSmoke(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float red, float green, float blue, float maxAlpha, float scale) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.setTexture(GasParticleTypes.GAS.getParticleTexture());
        this.particleRed = red;
        this.particleGreen = green;
        this.particleBlue = blue;
        this.particleAlpha = 0;
        this.particleMaxAlpha = maxAlpha;
        this.giveRandomMotion(0, 0, 0);
        this.motionY *= 0.5;
        this.particleScale = scale;
        this.setMaxAge(100);
    }

    public void giveRandomMotion(float xSpeedIn, float ySpeedIn, float zSpeedIn) {
        this.motionX = xSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        this.motionY = ySpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        this.motionZ = zSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4000000059604645D;
        float f = (float) (Math.random() + Math.random() + 1.0D) * 0.15F;
        float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        this.motionX = this.motionX / (double) f1 * (double) f * 0.4000000059604645D;
        this.motionY = this.motionY / (double) f1 * (double) f * 0.4000000059604645D + 0.10000000149011612D;
        this.motionZ = this.motionZ / (double) f1 * (double) f * 0.4000000059604645D;
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge) {
            this.particleAlpha *= .99;
            if (this.particleAlpha <= 0.01)
                this.setExpired();
        } else
            particleAlpha = Math.min((float) particleAge / (particleMaxAge / 2) * particleMaxAlpha, 1);

        this.motionY += 0.0005D;
        this.move(this.motionX, this.motionY, this.motionZ);

        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.motionX *= 0.9;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9;

        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }
}
