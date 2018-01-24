package ladysnake.gaspunk.gas;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Gas extends IForgeRegistryEntry.Impl<Gas> {

    public boolean isToxic() {
        return false;
    }

    public void applyEffect(EntityLivingBase entity, float concentration) {

    }

}
