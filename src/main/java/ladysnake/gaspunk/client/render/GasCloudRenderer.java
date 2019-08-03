package ladysnake.gaspunk.client.render;

import ladysnake.gaspunk.common.entity.GasCloudEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.util.Identifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GasCloudRenderer<E extends GasCloudEntity> extends EntityRenderer<E> {
    public GasCloudRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(@Nonnull E entity, double x, double y, double z, float entityYaw, float partialTicks) {
//        GlStateManager.pushMatrix();
//        GlStateManager.translate((float)x, (float)y, (float)z);
//        this.bindEntityTexture(entity);
//        RenderHelper.enableStandardItemLighting();
//        int j = entity.getBrightnessForRender();
//        int k = j % 65536;
//        int l = j / 65536;
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k, (float)l);
//        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//        GlStateManager.translate(0.0F, 0.1F, 0.0F);
//        GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
//        GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
//        GlStateManager.scale(1F, 1F, 1F);
//        ShaderHelper.useShader(ShaderHelper.dissolution);
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder bufferbuilder = tessellator.getBuffer();
//        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
//        bufferbuilder.pos(-0.5D, -0.25D, 0.0D).tex(0D, 0D).normal(0.0F, 1.0F, 0.0F).endVertex();
//        bufferbuilder.pos(0.5D, -0.25D, 0.0D).tex(0D, 16D).normal(0.0F, 1.0F, 0.0F).endVertex();
//        bufferbuilder.pos(0.5D, 0.75D, 0.0D).tex(16D, 16D).normal(0.0F, 1.0F, 0.0F).endVertex();
//        bufferbuilder.pos(-0.5D, 0.75D, 0.0D).tex(16D, 0D).normal(0.0F, 1.0F, 0.0F).endVertex();
//        tessellator.draw();
//        ShaderHelper.revert();
//        GlStateManager.disableBlend();
//        GlStateManager.disableRescaleNormal();
//        GlStateManager.popMatrix();
        super.render(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected Identifier getTexture(@Nonnull E entity) {
        return null;
    }
}
