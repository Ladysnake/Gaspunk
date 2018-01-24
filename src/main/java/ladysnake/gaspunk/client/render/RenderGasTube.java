package ladysnake.gaspunk.client.render;

import ladysnake.gaspunk.entity.EntityGasTube;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class RenderGasTube extends RenderSnowball<EntityGasTube> {
    public RenderGasTube(RenderManager renderManagerIn, Item itemIn, RenderItem itemRendererIn) {
        super(renderManagerIn, itemIn, itemRendererIn);
    }

    @Nonnull
    @Override
    public ItemStack getStackToRender(EntityGasTube entityIn) {
        return entityIn.getGrenade();
    }
}
