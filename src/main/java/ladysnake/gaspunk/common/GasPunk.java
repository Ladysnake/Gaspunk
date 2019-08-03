package ladysnake.gaspunk.common;

import com.google.common.base.Preconditions;
import ladysnake.gaspunk.api.customization.Skinnable;
import ladysnake.gaspunk.common.compat.WearablesCompatHandler;
import ladysnake.gaspunk.common.config.GasPunkCommonConfig;
import ladysnake.gaspunk.common.config.GasPunkConfig;
import ladysnake.gaspunk.common.entity.GasPunkEntities;
import ladysnake.gaspunk.common.entity.GrenadeEntity;
import ladysnake.gaspunk.common.gas.Gas;
import ladysnake.gaspunk.common.gas.GasAgents;
import ladysnake.gaspunk.common.gas.Gases;
import ladysnake.gaspunk.common.gas.core.CapabilityBreathing;
import ladysnake.gaspunk.common.item.GasPunkItems;
import ladysnake.gaspunk.common.network.PacketHandler;
import ladysnake.gaspunk.common.util.SpecialRewardChecker;
import ladysnake.pathos.capability.CapabilitySickness;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.impl.itemgroup.ItemGroupExtensions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.projectile.Projectile;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public final class GasPunk implements ModInitializer {

    public static final String MOD_ID = "gaspunk";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final GasPunk INSTANCE = new GasPunk();

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    private ItemGroup gaspunkGroup;

    public ItemGroup getItemGroup() {
        Preconditions.checkState(gaspunkGroup != null, "Gaspunk item group got queried too early!");
        return this.gaspunkGroup;
    }

    @Override
    public void onInitialize() {
        GasPunkConfig.load(GasPunkCommonConfig.class);
        this.createItemGroup();
        GasPunkEntities.init();
        GasAgents.init();
        Gases.init();
        Gas.classInit();
        CapabilityBreathing.register();
        CapabilitySickness.register();
        PacketHandler.initPackets();
        new Thread(SpecialRewardChecker::retrieveSpecialRewards).start();
        if (FabricLoader.getInstance().isModLoaded("wearables")) {
            WearablesCompatHandler.init();
        }
        DispenserBlock.registerBehavior(GasPunkItems.GRENADE, new ProjectileDispenserBehavior() {
            @Nonnull
            @Override
            protected Projectile createProjectile(@Nonnull World worldIn, @Nonnull Position position, @Nonnull ItemStack stackIn) {
                return new GrenadeEntity(GasPunkEntities.GRENADE, worldIn, stackIn.copy(), position.getX(), position.getY(), position.getZ());
            }
        });
    }

    private void createItemGroup() {
        // Internal method, may break awfully in the future
        ((ItemGroupExtensions) ItemGroup.BUILDING_BLOCKS).fabric_expandArray();
        this.gaspunkGroup = new GasPunkItemGroup(ItemGroup.GROUPS.length - 1);
    }

    public static class GasPunkItemGroup extends ItemGroup {
        GasPunkItemGroup(int index) {
            super(index, "gaspunk.gases");
        }

        @Nonnull
        @Environment(EnvType.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(GasPunkItems.GRENADE);
        }

        @Override
        public void appendStacks(@Nonnull DefaultedList<ItemStack> itemList) {
            super.appendStacks(itemList);
            // set the skin of every item that supports it to the one currently selected
            itemList.stream()
                    .filter(stack -> stack.getItem() instanceof Skinnable)
                    .forEach(stack -> ((Skinnable)stack.getItem()).setSkin(stack, SpecialRewardChecker.getSelectedSkin(MinecraftClient.getInstance().player)));
        }
    }
}
