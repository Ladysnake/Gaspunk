package ladysnake.pathos.sickness;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface ISickness extends IForgeRegistryEntry<ISickness> {

    /**
     *
     * @param carrier the entity affected by this disease
     * @param effect the specific effect afflicting this entity
     * @return true to reset the ticksSinceLastPerform counter on the effect
     */
    boolean performEffect(EntityLivingBase carrier, SicknessEffect effect);

    default String getUnlocalizedName() {
        // pattern : sickness.<modid>.<name>
        return ("sickness." + getRegistryName()).replace(':', '.');
    }

}
