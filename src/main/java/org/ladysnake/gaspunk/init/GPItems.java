package org.ladysnake.gaspunk.init;

import net.minecraft.item.Item;
import org.ladysnake.gaspunk.item.GrenadeItem;

public class GPItems {

    //TODO dispenser behaviour for grenades
    //TODO item tags
    //TODO sulfur in chests/nether_bridge loot table

    // grenade components
    public static final Item DIFFUSER = new Item(new Item.Settings().maxCount(16));
    public static final Item EMPTY_GRENADE = new Item(new Item.Settings().maxCount(16));
    public static final Item GAS_TUBE = new Item(new Item.Settings().maxCount(16));

    // raw ingredients
    public static final Item ASH = new Item(new Item.Settings());
    public static final Item SULFUR = new Item(new Item.Settings());
    public static final Item SMOKE_POWDER = new Item(new Item.Settings());

    // equipment
    public static final Item GAS_GRENADE = new GrenadeItem(new Item.Settings().maxCount(16));
    public static final Item GAS_MASK = new Item(new Item.Settings().maxCount(1));

    //TODO only with trinkets
    public static final Item GRENADE_BELT = new Item(new Item.Settings().maxCount(1));
}
