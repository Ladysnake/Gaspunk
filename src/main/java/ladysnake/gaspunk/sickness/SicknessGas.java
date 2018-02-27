package ladysnake.gaspunk.sickness;

import ladysnake.gaspunk.gas.core.ILingeringGas;
import ladysnake.pathos.sickness.Sickness;
import ladysnake.pathos.sickness.SicknessEffect;
import net.minecraft.entity.EntityLivingBase;

public class SicknessGas extends Sickness {
    private ILingeringGas afflictingGas;

    public SicknessGas(ILingeringGas afflictingGas) {
        this.afflictingGas = afflictingGas;
    }

    @Override
    public boolean performEffect(EntityLivingBase carrier, SicknessEffect effect) {
        return afflictingGas.applyLingeringEffect(carrier, effect);
    }
}
