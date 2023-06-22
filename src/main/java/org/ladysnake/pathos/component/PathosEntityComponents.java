package org.ladysnake.pathos.component;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.LivingEntity;
import org.ladysnake.pathos.api.SicknessHandler;

public class PathosEntityComponents implements EntityComponentInitializer {

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(LivingEntity.class, SicknessHandler.COMPONENT_KEY, SicknessHandlerImpl::new);
    }
}
