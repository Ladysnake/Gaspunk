package org.ladysnake.gaspunk.client;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import org.ladysnake.gaspunk.client.render.entity.GrenadeEntityRenderer;
import org.ladysnake.gaspunk.init.GPEntities;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

@ClientOnly
public class GasPunkClient implements ClientModInitializer {

    @Override
    public void onInitializeClient(ModContainer mod) {
        EntityRendererRegistry.register(GPEntities.GRENADE, GrenadeEntityRenderer::new);
    }
}
