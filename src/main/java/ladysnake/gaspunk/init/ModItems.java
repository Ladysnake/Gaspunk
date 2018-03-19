package ladysnake.gaspunk.init;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.item.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@GameRegistry.ObjectHolder(GasPunk.MOD_ID)
@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
public final class ModItems {

    public static final Item ASH = Items.AIR;
    public static final SkinItem EMPTY_GRENADE = new SkinItem();
    public static final Item GAS_MASK = Items.AIR;
    public static final Item GAS_TUBE = Items.AIR;
    public static final Item GRENADE = Items.AIR;
    public static final Item GRENADE_BELT = new ItemGrenadeBelt();
    public static final Item SULFUR = Items.AIR;
    public static final Item SMOKE_POWDER = Items.AIR;

    private static final Set<Item> allItems = new HashSet<>();

    @SuppressWarnings("unchecked")
    private static <T extends Item> T name(T item, String name) {
        return (T) item.setUnlocalizedName(GasPunk.MOD_ID + "." + name).setRegistryName(GasPunk.MOD_ID, name);
    }

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (event.getName().toString().equals("minecraft:chests/nether_bridge")) {
            ResourceLocation loc = new ResourceLocation(GasPunk.MOD_ID, "inject/nether_bridge");
            LootEntry entry = new LootEntryTable(loc, 1, 1, new LootCondition[0], "gaspunk_sulfur_entry");
            LootPool pool = new LootPool(new LootEntry[]{entry}, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0, 1), "gaspunk_sulfur_pool");
            event.getTable().addPool(pool);
        }
    }

    @SubscribeEvent
    public static void addItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> reg = event.getRegistry();
        Collections.addAll(allItems,
                name(new Item(), "ash"),
                name(new SkinItem(), "diffuser"),
                name(EMPTY_GRENADE, "empty_grenade"),
                name(new ItemGasMask(ItemArmor.ArmorMaterial.LEATHER, 0), "gas_mask"),
                name(new ItemGasTube(), "gas_tube"),
                name(new ItemGrenade(), "grenade"),
                name(new Item(), "smoke_powder"),
                name(new Item(), "sulfur")
        );
        if (Loader.isModLoaded("baubles")) {
            allItems.add(name(GRENADE_BELT, "grenade_belt"));
            MinecraftForge.EVENT_BUS.register(GRENADE_BELT);
        }
        for (Item item : allItems) {
            item.setCreativeTab(GasPunk.CREATIVE_TAB);
            reg.register(item);
        }
        ladysnake.pathos.item.ModItems.EMPTY_SYRINGE.setCreativeTab(GasPunk.CREATIVE_TAB);
        ladysnake.pathos.item.ModItems.FILLED_SYRINGE.setCreativeTab(GasPunk.CREATIVE_TAB);
    }

    public static void registerOres() {
        OreDictionary.registerOre("dustSulfur", SULFUR);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event) {
        allItems.forEach(ModItems::registerRender);
    }

    @SideOnly(Side.CLIENT)
    private static void registerRender(Item item) {
        registerRender(item, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()).toString()));
    }

    @SideOnly(Side.CLIENT)
    private static void registerRender(Item item, ModelResourceLocation loc) {
        ModelLoader.setCustomModelResourceLocation(item, 0, loc);
    }

    @SubscribeEvent
    public static void onRegistryMissingMappings(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getMappings()) {
            if (mapping.key.equals(new ResourceLocation("gaspunk:glass_tube")))
                mapping.remap(Items.GLASS_BOTTLE);
        }
    }
}
