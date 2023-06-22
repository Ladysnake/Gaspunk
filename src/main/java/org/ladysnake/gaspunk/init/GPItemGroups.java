package org.ladysnake.gaspunk.init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.ladysnake.gaspunk.GasPunk;

public class GPItemGroups {

    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder().name(Text.translatable(Util.createTranslationKey("itemGroup", GasPunk.id("items")))).icon(() -> new ItemStack(GPItems.GAS_MASK)).entries((displayParameters, collector) -> {
        // gear
        collector.addItem(GPItems.GAS_MASK);
        collector.addItem(GPItems.GRENADE_BELT);

        // components
        collector.addItem(GPItems.GAS_TUBE);
        collector.addItem(GPItems.DIFFUSER);
        collector.addItem(GPItems.EMPTY_GRENADE);

        // grenades
        collector.addItem(GPItems.GAS_GRENADE);

        // materials
        collector.addItem(GPItems.ASH);
        collector.addItem(GPItems.SULFUR);
        collector.addItem(GPItems.SMOKE_POWDER);


    }).build();

}
