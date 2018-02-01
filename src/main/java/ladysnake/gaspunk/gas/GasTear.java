package ladysnake.gaspunk.gas;

import ladysnake.gaspunk.gas.core.GasTypes;
import ladysnake.gaspunk.gas.core.IBreathingHandler;
import ladysnake.gaspunk.gas.core.IGas;
import ladysnake.gaspunk.network.PacketHandler;
import ladysnake.gaspunk.network.ShaderMessage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class GasTear extends Gas {
    public GasTear(int color) {
        super(GasTypes.GAS, color);
    }

    @Override
    public void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick) {
        super.applyEffect(entity, handler, concentration, firstTick);
        if (!handler.isImmune()) {
            if (firstTick && entity instanceof EntityPlayerMP)
                PacketHandler.NET.sendTo(new ShaderMessage("shaders/post/blur.json"), (EntityPlayerMP) entity);
            entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 5));
        }
    }

    @Override
    public ParticleTypes getParticleType() {
        return ParticleTypes.TEARGAS;
    }

    @Override
    public void onExitCloud(EntityLivingBase entity, IBreathingHandler handler) {
        if (entity instanceof EntityPlayerMP)
            PacketHandler.NET.sendTo(new ShaderMessage(null), (EntityPlayerMP) entity);
    }
}
