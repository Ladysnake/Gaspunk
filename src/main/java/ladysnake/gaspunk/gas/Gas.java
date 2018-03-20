package ladysnake.gaspunk.gas;

import com.google.common.collect.ImmutableList;
import ladylib.client.ShaderRegistryEvent;
import ladylib.client.ShaderUtil;
import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.GasPunkConfig;
import ladysnake.gaspunk.api.*;
import ladysnake.gaspunk.init.ModGases;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.awt.image.ColorModel;
import java.util.List;

/**
 * An implementation of {@link IGas} that delegates its effect to one or more gas agents
 * @see IGasAgent
 */
@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
public class Gas extends IForgeRegistryEntry.Impl<IGas> implements IGas {
    public static final ResourceLocation GAS_TEX_PATH = new ResourceLocation(GasPunk.MOD_ID, "textures/gui/vapor_overlay.png");
    public static final ResourceLocation NOISE_TEX_PATH = new ResourceLocation(GasPunk.MOD_ID, "textures/gui/noise.png");
    public static final ResourceLocation OVERLAY_SHADER = new ResourceLocation(GasPunk.MOD_ID, "gas_overlay");

    @SubscribeEvent
    public static void onShaderRegistry(ShaderRegistryEvent event) {
        event.registerShader(OVERLAY_SHADER);
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        // gets every possible particle texture from registered gases and register them as sprites
        ModGases.REGISTRY.getValues().stream()
                .map(IGas::getParticleType)
                .map(IGasParticleType::getParticleTexture)
                .distinct()
                .forEach(event.getMap()::registerSprite);
    }

    protected final IGasType type;
    protected int color, bottleColor;
    protected ImmutableList<AgentEffect> agents;

    public Gas(IGasType type, int color, IGasAgent agent, float potency) {
        this(type, color, new AgentEffect(agent, potency));
    }

    public Gas(IGasType type, int color, AgentEffect... agents) {
        this(type, color, color, ImmutableList.copyOf(agents));
    }

    public Gas(IGasType type, int color, int bottleColor, ImmutableList<AgentEffect> agents) {
        this.type = type;
        this.color = color;
        this.bottleColor = bottleColor;
        this.agents = agents;
    }

    @Override
    public IGasType getType() {
        return type;
    }

    @Override
    public void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick) {
        agents.forEach(agent -> agent.getAgent().applyEffect(entity, handler, concentration, firstTick, agent.getPotency()));
    }

    @Override
    public void onExitCloud(EntityLivingBase entity, IBreathingHandler handler) {
        agents.forEach(agent -> agent.getAgent().onExitCloud(entity, handler));
    }

    /**
     * Returns the RGB value representing the color in the default sRGB
     * {@link ColorModel}.
     * (Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are
     * blue).
     *
     * @return the RGB value of the color in the default sRGB
     * <code>ColorModel</code>.
     * @see java.awt.Color#getRGB
     */
    @Override
    public int getColor() {
        return color;
    }

    @Override
    public int getBottleColor() {
        return bottleColor;
    }

    @Override
    public boolean isToxic() {
        return agents.stream().map(AgentEffect::getAgent).anyMatch(IGasAgent::isToxic);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        for (AgentEffect effect : agents)
            tooltip.add(effect.getAgent().getLocalizedName());
    }

    public ImmutableList<AgentEffect> getAgents() {
        return agents;
    }

    protected ResourceLocation getOverlayTexture() {
        return GasPunkConfig.client.useShaders ? NOISE_TEX_PATH : GAS_TEX_PATH;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderOverlay(float concentration, float partialTicks, ScaledResolution resolution) {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        int color = getColor();
        float b = (color & 0xFF) / 255F;
        float g = (color >> 8 & 0xFF) / 255F;
        float r = (color >> 16 & 0xFF) / 255F;
        float a = (color >> 24 & 0xFF) / 255F;
        GlStateManager.color(r, g, b, concentration * a);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Minecraft.getMinecraft().getTextureManager().bindTexture(getOverlayTexture());
        ShaderUtil.useShader(OVERLAY_SHADER);
        ShaderUtil.setUniform("gasColor", r, g, b, a);
        ShaderUtil.setUniform("iTime", (int) System.currentTimeMillis());
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, (double) resolution.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
        bufferbuilder.pos((double) resolution.getScaledWidth(), (double) resolution.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos((double) resolution.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        ShaderUtil.revert();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static class AgentEffect {
        private final IGasAgent agent;
        private final float potency;

        public AgentEffect(IGasAgent agent, float potency) {
            this.agent = agent;
            this.potency = potency;
        }

        public IGasAgent getAgent() {
            return agent;
        }

        public float getPotency() {
            return potency;
        }
    }

}
