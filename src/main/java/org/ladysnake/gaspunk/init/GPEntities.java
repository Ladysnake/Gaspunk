package org.ladysnake.gaspunk.init;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import org.ladysnake.gaspunk.GrenadeEntity;
import org.quiltmc.qsl.entity.api.QuiltEntityTypeBuilder;

public class GPEntities {

    public static final EntityType<GrenadeEntity> GRENADE = QuiltEntityTypeBuilder.<GrenadeEntity>create()
            .entityFactory(GrenadeEntity::new)
            .setDimensions(EntityDimensions.fixed(0.25F, 0.25F))
            .alwaysUpdateVelocity(true)
            .build();
}
