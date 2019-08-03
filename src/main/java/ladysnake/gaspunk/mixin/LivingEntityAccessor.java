package ladysnake.gaspunk.mixin;

import ladysnake.gaspunk.mixin.client.LivingEntityRendererAccessor;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Invoker
    int invokeGetNextBreathInWater(int air);
}
