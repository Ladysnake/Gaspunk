package ladysnake.gaspunk.item;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.network.SpecialAwardChecker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemDiffuser extends Item {

    public static final String TAG_CUSTOM_SKIN = "grenade_skin";
    public static final ResourceLocation CUSTOM_SKIN_PROPERTY = new ResourceLocation(GasPunk.MOD_ID, "custom_skin");
    public static final IItemPropertyGetter CUSTOM_SKIN_GETTER = ((stack, worldIn, entityIn) ->
            stack.getTagCompound() != null ? stack.getTagCompound().getFloat(TAG_CUSTOM_SKIN) : 0F);

    public ItemDiffuser() {
        addPropertyOverride(CUSTOM_SKIN_PROPERTY, CUSTOM_SKIN_GETTER);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) {
            nbt = new NBTTagCompound();
            stack.setTagCompound(nbt);
        }
        if (SpecialAwardChecker.isModOffWinner(playerIn))
            nbt.setFloat(TAG_CUSTOM_SKIN, 1F);
    }
}
