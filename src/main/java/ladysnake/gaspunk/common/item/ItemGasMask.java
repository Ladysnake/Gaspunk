package ladysnake.gaspunk.common.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.Baubles;
import ladysnake.gaspunk.common.GasPunk;
import ladysnake.gaspunk.client.render.entity.model.ModelGasMask;
import mcjty.needtobreathe.NeedToBreathe;
import mcjty.needtobreathe.api.IProtectiveHelmet;
import mcjty.needtobreathe.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraftforge.fml.common.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import javax.annotation.Nullable;

@Optional.Interface(iface = "baubles.api.IBauble", modid = Baubles.MODID, striprefs = true)
@Optional.Interface(iface = "mcjty.needtobreathe.api.IProtectiveHelmet", modid = NeedToBreathe.MODID, striprefs = true)
public class ItemGasMask extends ArmorItem implements IBauble, IProtectiveHelmet {

    private static final Identifier GAS_MASK_TEX_PATH = new Identifier(GasPunk.MOD_ID, "textures/gui/gas_mask_overlay.png");

    public ItemGasMask(ItemArmor.ArmorMaterial materialIn, int renderIndexIn) {
        super(materialIn, renderIndexIn, EntityEquipmentSlot.HEAD);
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return GasPunk.MOD_ID + ":textures/entity/gasmask.png";
    }

    @Nullable
    @Override
    @Environment(EnvType.CLIENT)
    public BipedEntityModel getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, BipedEntityModel _default) {
        return ModelGasMask.MODEL;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void renderHelmetOverlay(ItemStack stack, PlayerEntity player, ScaledResolution resolution, float partialTicks) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableAlpha();
        MinecraftClient.getInstance().getTextureManager().bindTexture(GAS_MASK_TEX_PATH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, VertexFormats.POSITION_UV);
        bufferbuilder.pos(0.0D, (double) resolution.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
        bufferbuilder.pos((double) resolution.getScaledWidth(), (double) resolution.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos((double) resolution.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.HEAD;
    }


    @Override
    public boolean isActive(PlayerEntity entityPlayer) {
        return true;
    }

    @Override
    public int getReducedPoison(PlayerEntity entityPlayer, int poison) {
        return (int)((float)poison * Config.PROTECTIVE_HELMET_FACTOR);
    }
}
