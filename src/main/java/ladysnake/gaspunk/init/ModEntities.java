package ladysnake.gaspunk.init;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.client.render.RenderGasCloud;
import ladysnake.gaspunk.client.render.RenderGrenade;
import ladysnake.gaspunk.entity.EntityGasCloud;
import ladysnake.gaspunk.entity.EntityGrenade;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
public final class ModEntities {

    @SubscribeEvent
    public static void addEntities(RegistryEvent.Register<EntityEntry> event) {
        int id = 0;
        event.getRegistry().registerAll(
                EntityEntryBuilder.create()
                        .entity(EntityGrenade.class)
                        .factory(EntityGrenade::new)
                        .name("gas_grenade")
                        .id("gas_grenade", id++)
                        .tracker(64, 1, true)
                        .build(),

                EntityEntryBuilder.create()
                        .entity(EntityGasCloud.class)
                        .factory(EntityGasCloud::new)
                        .name("gas_cloud")
                        .id("gas_cloud", id)
                        .tracker(64, 1, true)
                        .build()
        );
    }

    @SubscribeEvent
    public static void onRegistryMissingMappings(RegistryEvent.MissingMappings<EntityEntry> event) {
        for (RegistryEvent.MissingMappings.Mapping<EntityEntry> mapping : event.getMappings()) {
            if (mapping.key.equals(new ResourceLocation("gaspunk:gas_tube")))
                mapping.remap(Objects.requireNonNull(ForgeRegistries.ENTITIES.getValue(new ResourceLocation("gaspunk:gas_grenade"))));
        }
    }


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegistry(ModelRegistryEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityGasCloud.class, RenderGasCloud::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityGrenade.class, manager -> new RenderGrenade(manager, ModItems.GRENADE, Minecraft.getMinecraft().getRenderItem()));
    }
}
