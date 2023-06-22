package org.ladysnake.gaspunk.sickness;

import net.minecraft.entity.LivingEntity;
import org.ladysnake.gaspunk.api.SeverityFunction;
import org.ladysnake.pathos.api.SicknessEffect;

public class SarinGasSickness extends GasSickness {

    public SarinGasSickness(SeverityFunction severityFunction) {
        super(severityFunction);
    }

    @Override
    public boolean performEffect(LivingEntity carrier, SicknessEffect effect) {
        //TODO temporary
        if (super.performEffect(carrier, effect)) {
            return true;
        }

        if (effect.getTicksSinceLastPerform() >= 20) {
            if (!carrier.getWorld().isClient()) {
                carrier.damage(carrier.getWorld().getDamageSources().drown(), effect.getRemainingSeverity(severityFunction) * 10.0F);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onCured(SicknessEffect sicknessEffect, LivingEntity carrier) {
        //TODO remove max air penalty
    }
}
