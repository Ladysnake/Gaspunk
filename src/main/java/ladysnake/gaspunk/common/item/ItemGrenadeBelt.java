package ladysnake.gaspunk.common.item;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.common.Baubles;
import ladylib.misc.ItemUtil;
import ladysnake.gaspunk.common.GasPunk;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Optional.Interface(iface = "baubles.api.IBauble", modid = Baubles.MODID, striprefs = true)
public class ItemGrenadeBelt extends Item implements IBauble {

    public static final String NBT_TAG_BELT_STACK = GasPunk.MOD_ID + ":belt_equipped";

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
            player.inventoryContainer.addListener(new BeltInventoryListener(player));
        }
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.BELT;
    }

    @Override
    public void onEquipped(ItemStack itemstack, LivingEntity player) {
        if (player instanceof PlayerEntity)
            updateStackSize((PlayerEntity) player);
    }

    @Override
    public void onUnequipped(ItemStack itemstack, LivingEntity player) {
        if (player instanceof PlayerEntity)
            preventTomfoolery((PlayerEntity) player);
    }

    private void updateStackSize(PlayerEntity player) {
        List<ItemStack> inventory = new ArrayList<>(player.inventory.mainInventory);
        inventory.addAll(player.inventory.offHandInventory);
        for (ItemStack stack : inventory) {
            if (stack.getItem() instanceof ItemGrenade) {
                ItemUtil.getOrCreateCompound(stack).setBoolean(NBT_TAG_BELT_STACK, true);
            }
        }
    }

    private void preventTomfoolery(PlayerEntity player) {
        List<ItemStack> inventory = new ArrayList<>(player.inventory.mainInventory);
        inventory.addAll(player.inventory.offHandInventory);
        for (ItemStack stack : inventory) {
            if (stack.getItem() instanceof ItemGrenade) {
                CompoundTag nbt = stack.getTagCompound();
                if (nbt != null) {
                    nbt.removeTag(NBT_TAG_BELT_STACK);
                    while (stack.getCount() > 1) {
                        ItemStack split = stack.splitStack(1);
                        if (!player.addItemStackToInventory(split)) {
                            player.dropItem(split, false);
                        }
                    }
                }
            }
        }
    }

    public class BeltInventoryListener implements IContainerListener {

        private final ServerPlayerEntity owner;

        BeltInventoryListener(ServerPlayerEntity owner) {
            this.owner = owner;
        }

        @Override
        public void sendSlotContents(@Nonnull Container containerToSend, int slotInd, @Nonnull ItemStack stack) {
            if (stack.getItem() == GasPunkItems.GRENADE) {
                if (BaublesApi.isBaubleEquipped(owner, GasPunkItems.GRENADE_BELT) == -1) {
                    preventTomfoolery(owner);
                } else {
                    updateStackSize(owner);
                }
            }
        }

        @Override
        public void sendAllContents(@Nonnull Container containerToSend, @Nonnull DefaultedList<ItemStack> itemsList) {
            // NO-OP
        }

        @Override
        public void sendWindowProperty(@Nonnull Container containerIn, int varToUpdate, int newValue) {
            // NO-OP
        }

        @Override
        public void sendAllWindowProperties(@Nonnull Container containerIn, @Nonnull Inventory inventory) {
            // NO-OP
        }
    }
}
