package ladysnake.pathos.sickness;

import ladysnake.pathos.Pathos;
import ladysnake.pathos.api.ISickness;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@GameRegistry.ObjectHolder(Pathos.MOD_ID)
public abstract class Sickness extends IForgeRegistryEntry.Impl<ISickness> implements ISickness {

    public static final ISickness NONE = null;
    public static IForgeRegistry<ISickness> REGISTRY;
}
