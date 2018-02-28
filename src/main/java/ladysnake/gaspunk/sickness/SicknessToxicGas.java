package ladysnake.gaspunk.sickness;

import ladysnake.gaspunk.gas.LingeringGas;
import ladysnake.pathos.api.SicknessEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;

public class SicknessToxicGas extends SicknessGas {
    public SicknessToxicGas(LingeringGas gas) {
        super(gas, 0.01f);
    }

    @Override
    public boolean performEffect(EntityLivingBase carrier, SicknessEffect effect) {
        super.performEffect(carrier, effect);
        if (!carrier.world.isRemote && effect.getTicksSinceLastPerform() % 20 == 0) {
            carrier.attackEntityFrom(DamageSource.DROWN, MathHelper.floor(effect.getSeverity() * 10));
            return true;
        }
        return false;
    }
}
