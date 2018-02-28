package ladysnake.gaspunk.sickness;

import ladysnake.gaspunk.gas.LingeringGas;
import ladysnake.pathos.api.SicknessEffect;
import ladysnake.pathos.sickness.Sickness;
import net.minecraft.entity.EntityLivingBase;

public abstract class SicknessGas extends Sickness {
    protected final LingeringGas gas;
    protected float severityDecreasePerTick;

    protected SicknessGas(LingeringGas gas, float severityDecreasePerTick) {
        this.gas = gas;
        this.severityDecreasePerTick = severityDecreasePerTick;
    }

    @Override
    public boolean performEffect(EntityLivingBase carrier, SicknessEffect effect) {
        decrementSeverity(carrier, effect);
        return false;
    }

    protected void decrementSeverity(EntityLivingBase carrier, SicknessEffect effect) {
        float severity = effect.getSeverity();
        if (severity > 0)
            effect.setSeverity(severity - severityDecreasePerTick);
    }

    public LingeringGas getGas() {
        return gas;
    }
}
