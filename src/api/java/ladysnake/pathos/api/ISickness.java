package ladysnake.pathos.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface ISickness extends IForgeRegistryEntry<ISickness> {

    /**
     * Performs this sickness effect on the afflicted entity
     *
     * @param carrier the entity affected by this disease
     * @param effect the specific effect afflicting this entity
     * @return true to reset the ticksSinceLastPerform counter on the effect
     */
    boolean performEffect(EntityLivingBase carrier, SicknessEffect effect);

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
    default void onCured(SicknessEffect sicknessEffect, EntityLivingBase carrier) {
        // NO-OP
    }
}
