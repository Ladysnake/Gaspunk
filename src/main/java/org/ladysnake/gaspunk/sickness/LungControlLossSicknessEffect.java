package org.ladysnake.gaspunk.sickness;

import net.minecraft.entity.LivingEntity;
import org.ladysnake.pathos.api.SicknessEffect;
import org.ladysnake.pathos.api.SicknessInstance;

public class LungControlLossSicknessEffect extends SicknessEffect {

    @Override
    public void performEffect(LivingEntity carrier, SicknessInstance instance) {
        long time = carrier.getWorld().getTime();
        if (instance.getTicksSinceBeginning(time) % 20 == 0) {
            carrier.damage(carrier.getWorld().getDamageSources().drown(), instance.getRemainingSeverity(time) * 10.0F);
        }
    }

    @Override
    public void onCured(SicknessInstance sicknessInstance, LivingEntity carrier) {
        //TODO remove max air penalty
    }
}
