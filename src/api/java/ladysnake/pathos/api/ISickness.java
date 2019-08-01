package ladysnake.pathos.api;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface ISickness extends IForgeRegistryEntry<ISickness> {

    /**
     * Performs this sickness effect on the afflicted entity
     *
     * @param carrier the entity affected by this disease
     * @param effect the specific effect afflicting this entity
     * @return true to reset the ticksSinceLastPerform counter on the effect
     */
    boolean performEffect(LivingEntity carrier, SicknessEffect effect);

    /**
     * @return the unlocalized name for this sickness
     */
    default String getUnlocalizedName() {
        // pattern : sickness.<modid>.<name>
        return ("sickness." + getRegistryName()).replace(':', '.');
    }

    /**
     * Called whenever an instance of an associated sickness effect is cured from an entity
     * @param sicknessEffect the effect that is being removed
     * @param carrier the entity that was being affected by this effect
     */
    default void onCured(SicknessEffect sicknessEffect, LivingEntity carrier) {
        // NO-OP
    }

    /**
     * Returns whether this sickness should be synchronized with the client.
     * <p>
     * This usually means that it has graphical effects.
     * Remember that it may be a good idea to use {@link ladysnake.pathos.api.event.SicknessEvent}
     * to interact with sicknesses on the client
     * </p>
     * @return true if this sickness should be synchronized with the client
     */
    default boolean isSynchronized() {
        return false;
    }
}
