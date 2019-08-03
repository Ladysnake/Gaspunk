package ladysnake.gaspunk.common.entity;

import ladysnake.gaspunk.common.GasPunk;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;

public final class GasPunkEntities {

    public static final EntityType<GrenadeEntity> GRENADE = FabricEntityTypeBuilder.<GrenadeEntity>create(EntityCategory.MISC, GrenadeEntity::new).size(EntityDimensions.changing(0.25F, 0.25F)).build();
    public static final EntityType<GasCloudEntity> GAS_CLOUD = FabricEntityTypeBuilder.<GasCloudEntity>create(EntityCategory.MISC, GasCloudEntity::new).size(EntityDimensions.changing(6.0F, 0.5F)).setImmuneToFire().build();

    public static void init() {
        Registry.register(Registry.ENTITY_TYPE, GasPunk.id("gas_grenade"), GRENADE);
        Registry.register(Registry.ENTITY_TYPE, GasPunk.id("gas_cloud"), GAS_CLOUD);
    }
}
