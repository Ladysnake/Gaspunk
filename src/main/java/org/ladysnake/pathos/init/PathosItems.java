package org.ladysnake.pathos.init;

import net.minecraft.item.Item;
import org.ladysnake.pathos.Pathos;
import org.ladysnake.pathos.item.FilledSyringeItem;
import org.ladysnake.pathos.item.SyringeItem;

public class PathosItems {

    public static final Item SYRINGE = new SyringeItem(new Item.Settings().maxCount(8));
    public static final Item BLOOD_SYRINGE = new FilledSyringeItem(new Item.Settings().maxCount(1));

    //TODO: Add filled syringe for one of each sickness
}
