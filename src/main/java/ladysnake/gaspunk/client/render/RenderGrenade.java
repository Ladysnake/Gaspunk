package ladysnake.gaspunk.client.render;

import ladysnake.gaspunk.common.entity.GrenadeEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class RenderGrenade extends FlyingItemEntityRenderer<GrenadeEntity> {
    public RenderGrenade(EntityRenderDispatcher dispatcher, ItemRenderer itemRendererIn) {
        super(dispatcher, itemRendererIn);
    }

    @Nonnull
    @Override
    public ItemStack getStack(GrenadeEntity entityIn) {
        return entityIn.getStack();
    }
}
