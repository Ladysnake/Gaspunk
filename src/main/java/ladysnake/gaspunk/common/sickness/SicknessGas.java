package ladysnake.gaspunk.common.sickness;

import ladysnake.pathos.api.SicknessEffect;
import ladysnake.pathos.sickness.Sickness;
import net.minecraft.entity.LivingEntity;

public abstract class SicknessGas extends Sickness {
    protected float severityDecreasePerTick;

    protected SicknessGas(float severityDecreasePerTick) {
        this.severityDecreasePerTick = severityDecreasePerTick;
    }

    @Override
    public boolean performEffect(LivingEntity carrier, SicknessEffect effect) {
        decrementSeverity(carrier, effect);
        return false;
    }

    protected void decrementSeverity(LivingEntity carrier, SicknessEffect effect) {
        float severity = effect.getSeverity();
        if (severity > 0)
            effect.setSeverity(severity - severityDecreasePerTick);
        else
            effect.setSeverity(0);
    }
}
