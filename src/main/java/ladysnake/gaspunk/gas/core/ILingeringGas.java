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

public interface ILingeringGas extends IGas {

    Map<ILingeringGas, ISickness> LINGERING_EFFECTS = new HashMap<>();

    @SuppressWarnings("unused")
    static void onRegistryAddGas(IForgeRegistryInternal<IGas> owner, RegistryManager stage, int id, IGas obj, @Nullable IGas oldObj) {
        if (obj instanceof ILingeringGas) {
            LINGERING_EFFECTS.put((ILingeringGas) obj, ((ILingeringGas) obj).createPotion());
        }
    }

    boolean applyLingeringEffect(EntityLivingBase entity, SicknessEffect effect);

    default ISickness createPotion() {
        return new SicknessGas(this).setRegistryName(Objects.requireNonNull(this.getRegistryName()));
    }

    default float getToxicity() {
        return 1F;
    }

    default void addEffectToEntity(EntityLivingBase entity, float concentration) {
        CapabilitySickness.getHandler(entity).ifPresent(h -> h.addSickness(new SicknessEffect(LINGERING_EFFECTS.get(this), getToxicity() * concentration)));
    }

    @Override
    default void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick) {
        addEffectToEntity(entity, concentration);
    }
}
