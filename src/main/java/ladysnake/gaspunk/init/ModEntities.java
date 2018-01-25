package ladysnake.gaspunk.init;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.client.render.RenderGasTube;
import ladysnake.gaspunk.client.render.RenderGasCloud;
import ladysnake.gaspunk.entity.EntityGasCloud;
import ladysnake.gaspunk.entity.EntityGasTube;
import ladysnake.gaspunk.entity.EntityGrenade;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
                        .entity(EntityGasTube.class)
                        .factory(EntityGasTube::new)
                        .name("gas_tube")
                        .id("gas_tube", id++)
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
    @SideOnly(Side.CLIENT)
    public static void onModelRegistry(ModelRegistryEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityGasCloud.class, RenderGasCloud::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityGasTube.class, manager -> new RenderGasTube(manager, ModItems.GAS_TUBE, Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityGrenade.class, manager -> new RenderGasTube(manager, ModItems.GRENADE, Minecraft.getMinecraft().getRenderItem()));
    }
}
