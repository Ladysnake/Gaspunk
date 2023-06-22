package org.ladysnake.gaspunk.sickness;

import net.minecraft.entity.LivingEntity;
import org.ladysnake.pathos.api.SeverityFunction;
import org.ladysnake.pathos.api.Sickness;
import org.ladysnake.pathos.api.SicknessEffect;
import org.ladysnake.pathos.api.SicknessHandler;

public class GasSickness extends Sickness {

    protected final SeverityFunction severityFunction; //FIXME move into SicknessEffect and make data-driven

    public GasSickness(SeverityFunction severityFunction) {
        this.severityFunction = severityFunction;
    }

    @Override
    public boolean performEffect(LivingEntity carrier, SicknessEffect effect) {
        if(effect.getRemainingSeverity(severityFunction) <= 0.0F) {
            SicknessHandler sicknessHandler = SicknessHandler.of(carrier);
            sicknessHandler.cure(effect.getSickness());
            sicknessHandler.sync();
            return true;
        }
        return false;
    }

    @Override
    public float getSampleChance() {
        return 0.0F;
    }
}
