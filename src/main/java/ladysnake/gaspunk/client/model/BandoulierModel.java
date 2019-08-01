package ladysnake.gaspunk.client.model;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.model.Cuboid;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

import javax.annotation.Nullable;

/**
 * BandoulierModel - Alien
 * Created using Tabula 5.1.0
 */
public class BandoulierModel<E extends LivingEntity> extends BipedEntityModel<E> {

    private Cuboid Bandoulier;

    public BandoulierModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        Cuboid shape15_4 = new Cuboid(this, 32, 31);
        shape15_4.setRotationPoint(-3.0F, 0.0F, -9.0F);
        shape15_4.addBox(-1.0F, -4.0F, 0.0F, 1, 4, 8, 0.0F);
        Cuboid shape27 = new Cuboid(this, 48, 5);
        shape27.setRotationPoint(-8.5F, -2.5F, -0.5F);
        shape27.addBox(-1.5F, 0.0F, -1.0F, 3, 3, 2, 0.0F);
        this.setRotateAngle(shape27, 0.0F, 0.0F, 0.06283185307179587F);
        Cuboid shape27_2 = new Cuboid(this, 0, 10);
        shape27_2.setRotationPoint(0.2F, 0.0F, 0.0F);
        shape27_2.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        Cuboid shape50_1 = new Cuboid(this, 0, 5);
        shape50_1.setRotationPoint(-0.5F, 0.3F, 0.0F);
        shape50_1.addBox(0.0F, -1.0F, 0.0F, 1, 1, 0, 0.0F);
        Cuboid shape23_2 = new Cuboid(this, 22, 25);
        shape23_2.setRotationPoint(0.0F, 2.0F, 0.0F);
        shape23_2.addBox(-4.0F, 0.0F, 0.0F, 8, 1, 1, 0.0F);
        Cuboid shape15_3 = new Cuboid(this, 23, 32);
        shape15_3.setRotationPoint(0.0F, 4.0F, 0.0F);
        shape15_3.addBox(-4.0F, -4.0F, -1.0F, 4, 4, 1, 0.0F);
        this.setRotateAngle(shape15_3, 0.0F, 0.0F, 0.6283185307179586F);
        Cuboid shape22 = new Cuboid(this, 36, 20);
        shape22.setRotationPoint(0.0F, 4.0F, 1.0F);
        shape22.addBox(-4.5F, 0.0F, -4.0F, 8, 1, 4, 0.0F);
        Cuboid shape27_5 = new Cuboid(this, 54, 25);
        shape27_5.setRotationPoint(-0.2F, 0.0F, 0.2F);
        shape27_5.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        Cuboid shape48 = new Cuboid(this, 2, 5);
        shape48.setRotationPoint(8.0F, -2.0F, 1.0F);
        shape48.addBox(0.0F, 0.0F, 0.0F, 1, 4, 8, 0.0F);
        Cuboid bag = new Cuboid(this, 20, 10);
        bag.setRotationPoint(-4.3F, -3.5F, 11.0F);
        bag.addBox(-5.5F, -1.5F, -3.0F, 10, 6, 4, 0.0F);
        this.setRotateAngle(bag, -0.12566370614359174F, 0.0F, -0.06283185307179587F);
        Cuboid emptySlot4 = new Cuboid(this, 0, 5);
        emptySlot4.setRotationPoint(5.5F, -0.5F, -0.5F);
        emptySlot4.addBox(-1.5F, 0.0F, -1.0F, 3, 3, 2, 0.0F);
        Cuboid emptySlot1 = new Cuboid(this, 26, 20);
        emptySlot1.setRotationPoint(-1.5F, -2.5F, -0.5F);
        emptySlot1.addBox(-1.5F, 0.0F, -1.0F, 3, 3, 2, 0.0F);
        this.setRotateAngle(emptySlot1, 0.0F, 0.0F, -0.12566370614359174F);
        this.Bandoulier = new Cuboid(this, 0, 0);
        this.Bandoulier.setRotationPoint(2.3F, 3.9F, -2.5F);
        this.Bandoulier.addBox(-3.0F, -2.0F, 0.0F, 12, 4, 1, 0.0F);
        this.setRotateAngle(Bandoulier, 0.0F, 0.0F, -1.1812388377497625F);
        Cuboid shape23 = new Cuboid(this, 0, 25);
        shape23.setRotationPoint(-0.5F, -2.0F, -3.0F);
        shape23.addBox(-4.5F, 0.0F, 0.0F, 9, 1, 4, 0.0F);
        Cuboid shape27_4 = new Cuboid(this, 46, 25);
        shape27_4.setRotationPoint(-0.2F, 0.0F, -0.2F);
        shape27_4.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        Cuboid shape15 = new Cuboid(this, 26, 0);
        shape15.setRotationPoint(-3.0F, 2.0F, 0.0F);
        shape15.addBox(-12.0F, -4.0F, 0.0F, 12, 4, 1, 0.0F);
        this.setRotateAngle(shape15, 0.0F, 0.0F, 0.5592034923389831F);
        Cuboid shape23_1 = new Cuboid(this, 0, 17);
        shape23_1.setRotationPoint(0.0F, 0.5F, 3.5F);
        shape23_1.addBox(-4.5F, 0.0F, 0.0F, 9, 2, 1, 0.0F);
        Cuboid shape27_11 = new Cuboid(this, 16, 30);
        shape27_11.setRotationPoint(-0.2F, -0.7F, 0.0F);
        shape27_11.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        Cuboid shape15_2 = new Cuboid(this, 38, 5);
        shape15_2.setRotationPoint(-12.0F, 0.0F, 0.0F);
        shape15_2.addBox(-4.0F, -4.0F, 0.0F, 4, 4, 1, 0.0F);
        this.setRotateAngle(shape15_2, 0.0F, 0.0F, 0.6283185307179586F);
        Cuboid nade2 = new Cuboid(this, 46, 28);
        nade2.setRotationPoint(0.3F, -1.7F, 0.2F);
        nade2.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(nade2, 0.0F, 0.0F, 0.12566370614359174F);
        Cuboid shape27_8 = new Cuboid(this, 32, 29);
        shape27_8.setRotationPoint(-0.4F, 0.0F, 0.0F);
        shape27_8.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        Cuboid emptySlot2 = new Cuboid(this, 48, 15);
        emptySlot2.setRotationPoint(-0.5F, -2.8F, -0.5F);
        emptySlot2.addBox(-1.5F, 0.0F, -1.0F, 3, 3, 2, 0.0F);
        this.setRotateAngle(emptySlot2, 0.0F, 0.0F, -0.25132741228718347F);
        Cuboid shape15_1 = new Cuboid(this, 12, 5);
        shape15_1.setRotationPoint(0.0F, 0.0F, 10.0F);
        shape15_1.addBox(-3.0F, -2.0F, -1.0F, 12, 4, 1, 0.0F);
        Cuboid shape27_1 = new Cuboid(this, 48, 10);
        shape27_1.setRotationPoint(-4.5F, -2.5F, -0.5F);
        shape27_1.addBox(-1.5F, 0.0F, -1.0F, 3, 3, 2, 0.0F);
        Cuboid shape27_10 = new Cuboid(this, 8, 30);
        shape27_10.setRotationPoint(-0.2F, 0.0F, 0.2F);
        shape27_10.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        Cuboid shape27_3 = new Cuboid(this, 12, 10);
        shape27_3.setRotationPoint(-0.4F, 0.0F, 0.0F);
        shape27_3.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        Cuboid nade1 = new Cuboid(this, 46, 28);
        nade1.mirror = true;
        nade1.setRotationPoint(-0.1F, -1.7F, 0.2F);
        nade1.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
        this.setRotateAngle(nade1, 0.0F, 0.0F, -0.06283185307179587F);
        Cuboid shape27_6 = new Cuboid(this, 26, 27);
        shape27_6.setRotationPoint(-0.2F, -0.7F, 0.0F);
        shape27_6.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        Cuboid shape27_9 = new Cuboid(this, 0, 30);
        shape27_9.setRotationPoint(-0.2F, 0.0F, -0.2F);
        shape27_9.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        Cuboid shape53 = new Cuboid(this, 0, 20);
        shape53.setRotationPoint(-12.0F, -4.0F, 10.0F);
        shape53.addBox(0.0F, 0.0F, -1.0F, 12, 4, 1, 0.0F);
        Cuboid shape26 = new Cuboid(this, 58, 5);
        shape26.setRotationPoint(0.0F, 0.0F, 1.2F);
        shape26.addBox(-0.5F, 0.0F, -1.0F, 1, 2, 1, 0.0F);
        this.setRotateAngle(shape26, -0.12566370614359174F, 0.0F, 0.0F);
        Cuboid shape27_7 = new Cuboid(this, 54, 28);
        shape27_7.setRotationPoint(0.2F, 0.0F, 0.0F);
        shape27_7.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        Cuboid emptySlot3 = new Cuboid(this, 52, 0);
        emptySlot3.setRotationPoint(1.5F, -0.5F, -0.5F);
        emptySlot3.addBox(-1.5F, 0.0F, -1.0F, 3, 3, 2, 0.0F);
        Cuboid shape50 = new Cuboid(this, 0, 5);
        shape50.setRotationPoint(-0.5F, 0.3F, 0.0F);
        shape50.addBox(0.0F, -1.0F, 0.0F, 1, 1, 0, 0.0F);
        this.body.addChild(this.Bandoulier);
        shape15_3.addChild(shape15_4);
        shape15.addChild(shape27);
        nade1.addChild(shape27_2);
        shape27_11.addChild(shape50_1);
        shape23_1.addChild(shape23_2);
        shape53.addChild(shape15_3);
        bag.addChild(shape22);
        shape27_2.addChild(shape27_5);
        this.Bandoulier.addChild(shape48);
        shape15.addChild(bag);
        this.Bandoulier.addChild(emptySlot4);
        shape15_2.addChild(emptySlot1);
        bag.addChild(shape23);
        shape27_2.addChild(shape27_4);
        this.Bandoulier.addChild(shape15);
        shape23.addChild(shape23_1);
        shape27_7.addChild(shape27_11);
        shape15.addChild(shape15_2);
        shape27_1.addChild(nade2);
        shape27_7.addChild(shape27_8);
        shape15.addChild(emptySlot2);
        this.Bandoulier.addChild(shape15_1);
        shape15.addChild(shape27_1);
        shape27_7.addChild(shape27_10);
        shape27_2.addChild(shape27_3);
        shape27.addChild(nade1);
        shape27_2.addChild(shape27_6);
        shape27_7.addChild(shape27_9);
        shape15.addChild(shape53);
        shape23_2.addChild(shape26);
        nade2.addChild(shape27_7);
        this.Bandoulier.addChild(emptySlot3);
        shape27_6.addChild(shape50);
    }

    @Override
    public void render(@Nullable E entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.pushMatrix();
        GlStateManager.translatef(this.Bandoulier.x, this.Bandoulier.y, this.Bandoulier.z);
        GlStateManager.translatef(this.Bandoulier.rotationPointX * scale, this.Bandoulier.rotationPointY * scale, this.Bandoulier.rotationPointZ * scale);
        GlStateManager.scaled(0.5D, 0.5D, 0.5D);
        GlStateManager.translatef(-this.Bandoulier.x, -this.Bandoulier.y, -this.Bandoulier.z);
        GlStateManager.translatef(-this.Bandoulier.rotationPointX * scale, -this.Bandoulier.rotationPointY * scale, -this.Bandoulier.rotationPointZ * scale);
        this.body.render(scale);
        GlStateManager.popMatrix();
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     * @param cuboid the model renderer being used
     * @param x a rotation angle
     * @param y a rotation angle
     * @param z a rotation angle
     */
    private void setRotateAngle(Cuboid cuboid, float x, float y, float z) {
        cuboid.rotationPointX = x;
        cuboid.rotationPointY = y;
        cuboid.rotationPointZ = z;
    }
}
