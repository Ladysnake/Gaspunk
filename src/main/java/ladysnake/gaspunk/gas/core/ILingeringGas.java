package ladysnake.gaspunk.gas.core;

import ladysnake.gaspunk.potion.PotionGas;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryManager;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public interface ILingeringGas extends IGas {

    Map<ILingeringGas, Potion> LINGERING_EFFECTS = new HashMap<>();

    @SuppressWarnings("unused")
    static void onRegistryAddGas(IForgeRegistryInternal<IGas> owner, RegistryManager stage, int id, IGas obj, @Nullable IGas oldObj) {
        if (obj instanceof ILingeringGas) {
            LINGERING_EFFECTS.put((ILingeringGas) obj, ((ILingeringGas) obj).createPotion());
        }
    }

    void applyLingeringEffect(EntityLivingBase entity, int amplifier);

    default boolean isReadyForLingeringEffect(int duration, int amplifier) {
        int j = 25 >> amplifier;
        return j <= 0 || duration % j == 0;
    }

    default Potion createPotion() {
        return new PotionGas(this.isToxic(), this.getColor(), this).setRegistryName(Objects.requireNonNull(this.getRegistryName()));
    }

    default int getLingeringTime() {
        return 500;
    }

    default void addEffectToEntity(EntityLivingBase entity, float concentration) {
        entity.addPotionEffect(new PotionEffect(LINGERING_EFFECTS.get(this), MathHelper.floor(getLingeringTime() * concentration)));
    }

    @Override
    default void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick) {
        addEffectToEntity(entity, concentration);
    }
}
