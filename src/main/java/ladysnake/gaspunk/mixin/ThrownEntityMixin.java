package ladysnake.gaspunk.mixin;

import ladysnake.gaspunk.common.entity.CustomizableCollider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.thrown.ThrownEntity;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ThrownEntity.class)
public abstract class ThrownEntityMixin extends Entity {
    public ThrownEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ProjectileUtil;getCollision(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;Lnet/minecraft/world/RayTraceContext$ShapeType;Z)Lnet/minecraft/util/hit/HitResult;"))
    private RayTraceContext.ShapeType fixCollisions(RayTraceContext.ShapeType shapeType) {
        if (this instanceof CustomizableCollider) {
            return ((CustomizableCollider) this).getRayTracingShapeType();
        }
        return shapeType;
    }
}
