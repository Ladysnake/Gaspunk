package ladysnake.gaspunk.client.particle;

import ladysnake.gaspunk.api.basetype.GasParticleTypes;
import net.fabricmc.fabric.api.particles.ParticleRegistry;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleGasSmoke extends SpriteBillboardParticle {

    private float particleMaxAlpha;

    public ParticleGasSmoke(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float red, float green, float blue, float maxAlpha, float scale) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.setSprite(GasParticleTypes.GAS.getParticleTexture());
        this.colorRed = red;
        this.colorGreen = green;
        this.colorBlue = blue;
        this.colorAlpha = 0;
        this.particleMaxAlpha = maxAlpha;
        this.giveRandomVelocity(0, 0, 0);
        this.velocityY *= 0.5;
        this.scale = scale;
        this.setMaxAge(100);
    }

    public void setSprite(Identifier particleTexture) {
        this.setSprite(ParticleRegistry.INSTANCE.getParticleSpriteAtlas().getSprite(particleTexture));
    }

    public void giveRandomVelocity(float xSpeedIn, float ySpeedIn, float zSpeedIn) {
        this.velocityX = xSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4;
        this.velocityY = ySpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4;
        this.velocityZ = zSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.4;
        float f = (float) (Math.random() + Math.random() + 1.0D) * 0.15F;
        float f1 = MathHelper.sqrt(this.velocityX * this.velocityX + this.velocityY * this.velocityY + this.velocityZ * this.velocityZ);
        this.velocityX = this.velocityX / (double) f1 * (double) f * 0.4;
        this.velocityY = this.velocityY / (double) f1 * (double) f * 0.4 + 0.1;
        this.velocityZ = this.velocityZ / (double) f1 * (double) f * 0.4;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        if (this.age++ >= this.maxAge) {
            this.colorAlpha *= .99;
            if (this.colorAlpha <= 0.01)
                this.markDead();
        } else {
            colorAlpha = Math.min(age / (maxAge / 2f) * particleMaxAlpha, 1f);
        }

        this.velocityY += 0.0005D;
        this.move(this.velocityX, this.velocityY, this.velocityZ);

        if (this.y == this.prevPosY) {
            this.velocityX *= 1.1D;
            this.velocityZ *= 1.1D;
        }

        this.velocityX *= 0.9;
        this.velocityY *= 0.9599999785423279D;
        this.velocityZ *= 0.9;

        if (this.onGround) {
            this.velocityX *= 0.699999988079071D;
            this.velocityZ *= 0.699999988079071D;
        }
    }
}
