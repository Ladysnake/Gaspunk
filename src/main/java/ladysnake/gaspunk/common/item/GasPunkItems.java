package ladysnake.gaspunk.common.item;

import ladylib.LadyLib;
import ladylib.registration.AutoRegister;
import ladylib.registration.ItemRegistrar;
import ladysnake.gaspunk.common.GasPunk;
import ladysnake.gaspunk.common.item.*;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.Identifier;
import net.minecraft.world.loot.LootPool;
import net.minecraft.world.loot.UniformLootTableRange;
import net.minecraft.world.loot.entry.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings("unused")
@AutoRegister(GasPunk.MOD_ID)
@GameRegistry.ObjectHolder(GasPunk.MOD_ID)
@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
public final class GasPunkItems {

    public static final Item ASH = new Item();
    public static final Item DIFFUSER = new SkinItem();
    public static final Item EMPTY_GRENADE = new SkinItem().setContainerItem(Items.GLASS_BOTTLE);
    public static final Item GAS_MASK = new ItemGasMask(ItemArmor.ArmorMaterial.LEATHER, 0);
    public static final Item GAS_TUBE = new ItemGasTube();
    public static final Item GRENADE = new ItemGrenade();
    @AutoRegister.Ignore    // needs to be registered conditionally
    public static final Item GRENADE_BELT = new ItemGrenadeBelt();
    public static final Item SMOKE_POWDER = new Item();
    @AutoRegister.Ore("dustSulfur")
    public static final Item SULFUR = new Item();

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (event.getName().toString().equals("minecraft:chests/nether_bridge")) {
            Identifier loc = new Identifier(GasPunk.MOD_ID, "inject/nether_bridge");
            LootEntry entry = new LootEntryTable(loc, 1, 1, new LootCondition[0], "gaspunk_sulfur_entry");
            LootPool pool = new LootPool(new LootEntry[]{entry}, new LootCondition[0], new UniformLootTableRange(1), new UniformLootTableRange(0, 1), "gaspunk_sulfur_pool");
            event.getTable().addPool(pool);
        }
    }

    @SubscribeEvent
    public static void addItems(RegistryEvent.Register<Item> event) {
        if (Loader.isModLoaded("baubles")) {
            LadyLib.instance.getItemRegistrar().addItem(ItemRegistrar.name(GRENADE_BELT, "grenade_belt"), true);
            MinecraftForge.EVENT_BUS.register(GRENADE_BELT);
        }
        ladysnake.pathos.item.ModItems.EMPTY_SYRINGE.setCreativeTab(GasPunk.CREATIVE_TAB);
        ladysnake.pathos.item.ModItems.FILLED_SYRINGE.setCreativeTab(GasPunk.CREATIVE_TAB);
    }

    @SubscribeEvent
    public static void onRegistryMissingMappings(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getMappings()) {
            if (mapping.key.equals(new Identifier("gaspunk:glass_tube")))
                mapping.remap(Items.GLASS_BOTTLE);
        }
    }
}
