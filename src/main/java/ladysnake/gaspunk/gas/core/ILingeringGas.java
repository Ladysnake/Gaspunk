package ladysnake.gaspunk.gas.core;

import ladysnake.gaspunk.sickness.SicknessGas;
import ladysnake.sicklib.capability.CapabilitySickness;
import ladysnake.sicklib.sickness.ISickness;
import ladysnake.sicklib.sickness.SicknessEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryManager;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A variant of gas that apply a long-term affliction to entities breathing it
 */
public interface ILingeringGas extends IGas {

    Map<ILingeringGas, ISickness> LINGERING_EFFECTS = new HashMap<>();

    /**
     * Called whenever a gas is added to the registry
     * Generates a sickness associated to the gas
     */
    @SuppressWarnings("unused")
    static void onRegistryAddGas(IForgeRegistryInternal<IGas> owner, RegistryManager stage, int id, IGas obj, @Nullable IGas oldObj) {
        if (obj instanceof ILingeringGas) {
            LINGERING_EFFECTS.put((ILingeringGas) obj, ((ILingeringGas) obj).createSickness());
        }
    }

    /**
     * Called each tick an entity is afflicted by the sickness associated with this gas
     *
     * @param entity the afflicted entity
     * @param effect the instance of the sickness effect afflicting the entity
     * @return true to reset the ticksSinceLastPerform counter on the effect
     */
    boolean applyLingeringEffect(EntityLivingBase entity, SicknessEffect effect);

    /**
     * Creates a {@link ISickness} object that will be registered automatically when this gas is itself registered
     *
     * @return a sickness associated with this gas
     */
    default ISickness createSickness() {
        return new SicknessGas(this).setRegistryName(Objects.requireNonNull(this.getRegistryName()));
    }

    /**
     * @return this gas' toxicity
     * @see #addEffectToEntity(EntityLivingBase, float)
     */
    default float getToxicity() {
        return 0.1F;
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
     */
    default void addEffectToEntity(EntityLivingBase entity, float concentration) {
        CapabilitySickness.getHandler(entity).ifPresent(h -> h.addSickness(new SicknessEffect(LINGERING_EFFECTS.get(this), (getToxicity() * concentration) / 20)));
    }

    @Override
    default void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick) {
        addEffectToEntity(entity, concentration);
    }
}
