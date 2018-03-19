package ladysnake.gaspunk.sickness;

import ladysnake.pathos.api.SicknessEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;

public class SicknessSarin extends SicknessGas {

//    public static final UUID SARIN_BREATH_PENALTY_ID = UUID.fromString("3f3d3c77-5c3e-4beb-bb0c-02c58bf580cb");
//    public static final AttributeModifier SARIN_BREATH_PENALTY = new AttributeModifier(SARIN_BREATH_PENALTY_ID, "Sarin air supply penalty", -0.5, 1);

    public SicknessSarin() {
        super(0.004f);
    }

    @Override
    public boolean performEffect(EntityLivingBase carrier, SicknessEffect effect) {
        super.performEffect(carrier, effect);
        if (!carrier.world.isRemote) {
//            if (carrier.getEntityAttribute(CapabilityBreathing.MAX_AIR_SUPPLY).getModifier(SARIN_BREATH_PENALTY_ID) == null)
//                carrier.getEntityAttribute(CapabilityBreathing.MAX_AIR_SUPPLY).applyModifier(SARIN_BREATH_PENALTY);
            if (effect.getTicksSinceLastPerform() % 20 == 0) {
                carrier.attackEntityFrom(DamageSource.DROWN, MathHelper.ceil(effect.getSeverity() * 10));
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCured(SicknessEffect sicknessEffect, EntityLivingBase carrier) {
//        carrier.getEntityAttribute(CapabilityBreathing.MAX_AIR_SUPPLY).removeModifier(SARIN_BREATH_PENALTY);
    }
}
