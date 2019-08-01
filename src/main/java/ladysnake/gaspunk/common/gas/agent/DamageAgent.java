package ladysnake.gaspunk.common.gas.agent;

import ladysnake.gaspunk.api.IBreathingHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public class DamageAgent extends GasAgent {
    // the amount of damage dealt with a potency of 1
    private int maxDamage;

    public DamageAgent(int maxDamage) {
        super(true);
        this.maxDamage = maxDamage;
    }

    @Override
    public void applyEffect(LivingEntity entity, IBreathingHandler handler, float concentration, boolean firstTick, float potency, boolean forced) {
        if (forced || handler.getAirSupply() <= 0) {
            entity.damage(DamageSource.DROWN, potency * maxDamage);
        }
    }
}
