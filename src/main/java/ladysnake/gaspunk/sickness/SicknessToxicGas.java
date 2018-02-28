package ladysnake.gaspunk.sickness;

import ladysnake.gaspunk.gas.LingeringGas;
import ladysnake.pathos.sickness.Sickness;
import ladysnake.pathos.sickness.SicknessEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;

public class SicknessToxicGas extends Sickness {
    private LingeringGas gas;

    public SicknessToxicGas(LingeringGas gas) {
        this.gas = gas;
    }

    @Override
    public boolean performEffect(EntityLivingBase carrier, SicknessEffect effect) {
        carrier.attackEntityFrom(DamageSource.DROWN, MathHelper.floor(gas.getToxicity()));
        return false;
    }
}
