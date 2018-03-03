package ladysnake.gaspunk.gas.agent;

import ladysnake.gaspunk.api.IBreathingHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class DamageAgent extends GasAgent {
    // the amount of damage dealt with a potency of 1
    private int maxDamage;

    public DamageAgent(int maxDamage) {
        super(true);
        this.maxDamage = maxDamage;
    }

    @Override
    public void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick, float potency) {
        if (handler.getAirSupply() <= 0)
            entity.attackEntityFrom(DamageSource.DROWN, potency * maxDamage);
    }
}
