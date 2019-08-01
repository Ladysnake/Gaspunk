package ladysnake.gaspunk.client.render.entity;

import baubles.api.BaublesApi;
import baubles.api.render.IRenderBauble;
import ladysnake.gaspunk.client.model.BandoulierModel;
import ladysnake.gaspunk.common.item.GasPunkItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

@Environment(EnvType.CLIENT)
public class LayerBelt extends FeatureRenderer<PlayerEntity, BandoulierModel<PlayerEntity>> {

    private BandoulierModel<PlayerEntity> bandoulier = new BandoulierModel<>();

    public LayerBelt(FeatureRendererContext<PlayerEntity, BandoulierModel<PlayerEntity>> ctx) {
        super(ctx);
    }

    @Override
    public void render(@Nullable PlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (player != null && BaublesApi.isBaubleEquipped(player, GasPunkItems.GRENADE_BELT) >= 0) {
            IRenderBauble.Helper.rotateIfSneaking(player);
            MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier("gaspunk:textures/entity/grenade_belt.png"));
            bandoulier.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean hasHurtOverlay() {
        return false;
    }
}
