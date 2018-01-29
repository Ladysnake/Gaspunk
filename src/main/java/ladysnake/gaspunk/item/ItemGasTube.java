package ladysnake.gaspunk.item;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.entity.EntityGasCloud;
import ladysnake.gaspunk.gas.Gas;
import ladysnake.gaspunk.init.ModGases;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ItemGasTube extends Item {

    public static String NBT_CONTAINED_GAS = GasPunk.MOD_ID + ":contained_gas";

    public ItemGasTube() {
        super();
        this.setMaxStackSize(16);
        this.addPropertyOverride(new ResourceLocation(GasPunk.MOD_ID, "gas_type"),
                ((stack, worldIn, entityIn) -> getContainedGas(stack).getType().getId()));
    }

    public static Gas getContainedGas(ItemStack stack) {
        Gas ret = null;
        if (stack.hasTagCompound()) {
            ret = ModGases.REGISTRY.getValue(new ResourceLocation(Objects.requireNonNull(stack.getTagCompound()).getString(NBT_CONTAINED_GAS)));
        }
        return ret == null ? ModGases.VAPOR : ret;
    }

    public ItemStack getItemStackFor(Gas gas) {
        ItemStack stack = new ItemStack(this);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString(NBT_CONTAINED_GAS, Objects.requireNonNull(gas.getRegistryName(), "Can't use an unregistered gas in grenade").toString());
        stack.setTagCompound(nbt);
        return stack;
    }

    /*@Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        if (!worldIn.isRemote) {
            EntityGasTube tube = new EntityGasTube(worldIn, playerIn, stack);
            tube.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntity(tube);
            stack.shrink(1);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }*/

    public EntityGasCloud explode(WorldServer worldIn, Vec3d pos, ItemStack stack) {
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.x, pos.y, pos.z, 20, 0.5, 0.5, 0.5, 0.2);
        EntityGasCloud cloud = new EntityGasCloud(worldIn, getContainedGas(stack));
        cloud.setPosition(pos.x, pos.y, pos.z);
        cloud.setMaxLifespan(100);
        worldIn.spawnEntity(cloud);
        return cloud;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (tab == GasPunk.CREATIVE_TAB) {
            for (Gas gas : ModGases.REGISTRY.getValues()) {
                items.add(getItemStackFor(gas));
            }
        }
    }
}
