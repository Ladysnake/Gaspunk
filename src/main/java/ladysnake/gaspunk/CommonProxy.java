package ladysnake.gaspunk;

import ladysnake.gaspunk.api.IGasParticleType;
import ladysnake.gaspunk.api.customization.GrenadeSkins;
import ladysnake.gaspunk.compat.BaublesCompatHandler;
import ladysnake.gaspunk.entity.EntityGrenade;
import ladysnake.gaspunk.gas.core.CapabilityBreathing;
import ladysnake.gaspunk.init.ModItems;
import ladysnake.gaspunk.network.PacketHandler;
import ladysnake.gaspunk.util.SpecialRewardChecker;
import ladysnake.pathos.capability.CapabilitySickness;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;

public class CommonProxy {
    protected Configuration config;

    public void preInit(FMLPreInitializationEvent event) {
        CapabilityBreathing.register();
        CapabilitySickness.register();
        config = new Configuration(event.getSuggestedConfigurationFile());
    }

    public void init() {
        PacketHandler.initPackets();
        if (GasPunkConfig.alternativeAshRecipe)
            GameRegistry.addSmelting(Items.NETHER_WART, new ItemStack(ModItems.ASH), 0.8f);
        else
            GameRegistry.addSmelting(Items.ROTTEN_FLESH, new ItemStack(ModItems.ASH), 0.35f);
        if (Loader.isModLoaded("baubles"))
            MinecraftForge.EVENT_BUS.register(new BaublesCompatHandler());
        new Thread(SpecialRewardChecker::retrieveSpecialRewards).start();
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.GRENADE, new BehaviorProjectileDispense() {
            @Nonnull
            @Override
            protected IProjectile getProjectileEntity(@Nonnull World worldIn, @Nonnull IPosition position, @Nonnull ItemStack stackIn) {
                return new EntityGrenade(worldIn, stackIn.copy(), position.getX(), position.getY(), position.getZ());
            }
        });
    }

    public void postInit() {
        // NO-OP
    }

    public void makeSmoke(World world, double x, double y, double z, int color, int amount, int radX, int radY, IGasParticleType texture) {
        // NO-OP
    }

    /**
     * Called by {@link SpecialRewardChecker#retrieveSpecialRewards()} when the list of winners has been successfully retrieved
     */
    public void onSpecialRewardsRetrieved() {
        // NO-OP
    }

    public GrenadeSkins getSelectedSkin() {
        return GrenadeSkins.NONE;
    }

}
