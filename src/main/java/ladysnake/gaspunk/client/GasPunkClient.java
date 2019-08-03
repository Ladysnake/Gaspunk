package ladysnake.gaspunk.client;

import com.google.common.collect.ImmutableSet;
import ladysnake.gaspunk.api.IGasParticleType;
import ladysnake.gaspunk.api.customization.GrenadeSkins;
import ladysnake.gaspunk.client.config.GasPunkClientConfig;
import ladysnake.gaspunk.client.config.GasPunkPremiumConfig;
import ladysnake.gaspunk.client.particle.ParticleGasSmoke;
import ladysnake.gaspunk.client.render.GasCloudRenderer;
import ladysnake.gaspunk.client.render.entity.LayerBelt;
import ladysnake.gaspunk.common.config.GasPunkConfig;
import ladysnake.gaspunk.common.entity.GasCloudEntity;
import ladysnake.gaspunk.common.entity.GrenadeEntity;
import ladysnake.gaspunk.common.item.GasPunkItems;
import ladysnake.gaspunk.common.item.ItemGasTube;
import ladysnake.gaspunk.common.util.SpecialRewardChecker;
import ladysnake.gaspunk.mixin.client.EntityRenderDispatcherAccessor;
import ladysnake.gaspunk.mixin.client.LivingEntityRendererAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.render.EntityRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.ParticlesOption;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.world.World;

import java.awt.*;
import java.util.UUID;

public final class GasPunkClient implements ClientModInitializer {

    public static final GasPunkClient INSTANCE = new GasPunkClient();

    private int particleCount = 0;

    @Override
    public void onInitializeClient() {
        GasPunkConfig.load(GasPunkClientConfig.class);
        MinecraftClient mc = MinecraftClient.getInstance();
        ColorProviderRegistry.ITEM.register(((stack, tintIndex) -> tintIndex == 0
                ? ItemGasTube.getContainedGas(stack).getBottleColor()
                : Color.WHITE.getRGB()), GasPunkItems.GAS_TUBE, GasPunkItems.GRENADE);
        if (FabricLoader.getInstance().isModLoaded("wearables")) {
            ((EntityRenderDispatcherAccessor) mc.getEntityRenderManager()).getModelRenderers().forEach((s, render) -> ((LivingEntityRendererAccessor)render).invokeAddFeature(new LayerBelt(render)));
        }
        EntityRendererRegistry.INSTANCE.register(GasCloudEntity.class, (manager, ctx) -> new GasCloudRenderer<>(manager));
        EntityRendererRegistry.INSTANCE.register(GrenadeEntity.class, (manager, ctx) -> new FlyingItemEntityRenderer<GrenadeEntity>(manager, ctx.getItemRenderer()));
    }

    public void makeSmoke(World world, double x, double y, double z, int color, int amount, int radX, int radY, IGasParticleType texture) {
        if (!world.isClient) return;
        float b = (color & 0xFF) / 255F;
        float g = (color >> 8 & 0xFF) / 255F;
        float r = (color >> 16 & 0xFF) / 255F;
        float a = (color >> 24 & 0xFF) / 255F;
        // no need to spawn invisible particles
        if (a == 0) return;

        for (int i = 0; i < amount; i++) {
            particleCount += world.random.nextInt(3);
            if (particleCount % (MinecraftClient.getInstance().options.particles == ParticlesOption.ALL ? 1 : 2 * MinecraftClient.getInstance().options.particles.getId()) == 0) {
                double posX = x + world.random.nextGaussian() * radX % radX;
                double posY = y + world.random.nextGaussian() * radY % radY;
                double posZ = z + world.random.nextGaussian() * radX % radX;
                ParticleGasSmoke particle = new ParticleGasSmoke(world, posX, posY, posZ, r, g, b, a, (float) (55 + 20 * world.random.nextGaussian()));
                particle.setSprite(texture.getParticleTexture());
                MinecraftClient.getInstance().particleManager.addParticle(particle);
            }
        }
    }

    /**
     * Called by {@link SpecialRewardChecker#retrieveSpecialRewards()} when the list of winners has been successfully retrieved
     */
    public static void onSpecialRewardsRetrieved() {
        UUID profileID = MinecraftClient.getInstance().getSession().getProfile().getId();
        // If the profile has been rewarded with one or more custom skins,
        // add the config option to choose which one will appear on new grenades
        if (SpecialRewardChecker.isSpecialPerson(profileID)) {
            ImmutableSet<GrenadeSkins> awardedSkins = SpecialRewardChecker.getRewards(profileID);
            // this guy has a special skin, don't hide it by default
            GrenadeSkins defaultSkin = awardedSkins.stream()
                    .filter(g -> g != GrenadeSkins.NONE)
                    .findAny()
                    .orElse(GrenadeSkins.NONE);
            GasPunkConfig.load(GasPunkPremiumConfig.class, c -> {
                c.setAvailableSkins(awardedSkins);
                if (c.getSelectedSkin() == null) {
                    c.setSelectedSkin(defaultSkin);
                }
                return c;
            });
        }
    }

}
