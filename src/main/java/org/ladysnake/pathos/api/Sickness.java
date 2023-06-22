package org.ladysnake.pathos.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.ladysnake.pathos.Pathos;

public class Sickness {

    public static final Registry<Sickness> REGISTRY = Pathos.SICKNESS_REGISTRY;
    private String translationKey;

    public Identifier getId() {
        return REGISTRY.getId(this);
    }

    /**
     * Performs this api effect on the afflicted entity
     *
     * @param carrier the entity affected by this disease
     * @param effect  the specific effect afflicting this entity
     * @return true to reset the ticksSinceLastPerform counter on the effect
     */
    public boolean performEffect(LivingEntity carrier, SicknessEffect effect) {
        return true;
    }

    /**
     * Called whenever an instance of an associated api effect is cured from an entity
     *
     * @param sicknessEffect the effect that is being removed
     * @param carrier        the entity that was being affected by this effect
     */
    public void onCured(SicknessEffect sicknessEffect, LivingEntity carrier) {
        // NO-OP
    }

    public float getSampleChance() {
        return 0.5F;
    }

    public String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.createTranslationKey("sickness", getId());
        }
        return translationKey;
    }

    static void setSeverity(LivingEntity carrier, SicknessEffect effect, float severity) {
        effect.setSeverity(severity);
        SicknessHandler.of(carrier).sync();
    }
}
