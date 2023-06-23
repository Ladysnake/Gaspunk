package org.ladysnake.pathos.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.ladysnake.pathos.Pathos;

public class SicknessEffect {

    public static final Registry<SicknessEffect> REGISTRY = Pathos.SICKNESS_EFFECT_REGISTRY;
    private String translationKey;

    public Identifier getId() {
        return REGISTRY.getId(this);
    }

    /**
     * Performs this api effect on the afflicted entity
     *
     * @param carrier the entity affected by this disease
     * @param effect  the specific effect afflicting this entity
     */
    public void performEffect(LivingEntity carrier, SicknessInstance effect) {
        // NO-OP
    }

    /**
     * Called whenever an instance of an associated api effect is cured from an entity
     *
     * @param sicknessInstance the effect that is being removed
     * @param carrier        the entity that was being affected by this effect
     */
    public void onCured(SicknessInstance sicknessInstance, LivingEntity carrier) {
        // NO-OP
    }

    public String getTranslationKey() {
        if (translationKey == null) {
            translationKey = Util.createTranslationKey(Pathos.MODID + ".sickness_effect", getId());
        }
        return translationKey;
    }

    static void setSeverity(LivingEntity carrier, SicknessInstance effect, float severity) {
        effect.setSeverity(severity);
        SicknessHandler.of(carrier).sync();
    }
}
