package ladysnake.gaspunk.gas;

import ladysnake.gaspunk.gas.core.IBreathingHandler;
import ladysnake.gaspunk.gas.core.IGasType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class GasToxic extends Gas {
    public GasToxic(IGasType type, int color) {
        super(type, color);
    }

    @Override
    public boolean isToxic() {
        return true;
    }

    @Override
    public void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick) {
        if (handler.getAirSupply() <= 0) {
            entity.attackEntityFrom(DamageSource.DROWN, 2);
        }
    }
}
