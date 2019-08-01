package ladysnake.gaspunk.common.gas;

import IGasParticleType;
import IGasType;
import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.JsonAdapter;
import com.mojang.blaze3d.platform.GlStateManager;
import ladylib.client.shader.ShaderRegistryEvent;
import ladylib.client.shader.ShaderUtil;
import ladysnake.gaspunk.common.GasPunk;
import ladysnake.gaspunk.api.*;
import ladysnake.gaspunk.common.config.GasPunkConfig;
import ladysnake.gaspunk.common.gas.core.GasDeserializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import javax.annotation.Nullable;
import java.awt.image.ColorModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * An implementation of {@link IGas} that delegates its effect to one or more gas agents
 * @see IGasAgent
 */
@JsonAdapter(GasDeserializer.class)
public class Gas extends AbstractGas {
    public static final Identifier GAS_TEX_PATH = new Identifier(GasPunk.MOD_ID, "textures/gui/vapor_overlay.png");
    public static final Identifier NOISE_TEX_PATH = new Identifier(GasPunk.MOD_ID, "textures/gui/noise.png");
    public static final Identifier OVERLAY_SHADER = new Identifier(GasPunk.MOD_ID, "gas_overlay");

    static {
        try {
            Field f = AbstractGas.class.getDeclaredField("builderSupplier");
            f.setAccessible(true);
            f.set(null, (Supplier) Builder::new);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            GasPunk.LOGGER.error("Error while setting the builderSupplier in the api", e);
        }
    }

    /**
     * Called during {@link net.minecraftforge.fml.common.event.FMLPreInitializationEvent} to make sure the class is loaded
     */
    public static void classInit() { }

    @SubscribeEvent
    public static void onShaderRegistry(ShaderRegistryEvent event) {
        event.registerShader(OVERLAY_SHADER);
    }

    @SubscribeEvent
    @Environment(EnvType.CLIENT)
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        // gets every possible particle texture from registered gases and register them as sprites
        Gases.GAS_REGISTRY.getValuesCollection().stream()
                .map(IGas::getParticleType)
                .map(IGasParticleType::getParticleTexture)
                .distinct()
                .forEach(event.getMap()::registerSprite);
    }


    protected final IGasType type;
    protected final IGasParticleType particleType;
    protected int color;
    protected int bottleColor;
    protected ImmutableList<AgentEffect> agents;
    protected String[] tooltipLines;

    public Gas(IGasType type, int color, IGasAgent agent, float potency) {
        this(type, color, new AgentEffect(agent, potency));
    }

    public Gas(IGasType type, int color, AgentEffect... agents) {
        this(type, type.getParticleType(), color, color, ImmutableList.copyOf(agents), new String[0]);
    }

    public Gas(IGasType type, IGasParticleType particleType, int color, int bottleColor, ImmutableList<AgentEffect> agents, String[] tooltipLines) {
        this.type = type;
        this.particleType = particleType;
        this.color = color;
        this.bottleColor = bottleColor;
        this.agents = agents;
        this.tooltipLines = tooltipLines;
    }

    @Override
    public IGasType getType() {
        return type;
    }

    @Override
    public IGasParticleType getParticleType() {
        return particleType;
    }

    @Override
    public void applyEffect(LivingEntity entity, IBreathingHandler handler, float concentration, boolean firstTick, boolean forced) {
        agents.forEach(agent -> agent.getAgent().applyEffect(entity, handler, concentration, firstTick, agent.getPotency()));
    }

    @Override
    public void onExitCloud(LivingEntity entity, IBreathingHandler handler) {
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
    @Environment(EnvType.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, TooltipContext flagIn) {
        for (AgentEffect effect : agents) {
            tooltip.add(effect.getAgent().getLocalizedName());
        }
        for (String line : tooltipLines) {
            tooltip.add(I18n.translate(line));
        }
        if (flagIn.isAdvanced()) {
            tooltip.add(Formatting.DARK_GRAY + "" + this.getRegistryName());
        }
    }

    @Override
    public ImmutableList<AgentEffect> getAgents() {
        return agents;
    }

    @Override
    public String[] getTooltipLines() {
        return tooltipLines;
    }

    protected Identifier getOverlayTexture() {
        return GasPunkConfig.get().shouldUseShaders() ? NOISE_TEX_PATH : GAS_TEX_PATH;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void renderOverlay(float concentration, float partialTicks, ScaledResolution resolution) {
        GlStateManager.disableDepthTest();
        GlStateManager.depthMask(false);
        int color = getColor();
        float b = (color & 0xFF) / 255F;
        float g = (color >> 8 & 0xFF) / 255F;
        float r = (color >> 16 & 0xFF) / 255F;
        float a = (color >> 24 & 0xFF) / 255F;
        GlStateManager.color4f(r, g, b, concentration * a);
        GlStateManager.enableAlphaTest();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        MinecraftClient.getInstance().getTextureManager().bindTexture(getOverlayTexture());
        ShaderUtil.useShader(OVERLAY_SHADER);
        ShaderUtil.setUniform("gasColor", r, g, b, a);
        ShaderUtil.setUniform("iTime", (int) System.currentTimeMillis());
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, VertexFormats.POSITION_UV);
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

    public static final class Builder implements AbstractGas.Builder {
        private IGasType type;
        private IGasParticleType particleType;
        private int color;
        // nullable type so we know whether it was assigned or not
        private Integer bottleColor;
        private ImmutableList.Builder<AgentEffect> agents = ImmutableList.builder();
        private List<String> tooltipLines = new ArrayList<>();

        @Override
        public Builder setType(IGasType type) {
            this.type = type;
            return this;
        }

        @Override
        public Builder setParticleType(IGasParticleType type) {
            this.particleType = type;
            return this;
        }

        @Override
        public Builder setColor(int color) {
            this.color = color;
            return this;
        }

        @Override
        public Builder setBottleColor(int color) {
            this.bottleColor = color;
            return this;
        }

        @Override
        public Builder addAgent(IGasAgent agent, float potency) {
            agents.add(new AgentEffect(agent, potency));
            return this;
        }

        @Override
        public Builder addTooltipLine(String tooltipLine) {
            tooltipLines.add(tooltipLine);
            return this;
        }

        @Override
        public Gas build() {
            if (type == null) throw new IllegalStateException("gas type not provided");
            return new Gas(
                    type,
                    particleType == null ? type.getParticleType() : particleType,
                    color,
                    bottleColor == null ? color : bottleColor,
                    agents.build(),
                    tooltipLines.toArray(new String[0])
            );
        }
    }

}
