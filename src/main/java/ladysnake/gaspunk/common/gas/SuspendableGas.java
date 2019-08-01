package ladysnake.gaspunk.common.gas;

import ladysnake.gaspunk.api.IBreathingHandler;
import ladysnake.gaspunk.api.IGasAgent;
import ladysnake.gaspunk.api.IGasType;
import net.minecraft.entity.LivingEntity;

/**
 * A gas that suspends its effect when the player is sneaking and has breath left
 */
public class SuspendableGas extends Gas {
    public SuspendableGas(IGasType type, int color, IGasAgent agent, float potency) {
        super(type, color, agent, potency);
    }

    @Override
    public void applyEffect(LivingEntity entity, IBreathingHandler handler, float concentration, boolean firstTick, boolean forced) {
        if (forced || !entity.isSneaking() || handler.getAirSupply() <= 0)
            super.applyEffect(entity, handler, concentration, firstTick, forced);
    }
}
