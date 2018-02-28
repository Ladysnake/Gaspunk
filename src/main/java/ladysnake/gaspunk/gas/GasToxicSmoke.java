package ladysnake.gaspunk.gas;

import ladysnake.gaspunk.api.IBreathingHandler;
import ladysnake.gaspunk.api.IGasType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class GasToxicSmoke extends Gas {

    public GasToxicSmoke(IGasType type, int color) {
        super(type, color);
    }

    @Override
    public boolean isToxic() {
        return true;
    }

    @Override
    public void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick) {
        if (handler.getAirSupply() <= 0)
            entity.attackEntityFrom(DamageSource.DROWN, 2);
    }
}
