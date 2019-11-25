package ladysnake.gaspunk.client.render.entity;

import javax.annotation.Nullable;

import baubles.api.BaublesApi;
import baubles.api.render.IRenderBauble;
import ladysnake.gaspunk.client.model.ModelBandoulier;
import ladysnake.gaspunk.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerBelt implements LayerRenderer<EntityPlayer> {

    private ModelBandoulier bandoulier = new ModelBandoulier();

    @Override
    public void doRenderLayer(@Nullable EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (player != null && BaublesApi.isBaubleEquipped(player, ModItems.GRENADE_BELT) >= 0) {
            IRenderBauble.Helper.rotateIfSneaking(player);
            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("gaspunk:textures/entity/grenade_belt.png"));
            bandoulier.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
