package ladysnake.gaspunk.gas;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class ToxicGas extends Gas {
    public ToxicGas(IGasType type, int color) {
        super(type, color);
    }

    @Override
    public boolean isToxic() {
        return true;
    }

    @Override
    public void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration) {
        if (handler.getAirSupply() <= 0) {
            entity.attackEntityFrom(DamageSource.DROWN, 2);
        }
    }
}
