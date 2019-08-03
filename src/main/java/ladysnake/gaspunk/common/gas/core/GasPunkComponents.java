package ladysnake.gaspunk.common.gas.core;

import ladysnake.gaspunk.api.IBreathingHandler;
import ladysnake.gaspunk.common.GasPunk;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Npc;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class GasPunkComponents {

    public static final EntityAttribute MAX_AIR_SUPPLY = new ClampedEntityAttribute(null, GasPunk.MOD_ID + ".maxAirSupply", 300, 0, 1024).setName("Max Air Supply").setTracked(true);
    public static final ComponentType<IBreathingHandler> BREATHING = ComponentRegistry.INSTANCE.registerIfAbsent(GasPunk.id("breathing"), IBreathingHandler.class);

    public static void init() {
        EntityComponentCallback.event(LivingEntity.class).register((entity, components) -> {
            int maxAir;
            if (entity instanceof PlayerEntity) {
                maxAir = 300;
            } else if (entity instanceof Npc) {
                maxAir = 200;
            } else {
                maxAir = 40;
            }
            entity.getAttributeContainer().register(MAX_AIR_SUPPLY).setBaseValue(maxAir);
            IBreathingHandler instance;
            if (entity instanceof ServerPlayerEntity) {
                instance = new PlayerBreathingHandler((ServerPlayerEntity) entity);
            } else {
                instance = new DefaultBreathingHandler(entity);
            }
            components.put(BREATHING, instance);
        });
    }

}
