package org.ladysnake.pathos;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ladysnake.pathos.api.Sickness;
import org.ladysnake.pathos.api.SicknessEffect;
import org.ladysnake.pathos.init.PathosItems;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.registry.api.dynamic.DynamicMetaRegistry;

public class Pathos implements ModInitializer {

    public static final String MODID = "pathos";
    private static final Logger LOGGER = LogManager.getLogger("Pathos");

    public static final RegistryKey<Registry<Sickness>> SICKNESS_REGISTRY_KEY = RegistryKey.ofRegistry(id("sicknesses"));
    public static final RegistryKey<Registry<SicknessEffect>> SICKNESS_EFFECT_REGISTRY_KEY = RegistryKey.ofRegistry(id("sickness_effects"));

    public static final Registry<SicknessEffect> SICKNESS_EFFECT_REGISTRY = FabricRegistryBuilder.createSimple(SICKNESS_EFFECT_REGISTRY_KEY).buildAndRegister();

    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void onInitialize(ModContainer mod) {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.addBefore(Items.GLASS_BOTTLE, PathosItems.SYRINGE);
            entries.addItem(PathosItems.BLOOD_SYRINGE, ItemGroup.Visibility.SEARCH_TAB_ONLY);
        });

        DynamicMetaRegistry.registerSynced(SICKNESS_REGISTRY_KEY, Sickness.CODEC);

        ImmutableMap.<String, Item>builder()
                .put("syringe", PathosItems.SYRINGE)
                .put("blood_syringe", PathosItems.BLOOD_SYRINGE)
                .build().forEach((name, item) -> {
                    Identifier id = id(name);
                    Registry.register(Registries.ITEM, id, item);
                });
    }
}
