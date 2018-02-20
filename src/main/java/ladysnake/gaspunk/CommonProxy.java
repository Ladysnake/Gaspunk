package ladysnake.gaspunk;

import ladysnake.gaspunk.compat.BaublesCompatHandler;
import ladysnake.gaspunk.entity.EntityGrenade;
import ladysnake.gaspunk.gas.core.CapabilityBreathing;
import ladysnake.gaspunk.gas.core.IGas;
import ladysnake.gaspunk.init.ModGases;
import ladysnake.gaspunk.init.ModItems;
import ladysnake.gaspunk.network.PacketHandler;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import ladysnake.gaspunk.network.SpecialAwardChecker;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;

public class CommonProxy {

    public void preInit() {
        CapabilityBreathing.register();
    }

    public void init() {
        PacketHandler.initPackets();
        if (Configuration.alternativeAshRecipe)
            GameRegistry.addSmelting(Items.NETHER_WART, new ItemStack(ModItems.ASH), 0.8f);
        else
            GameRegistry.addSmelting(Items.ROTTEN_FLESH, new ItemStack(ModItems.ASH), 0.35f);
        ModGases.initRecipes();
        if (Loader.isModLoaded("baubles"))
            MinecraftForge.EVENT_BUS.register(new BaublesCompatHandler());
        new Thread(SpecialAwardChecker::retrieveModOffWinners).start();
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.GRENADE, new BehaviorProjectileDispense() {
            @Nonnull
            @Override
            protected IProjectile getProjectileEntity(@Nonnull World worldIn, @Nonnull IPosition position, @Nonnull ItemStack stackIn) {
                return new EntityGrenade(worldIn, null, stackIn);
            }
        });
    }

    public void postInit() {

    }

    public void makeSmoke(World world, double x, double y, double z, int color, int amount, int radX, int radY, IGas.ParticleTypes texture) {

    }

}
