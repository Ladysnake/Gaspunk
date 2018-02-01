package ladysnake.gaspunk.item;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.render.IRenderBauble;
import ladysnake.gaspunk.init.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles", striprefs = true)
public class ItemGrenadeBelt extends Item implements IBauble {

    public static final String NBT_TAG_BELT_STACK = "gaspunk:belt_equipped";

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        event.player.inventoryContainer.addListener(new BeltInventoryListener((EntityPlayerMP) event.player));
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.BELT;
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
        if (player instanceof EntityPlayer)
            updateStackSize((EntityPlayer) player);
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
        if (player instanceof EntityPlayer)
            preventTomfoolery((EntityPlayer) player);
    }

    private void updateStackSize(EntityPlayer player) {
        List<ItemStack> inventory = new ArrayList<>(player.inventory.mainInventory);
        inventory.addAll(player.inventory.offHandInventory);
        for (ItemStack stack : inventory) {
            if (stack.getItem() instanceof ItemGrenade) {
                NBTTagCompound nbt = stack.getTagCompound();
                if (nbt == null) {
                    nbt = new NBTTagCompound();
                    stack.setTagCompound(nbt);
                }
                nbt.setBoolean(NBT_TAG_BELT_STACK, true);
            }
        }
    }

    private void preventTomfoolery(EntityPlayer player) {
        List<ItemStack> inventory = new ArrayList<>(player.inventory.mainInventory);
        inventory.addAll(player.inventory.offHandInventory);
        for (ItemStack stack : inventory) {
            if (stack.getItem() instanceof ItemGrenade) {
                NBTTagCompound nbt = stack.getTagCompound();
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

        private final EntityPlayerMP owner;

        BeltInventoryListener(EntityPlayerMP owner) {
            this.owner = owner;
        }

        @Override
        public void sendSlotContents(@Nonnull Container containerToSend, int slotInd, @Nonnull ItemStack stack) {
            if (stack.getItem() == ModItems.GRENADE) {
                if (BaublesApi.isBaubleEquipped(owner, ModItems.GRENADE_BELT) == -1) {
                    preventTomfoolery(owner);
                } else {
                    updateStackSize(owner);
                }
            }
        }

        @Override
        public void sendAllContents(@Nonnull Container containerToSend, @Nonnull NonNullList<ItemStack> itemsList) {
            // NO-OP
        }

        @Override
        public void sendWindowProperty(@Nonnull Container containerIn, int varToUpdate, int newValue) {
            // NO-OP
        }

        @Override
        public void sendAllWindowProperties(@Nonnull Container containerIn, @Nonnull IInventory inventory) {
            // NO-OP
        }
    }
}
