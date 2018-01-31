package ladysnake.gaspunk.potion;

import ladysnake.gaspunk.gas.core.ILingeringGas;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class PotionGas extends Potion {
    private final ILingeringGas gas;

    public PotionGas(boolean isBadEffectIn, int liquidColorIn, ILingeringGas gas) {
        super(isBadEffectIn, liquidColorIn);
        this.gas = gas;
    }

    @Override
    public void performEffect(@Nonnull EntityLivingBase entityLivingBaseIn, int amplifier) {
        gas.applyLingeringEffect(entityLivingBaseIn, amplifier);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return gas.isReadyForLingeringEffect(duration, amplifier);
    }

    @Nonnull
    @Override
    public List<ItemStack> getCurativeItems() {
        return Collections.emptyList();
    }

    @Override
    public boolean shouldRender(PotionEffect effect) {
        return false;
    }

    @Override
    public boolean shouldRenderInvText(PotionEffect effect) {
        return false;
    }

    @Override
    public boolean shouldRenderHUD(PotionEffect effect) {
        return false;
    }
}
