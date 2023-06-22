package org.ladysnake.gaspunk;

import com.google.common.collect.ImmutableMap;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.ladysnake.gaspunk.init.GPEntities;
import org.ladysnake.gaspunk.init.GPItemGroups;
import org.ladysnake.gaspunk.init.GPItems;
import org.ladysnake.gaspunk.init.GPSicknesses;
import org.ladysnake.pathos.api.Sickness;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class GasPunk implements ModInitializer {

    public static final String MODID = "gaspunk";

    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }

    @Override
    public void onInitialize(ModContainer mod) {
        MidnightConfig.init(MODID, GPConfig.class);
        Registry.register(Registries.ITEM_GROUP, id("items"), GPItemGroups.ITEM_GROUP);

        ImmutableMap.<String, Item>builder()
                .put("diffuser", GPItems.DIFFUSER)
                .put("empty_grenade", GPItems.EMPTY_GRENADE)
                .put("gas_tube", GPItems.GAS_TUBE)
                .put("ash", GPItems.ASH)
                .put("sulfur", GPItems.SULFUR)
                .put("smoke_powder", GPItems.SMOKE_POWDER)
                .put("gas_mask", GPItems.GAS_MASK)
                .put("grenade_belt", GPItems.GRENADE_BELT)
                .put("gas_grenade", GPItems.GAS_GRENADE)
                .build().forEach((name, item) -> {
                    Identifier id = id(name);
                    Registry.register(Registries.ITEM, id, item);
                });

        ImmutableMap.<String, Sickness>builder()
                .put("tear_gas", GPSicknesses.TEAR_GAS)
                .put("sarin_gas", GPSicknesses.SARIN_GAS)
                .build().forEach((name, sickness) -> {
                    Identifier id = id(name);
                    Registry.register(Sickness.REGISTRY, id, sickness);
                });

        ImmutableMap.<String, EntityType<?>>builder()
                .put("grenade", GPEntities.GRENADE)
                .build().forEach((name, entity) -> {
                    Identifier id = id(name);
                    Registry.register(Registries.ENTITY_TYPE, id, entity);
                });
    }
}
