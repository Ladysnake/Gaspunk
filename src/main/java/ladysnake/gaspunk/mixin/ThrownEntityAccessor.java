package ladysnake.gaspunk.mixin;

import net.minecraft.entity.thrown.ThrownEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ThrownEntity.class)
public interface ThrownEntityAccessor {
    @Accessor("field_7638")
    int getGhostTicks();
    @Accessor("field_7638")
    void setGhostTicks(int ghostTicks);
}
