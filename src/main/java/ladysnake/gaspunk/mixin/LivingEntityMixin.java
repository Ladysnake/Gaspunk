package ladysnake.gaspunk.mixin;

import ladysnake.gaspunk.common.gas.core.GasPunkComponents;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "tickMovement", at = @At("RETURN"))
    private void tickGases(CallbackInfo ci) {
        GasPunkComponents.BREATHING.get(this).tick();
    }
}
