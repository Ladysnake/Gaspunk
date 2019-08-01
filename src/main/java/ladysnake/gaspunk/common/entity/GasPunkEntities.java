package ladysnake.gaspunk.common.entity;

import ladysnake.gaspunk.common.GasPunk;
import ladysnake.gaspunk.client.render.RenderGasCloud;
import ladysnake.gaspunk.client.render.RenderGrenade;
import ladysnake.gaspunk.common.item.GasPunkItems;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
public final class GasPunkEntities {

    public static final EntityType<GrenadeEntity> GRENADE = FabricEntityTypeBuilder.<GrenadeEntity>create(EntityCategory.MISC, GrenadeEntity::new).build();
    public static final EntityType<GasCloudEntity> GAS_CLOUD = FabricEntityTypeBuilder.<GasCloudEntity>create(EntityCategory.MISC, GasCloudEntity::new).build();

    @SubscribeEvent
    public static void addEntities(RegistryEvent.Register<EntityEntry> event) {
        int id = 0;
        event.getRegistry().registerAll(
                EntityEntryBuilder.create()
                        .entity(GrenadeEntity.class)
                        .factory(GrenadeEntity::new)
                        .name("gas_grenade")
                        .id("gas_grenade", id++)
                        .tracker(64, 1, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(GasCloudEntity.class)
                        .factory(GasCloudEntity::new)
                        .name("gas_cloud")
                        .id("gas_cloud", id)
                        .tracker(64, 1, true)
                        .build()
        );
    }

    @SubscribeEvent
    public static void onRegistryMissingMappings(RegistryEvent.MissingMappings<EntityEntry> event) {
        for (RegistryEvent.MissingMappings.Mapping<EntityEntry> mapping : event.getMappings()) {
            if (mapping.key.equals(new Identifier("gaspunk:gas_tube")))
                mapping.remap(Objects.requireNonNull(ForgeRegistries.ENTITIES.getValue(new Identifier("gaspunk:gas_grenade"))));
        }
    }


    @SubscribeEvent
    @Environment(EnvType.CLIENT)
    public static void onModelRegistry(ModelRegistryEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(GasCloudEntity.class, RenderGasCloud::new);
        RenderingRegistry.registerEntityRenderingHandler(GrenadeEntity.class, manager -> new RenderGrenade(manager, GasPunkItems.GRENADE, MinecraftClient.getInstance().getRenderItem()));
    }
}
