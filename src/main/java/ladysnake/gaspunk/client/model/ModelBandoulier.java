package ladysnake.gaspunk.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

import javax.annotation.Nullable;

/**
 * ModelBandoulier - Alien
 * Created using Tabula 5.1.0
 */
public class ModelBandoulier extends ModelBiped {


    public ModelRenderer Bandoulier;
    public ModelRenderer shape15;
    public ModelRenderer EmptySlot3;
    public ModelRenderer EmptySlot4;
    public ModelRenderer shape48;
    public ModelRenderer shape15_1;
    public ModelRenderer shape15_2;
    public ModelRenderer Bag;
    public ModelRenderer shape27;
    public ModelRenderer shape27_1;
    public ModelRenderer EmptySlot2;
    public ModelRenderer shape53;
    public ModelRenderer EmptySlot1;
    public ModelRenderer shape22;
    public ModelRenderer shape23;
    public ModelRenderer shape23_1;
    public ModelRenderer shape23_2;
    public ModelRenderer shape26;
    public ModelRenderer Nade1;
    public ModelRenderer shape27_2;
    public ModelRenderer shape27_3;
    public ModelRenderer shape27_4;
    public ModelRenderer shape27_5;
    public ModelRenderer shape27_6;
    public ModelRenderer shape50;
    public ModelRenderer Nade2;
    public ModelRenderer shape27_7;
    public ModelRenderer shape27_8;
    public ModelRenderer shape27_9;
    public ModelRenderer shape27_10;
    public ModelRenderer shape27_11;
    public ModelRenderer shape50_1;
    public ModelRenderer shape15_3;
    public ModelRenderer shape15_4;

