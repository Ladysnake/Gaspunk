package ladysnake.gaspunk.gas.agent;

import ladysnake.gaspunk.api.IBreathingHandler;
import ladysnake.pathos.api.ISickness;
import net.minecraft.entity.EntityLivingBase;

public class SarinAgent extends LingeringAgent {
    public SarinAgent(ISickness sickness) {
        super(true, true, sickness);
    }

    @Override
    public void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick, float potency) {
        if (!entity.isSneaking() || handler.getAirSupply() <= 0)
            super.applyEffect(entity, handler, concentration, firstTick, potency);
    }
}
