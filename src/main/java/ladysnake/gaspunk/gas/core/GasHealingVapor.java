package ladysnake.gaspunk.gas.core;

import ladysnake.gaspunk.api.IBreathingHandler;
import ladysnake.gaspunk.gas.Gas;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class GasHealingVapor extends Gas {

    public GasHealingVapor() {
        super(GasTypes.VAPOR, 0xFFCD5CAB);
    }

    @Override
    public void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick) {
        super.applyEffect(entity, handler, concentration, firstTick);
        if (!entity.world.isRemote)
            // Why 202 you ask ? Well to be sure that the gui displays "10 seconds"
            entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 202, 1));
    }
}
