package ladysnake.sicklib.sickness;

import ladysnake.sicklib.Pathos;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

@GameRegistry.ObjectHolder(Pathos.MOD_ID)
public abstract class Sickness extends IForgeRegistryEntry.Impl<ISickness> implements ISickness {

    public static final ISickness NONE = null;
    public static IForgeRegistry<ISickness> REGISTRY;
}
