package org.ladysnake.gaspunk;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.ladysnake.gaspunk.init.GPEntities;
import org.ladysnake.gaspunk.init.GPItems;

public class GrenadeEntity extends ThrownItemEntity {

    //TODO proper flight behavior

    public GrenadeEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public GrenadeEntity(double d, double e, double f, World world) {
        super(GPEntities.GRENADE, d, e, f, world);
    }

    public GrenadeEntity(LivingEntity thrower, World world) {
        super(GPEntities.GRENADE, thrower, world);
    }

    @Override
    protected Item getDefaultItem() {
        return GPItems.GAS_GRENADE;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient()) {
            ItemStack itemStack = this.getStack();
            //TODO spawn gas cloud

            //TODO gas hissing sound?
            this.getWorld().syncWorldEvent(WorldEvents.SPLASH_POTION_SPLASHED, this.getBlockPos(), 0xF800F8);

            //TODO keep grenade alive for some time
            this.discard();
        }
    }

}
