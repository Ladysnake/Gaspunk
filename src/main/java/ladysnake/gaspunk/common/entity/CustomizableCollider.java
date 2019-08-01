package ladysnake.gaspunk.common.entity;

import net.minecraft.world.RayTraceContext;

public interface CustomizableCollider {
    RayTraceContext.ShapeType getRayTracingShapeType();
}
