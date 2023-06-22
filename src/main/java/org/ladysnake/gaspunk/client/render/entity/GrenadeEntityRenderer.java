package org.ladysnake.gaspunk.client.render.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import org.ladysnake.gaspunk.GrenadeEntity;
import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class GrenadeEntityRenderer extends FlyingItemEntityRenderer<GrenadeEntity> {

    //TODO 3d model, but for now we just use the item renderer

    public GrenadeEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }
}
