package ladysnake.gaspunk.entity;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.init.ModItems;
import ladysnake.gaspunk.item.ItemGasTube;
import ladysnake.gaspunk.item.ItemGrenade;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;

public class EntityGasTube extends EntityThrowable {
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityGrenade.class, DataSerializers.ITEM_STACK);

    public EntityGasTube(World worldIn) {
        super(worldIn);
    }

    public EntityGasTube(World worldIn, EntityLivingBase throwerIn, ItemStack stack) {
        super(worldIn, throwerIn);
        setItem(stack);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        getDataManager().register(ITEM, new ItemStack(ModItems.GAS_TUBE));
    }

    @Nonnull
    public ItemStack getGrenade() {
        ItemStack itemstack = this.getDataManager().get(ITEM);

        if (!(itemstack.getItem() instanceof ItemGasTube)) {
            if (this.world != null) {
                GasPunk.LOGGER.error("{} {} has no item?!", this.getClass().getSimpleName(), this.getEntityId());
            }

            return new ItemStack(ModItems.GAS_TUBE);
        } else {
            return itemstack;
        }
    }

    public void setItem(ItemStack item) {
        getDataManager().set(ITEM, item);
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult result) {
        explode();
    }

    protected void explode() {
        if (this.world instanceof WorldServer) {
            ((ItemGasTube) this.getGrenade().getItem()).explode((WorldServer) world, this.getPositionVector(), getGrenade());
            this.setDead();
        }
    }
}
