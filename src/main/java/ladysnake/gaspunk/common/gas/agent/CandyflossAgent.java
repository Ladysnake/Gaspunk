package ladysnake.gaspunk.common.gas.agent;

import ladysnake.gaspunk.api.IBreathingHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class CandyflossAgent extends GasAgent {

    public CandyflossAgent() {
        super(true);    // toxic to make it decrease air
    }

    @Override
    public void applyEffect(LivingEntity entity, IBreathingHandler handler, float concentration, boolean firstTick, float potency, boolean forced) {
        if ((entity instanceof PlayerEntity) && (forced || (((concentration * potency) > 0) && ((entity.world.getTime() % ((int)(20.0f / (concentration * potency)))) == 0)))) {
            if (((PlayerEntity) entity).world.random.nextBoolean()) {
                ((PlayerEntity) entity).getHungerManager().add(1, 1F);
            } else {
                ((PlayerEntity) entity).getHungerManager().add(1, 0F);
            }
        }
    }
}
