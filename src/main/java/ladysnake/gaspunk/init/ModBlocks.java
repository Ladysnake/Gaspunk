package ladysnake.gaspunk.init;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.block.BlockGasFactory;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Function;

@GameRegistry.ObjectHolder(GasPunk.MOD_ID)
@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
public final class ModBlocks {

    public static final Block GAS_FACTORY = Blocks.AIR;

    @SuppressWarnings("unchecked")
    private static <T extends Block> T name(T block, String name) {
        return (T) block.setUnlocalizedName(name).setRegistryName(GasPunk.MOD_ID, name);
    }

    @SubscribeEvent
    public static void addBlocks(RegistryEvent.Register<Block> event) {
        registerBlocks(event.getRegistry(),
                name(new BlockGasFactory(), "gas_factory"));
    }

    private static void registerBlocks(IForgeRegistry<Block> blockRegistry, Block... blocks) {
        for(Block b : blocks)
            registerBlock(blockRegistry, b);
    }

    private static void registerBlock(IForgeRegistry<Block> blockRegistry, Block block) {
        registerBlock(blockRegistry, block, ItemBlock::new);
    }

    private static void registerBlock(IForgeRegistry<Block> blockRegistry, Block block, Function<Block, Item> blockItemFunction) {
        blockRegistry.register(block);
        assert block.getRegistryName() != null;
        Item item = blockItemFunction.apply(block).setRegistryName(block.getRegistryName());
        ModItems.allItems.add(item);
        block.setCreativeTab(GasPunk.CREATIVE_TAB);
    }
}
