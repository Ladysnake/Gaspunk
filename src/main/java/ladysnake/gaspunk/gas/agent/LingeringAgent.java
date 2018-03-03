package ladysnake.gaspunk.gas.agent;

import ladysnake.gaspunk.api.IBreathingHandler;
import ladysnake.gaspunk.gas.GasAgents;
import ladysnake.pathos.api.SicknessEffect;
import ladysnake.pathos.capability.CapabilitySickness;
import net.minecraft.entity.EntityLivingBase;

/**
 * An agent that applies a long-term affliction to entities breathing it
 */
public class LingeringAgent extends GasAgent {

    protected boolean ignoreBreath;

    public LingeringAgent(boolean toxic, boolean ignoreBreath) {
        super(toxic);
        this.ignoreBreath = ignoreBreath;
    }

    /**
     * Adds a sickness effect to the entity breathing this gas
     * By default this will add a new effect with a severity computed as
     * <pre> {@code (concentration * toxicity) / 20f}</pre>
     * The resulting effect severity will be cumulated with the previous one, if present
     * (this can be changed by other implementations)
     *
     * @param entity        the entity breathing this gas
     * @param concentration the concentration in the air breathed by this entity
     * @param potency       the potency of the gas carrying this agent
     */
    public void addEffectToEntity(EntityLivingBase entity, float concentration, float potency) {
        CapabilitySickness.getHandler(entity).ifPresent(h -> h.addSickness(new SicknessEffect(GasAgents.LINGERING_EFFECTS.get(this), (potency * concentration) / 20)));
    }

    @Override
    public void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick, float potency) {
        if (ignoreBreath || handler.getAirSupply() <= 0)
            addEffectToEntity(entity, concentration, potency);
    }

}
