package ladysnake.gaspunk.common.gas.agent;

import ladysnake.gaspunk.api.IBreathingHandler;
import ladysnake.gaspunk.common.GasPunk;
import ladysnake.gaspunk.mixin.StatusEffectAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PotionAgent extends GasAgent {
    private StatusEffect statusEffect;
    private Identifier statusEffectId;
    private int effectDuration;
    private int effectAmplifier;

    public PotionAgent(Identifier statusEffect, int effectDuration, int effectAmplifier) {
        super(true);    // default to the statusEffect being toxic, will be changed later
        this.statusEffectId = statusEffect;
        this.effectDuration = effectDuration;
        this.effectAmplifier = effectAmplifier;
    }

    public StatusEffect getStatusEffect() {
        if (statusEffect == null) {
            statusEffect = Registry.STATUS_EFFECT.get(statusEffectId);
            if (!Registry.STATUS_EFFECT.containsId(statusEffectId)) {
                GasPunk.LOGGER.warn("Potion gas agent {} is linked to invalid status effect id {}", getRegistryName(), statusEffectId);
            }
            this.toxic = ((StatusEffectAccessor) statusEffect).getStatusEffectType() == StatusEffectType.HARMFUL;
        }
        return statusEffect;
    }

    @Override
    public void applyEffect(LivingEntity entity, IBreathingHandler handler, float concentration, boolean firstTick, float potency, boolean forced) {
        // still check for server-side because doing otherwise is stupid
        if (!entity.world.isClient && (forced || entity.world.getTime() % 20 == 0)) {
            StatusEffect statusEffect = getStatusEffect();
            entity.addPotionEffect(new StatusEffectInstance(statusEffect, effectDuration, effectAmplifier));
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public String getLocalizedName() {
        return I18n.translate(getUnlocalizedName(), I18n.translate(getStatusEffect().getTranslationKey()));
    }
}
