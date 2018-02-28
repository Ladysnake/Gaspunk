package ladysnake.gaspunk.gas;

import ladysnake.gaspunk.gas.core.GasTypes;
import ladysnake.gaspunk.api.IBreathingHandler;
import ladysnake.gaspunk.sickness.SicknessToxicGas;
import net.minecraft.entity.EntityLivingBase;

public class GasSarin extends LingeringGas {
    public GasSarin() {
        super(SicknessToxicGas::new, GasTypes.GAS, 0x00FFFFFF, 0x00FFFFFF, 0.8f, true, ParticleTypes.VAPOR);
    }

    @Override
    public void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick) {
        if (!entity.isSneaking() || handler.getAirSupply() <= 0)
            super.applyEffect(entity, handler, concentration, firstTick);
    }
}
