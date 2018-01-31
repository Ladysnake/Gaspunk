package ladysnake.gaspunk.gas.core;

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
        if (!entity.world.isRemote)
            entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 5));
    }
}