    public ModelBandoulier() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.shape15_4 = new ModelRenderer(this, 32, 31);
        this.shape15_4.setRotationPoint(-3.0F, 0.0F, -9.0F);
        this.shape15_4.addBox(-1.0F, -4.0F, 0.0F, 1, 4, 8, 0.0F);
        this.shape27 = new ModelRenderer(this, 48, 5);
        this.shape27.setRotationPoint(-8.5F, -2.5F, -0.5F);
        this.shape27.addBox(-1.5F, 0.0F, -1.0F, 3, 3, 2, 0.0F);
        this.setRotateAngle(shape27, 0.0F, 0.0F, 0.06283185307179587F);
        this.shape27_2 = new ModelRenderer(this, 0, 10);
        this.shape27_2.setRotationPoint(0.2F, 0.0F, 0.0F);
        this.shape27_2.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        this.shape50_1 = new ModelRenderer(this, 0, 5);
        this.shape50_1.setRotationPoint(-0.5F, 0.3F, 0.0F);
        this.shape50_1.addBox(0.0F, -1.0F, 0.0F, 1, 1, 0, 0.0F);
        this.shape23_2 = new ModelRenderer(this, 22, 25);
        this.shape23_2.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.shape23_2.addBox(-4.0F, 0.0F, 0.0F, 8, 1, 1, 0.0F);
        this.shape15_3 = new ModelRenderer(this, 23, 32);
        this.shape15_3.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.shape15_3.addBox(-4.0F, -4.0F, -1.0F, 4, 4, 1, 0.0F);
        this.setRotateAngle(shape15_3, 0.0F, 0.0F, 0.6283185307179586F);
        this.shape22 = new ModelRenderer(this, 36, 20);
        this.shape22.setRotationPoint(0.0F, 4.0F, 1.0F);
        this.shape22.addBox(-4.5F, 0.0F, -4.0F, 8, 1, 4, 0.0F);
        this.shape27_5 = new ModelRenderer(this, 54, 25);
        this.shape27_5.setRotationPoint(-0.2F, 0.0F, 0.2F);
        this.shape27_5.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        this.shape48 = new ModelRenderer(this, 2, 5);
        this.shape48.setRotationPoint(8.0F, -2.0F, 1.0F);
        this.shape48.addBox(0.0F, 0.0F, 0.0F, 1, 4, 8, 0.0F);
        this.Bag = new ModelRenderer(this, 20, 10);
        this.Bag.setRotationPoint(-4.3F, -3.5F, 11.0F);
        this.Bag.addBox(-5.5F, -1.5F, -3.0F, 10, 6, 4, 0.0F);
        this.setRotateAngle(Bag, -0.12566370614359174F, 0.0F, -0.06283185307179587F);
        this.EmptySlot4 = new ModelRenderer(this, 0, 5);
        this.EmptySlot4.setRotationPoint(5.5F, -0.5F, -0.5F);
        this.EmptySlot4.addBox(-1.5F, 0.0F, -1.0F, 3, 3, 2, 0.0F);
        this.EmptySlot1 = new ModelRenderer(this, 26, 20);
        this.EmptySlot1.setRotationPoint(-1.5F, -2.5F, -0.5F);
        this.EmptySlot1.addBox(-1.5F, 0.0F, -1.0F, 3, 3, 2, 0.0F);
        this.setRotateAngle(EmptySlot1, 0.0F, 0.0F, -0.12566370614359174F);
        this.Bandoulier = new ModelRenderer(this, 0, 0);
        this.Bandoulier.setRotationPoint(2.3F, 3.9F, -2.5F);
        this.Bandoulier.addBox(-3.0F, -2.0F, 0.0F, 12, 4, 1, 0.0F);
        this.setRotateAngle(Bandoulier, 0.0F, 0.0F, -1.1812388377497625F);
        this.shape23 = new ModelRenderer(this, 0, 25);
        this.shape23.setRotationPoint(-0.5F, -2.0F, -3.0F);
        this.shape23.addBox(-4.5F, 0.0F, 0.0F, 9, 1, 4, 0.0F);
        this.shape27_4 = new ModelRenderer(this, 46, 25);
        this.shape27_4.setRotationPoint(-0.2F, 0.0F, -0.2F);
        this.shape27_4.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        this.shape15 = new ModelRenderer(this, 26, 0);
        this.shape15.setRotationPoint(-3.0F, 2.0F, 0.0F);
        this.shape15.addBox(-12.0F, -4.0F, 0.0F, 12, 4, 1, 0.0F);
        this.setRotateAngle(shape15, 0.0F, 0.0F, 0.5592034923389831F);
        this.shape23_1 = new ModelRenderer(this, 0, 17);
        this.shape23_1.setRotationPoint(0.0F, 0.5F, 3.5F);
        this.shape23_1.addBox(-4.5F, 0.0F, 0.0F, 9, 2, 1, 0.0F);
        this.shape27_11 = new ModelRenderer(this, 16, 30);
        this.shape27_11.setRotationPoint(-0.2F, -0.7F, 0.0F);
        this.shape27_11.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        this.shape15_2 = new ModelRenderer(this, 38, 5);
        this.shape15_2.setRotationPoint(-12.0F, 0.0F, 0.0F);
        this.shape15_2.addBox(-4.0F, -4.0F, 0.0F, 4, 4, 1, 0.0F);
        this.setRotateAngle(shape15_2, 0.0F, 0.0F, 0.6283185307179586F);
        this.Nade2 = new ModelRenderer(this, 46, 28);
        this.Nade2.setRotationPoint(0.3F, -1.7F, 0.2F);
        this.Nade2.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(Nade2, 0.0F, 0.0F, 0.12566370614359174F);
        this.shape27_8 = new ModelRenderer(this, 32, 29);
        this.shape27_8.setRotationPoint(-0.4F, 0.0F, 0.0F);
        this.shape27_8.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        this.EmptySlot2 = new ModelRenderer(this, 48, 15);
        this.EmptySlot2.setRotationPoint(-0.5F, -2.8F, -0.5F);
        this.EmptySlot2.addBox(-1.5F, 0.0F, -1.0F, 3, 3, 2, 0.0F);
        this.setRotateAngle(EmptySlot2, 0.0F, 0.0F, -0.25132741228718347F);
        this.shape15_1 = new ModelRenderer(this, 12, 5);
        this.shape15_1.setRotationPoint(0.0F, 0.0F, 10.0F);
        this.shape15_1.addBox(-3.0F, -2.0F, -1.0F, 12, 4, 1, 0.0F);
        this.shape27_1 = new ModelRenderer(this, 48, 10);
        this.shape27_1.setRotationPoint(-4.5F, -2.5F, -0.5F);
        this.shape27_1.addBox(-1.5F, 0.0F, -1.0F, 3, 3, 2, 0.0F);
        this.shape27_10 = new ModelRenderer(this, 8, 30);
        this.shape27_10.setRotationPoint(-0.2F, 0.0F, 0.2F);
        this.shape27_10.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        this.shape27_3 = new ModelRenderer(this, 12, 10);
        this.shape27_3.setRotationPoint(-0.4F, 0.0F, 0.0F);
        this.shape27_3.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        this.Nade1 = new ModelRenderer(this, 46, 28);
        this.Nade1.mirror = true;
        this.Nade1.setRotationPoint(-0.1F, -1.7F, 0.2F);
        this.Nade1.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(Nade1, 0.0F, 0.0F, -0.06283185307179587F);
        this.shape27_6 = new ModelRenderer(this, 26, 27);
        this.shape27_6.setRotationPoint(-0.2F, -0.7F, 0.0F);
        this.shape27_6.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        this.shape27_9 = new ModelRenderer(this, 0, 30);
        this.shape27_9.setRotationPoint(-0.2F, 0.0F, -0.2F);
        this.shape27_9.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        this.shape53 = new ModelRenderer(this, 0, 20);
        this.shape53.setRotationPoint(-12.0F, -4.0F, 10.0F);
        this.shape53.addBox(0.0F, 0.0F, -1.0F, 12, 4, 1, 0.0F);
        this.shape26 = new ModelRenderer(this, 58, 5);
        this.shape26.setRotationPoint(0.0F, 0.0F, 1.2F);
        this.shape26.addBox(-0.5F, 0.0F, -1.0F, 1, 2, 1, 0.0F);
        this.setRotateAngle(shape26, -0.12566370614359174F, 0.0F, 0.0F);
        this.shape27_7 = new ModelRenderer(this, 54, 28);
        this.shape27_7.setRotationPoint(0.2F, 0.0F, 0.0F);
        this.shape27_7.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        this.EmptySlot3 = new ModelRenderer(this, 52, 0);
        this.EmptySlot3.setRotationPoint(1.5F, -0.5F, -0.5F);
        this.EmptySlot3.addBox(-1.5F, 0.0F, -1.0F, 3, 3, 2, 0.0F);
        this.shape50 = new ModelRenderer(this, 0, 5);
        this.shape50.setRotationPoint(-0.5F, 0.3F, 0.0F);
        this.shape50.addBox(0.0F, -1.0F, 0.0F, 1, 1, 0, 0.0F);
        this.bipedBody.addChild(this.Bandoulier);
        this.shape15_3.addChild(this.shape15_4);
        this.shape15.addChild(this.shape27);
        this.Nade1.addChild(this.shape27_2);
        this.shape27_11.addChild(this.shape50_1);
        this.shape23_1.addChild(this.shape23_2);
        this.shape53.addChild(this.shape15_3);
        this.Bag.addChild(this.shape22);
        this.shape27_2.addChild(this.shape27_5);
        this.Bandoulier.addChild(this.shape48);
        this.shape15.addChild(this.Bag);
        this.Bandoulier.addChild(this.EmptySlot4);
        this.shape15_2.addChild(this.EmptySlot1);
        this.Bag.addChild(this.shape23);
        this.shape27_2.addChild(this.shape27_4);
        this.Bandoulier.addChild(this.shape15);
        this.shape23.addChild(this.shape23_1);
        this.shape27_7.addChild(this.shape27_11);
        this.shape15.addChild(this.shape15_2);
        this.shape27_1.addChild(this.Nade2);
        this.shape27_7.addChild(this.shape27_8);
        this.shape15.addChild(this.EmptySlot2);
        this.Bandoulier.addChild(this.shape15_1);
        this.shape15.addChild(this.shape27_1);
        this.shape27_7.addChild(this.shape27_10);
        this.shape27_2.addChild(this.shape27_3);
        this.shape27.addChild(this.Nade1);
        this.shape27_2.addChild(this.shape27_6);
        this.shape27_7.addChild(this.shape27_9);
        this.shape15.addChild(this.shape53);
        this.shape23_2.addChild(this.shape26);
        this.Nade2.addChild(this.shape27_7);
        this.Bandoulier.addChild(this.EmptySlot3);
        this.shape27_6.addChild(this.shape50);
    }

    @Override
    public void render(@Nullable Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.Bandoulier.offsetX, this.Bandoulier.offsetY, this.Bandoulier.offsetZ);
        GlStateManager.translate(this.Bandoulier.rotationPointX * scale, this.Bandoulier.rotationPointY * scale, this.Bandoulier.rotationPointZ * scale);
        GlStateManager.scale(0.5D, 0.5D, 0.5D);
        GlStateManager.translate(-this.Bandoulier.offsetX, -this.Bandoulier.offsetY, -this.Bandoulier.offsetZ);
        GlStateManager.translate(-this.Bandoulier.rotationPointX * scale, -this.Bandoulier.rotationPointY * scale, -this.Bandoulier.rotationPointZ * scale);
        this.bipedBody.render(scale);
        GlStateManager.popMatrix();
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     * @param modelRenderer the model renderer being used
     * @param x a rotation angle
     * @param y a rotation angle
     * @param z a rotation angle
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
