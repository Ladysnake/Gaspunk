package ladysnake.gaspunk.client.render;

import ladysnake.gaspunk.entity.EntityGrenade;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class RenderGrenade extends RenderSnowball<EntityGrenade> {
    public RenderGrenade(RenderManager renderManagerIn, Item itemIn, RenderItem itemRendererIn) {
        super(renderManagerIn, itemIn, itemRendererIn);
    }

    @Nonnull
    @Override
    public ItemStack getStackToRender(EntityGrenade entityIn) {
        return entityIn.getGrenade();
    }
}
