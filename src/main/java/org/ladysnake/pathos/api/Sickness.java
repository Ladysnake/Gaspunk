package org.ladysnake.pathos.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.world.World;
import org.ladysnake.pathos.Pathos;

import java.util.List;

public record Sickness(List<SicknessEffect> effects, SeverityFunction severityFunction, float initialSeverity) {

    public static Registry<Sickness> getRegistry(World world) {
        return world.getRegistryManager().get(Pathos.SICKNESS_REGISTRY_KEY);
    }

    public static final Codec<Sickness> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(SicknessEffect.REGISTRY.getCodec()).fieldOf("effects").forGetter(Sickness::effects),
            SeverityFunction.CODEC.fieldOf("severity_function").forGetter(Sickness::severityFunction),
            Codec.FLOAT.fieldOf("initial_severity").forGetter(Sickness::initialSeverity)
    ).apply(instance, Sickness::new));
}
