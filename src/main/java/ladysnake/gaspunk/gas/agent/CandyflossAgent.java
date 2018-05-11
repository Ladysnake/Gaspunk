package ladysnake.gaspunk.gas.agent;

import ladysnake.gaspunk.api.IBreathingHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class CandyflossAgent extends GasAgent {

    public CandyflossAgent() {
        super(true);    // toxic to make it decrease air
    }

    @Override
    public void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick, float potency, boolean forced) {
        if ((entity instanceof EntityPlayer) && (forced || (((concentration * potency) > 0) && ((entity.world.getWorldTime() % ((int)(20.0f / (concentration * potency)))) == 0)))) {
            if (((EntityPlayer) entity).world.rand.nextBoolean()) {
                ((EntityPlayer) entity).getFoodStats().setFoodSaturationLevel(((EntityPlayer) entity).getFoodStats().getSaturationLevel()+1);
            } else {
                ((EntityPlayer) entity).getFoodStats().setFoodLevel(((EntityPlayer) entity).getFoodStats().getFoodLevel()+1);
            }
        }
    }
}
