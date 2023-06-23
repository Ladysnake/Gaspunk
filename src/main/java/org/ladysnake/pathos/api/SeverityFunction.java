package org.ladysnake.pathos.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import org.ladysnake.pathos.Pathos;

public record SeverityFunction(Type type, float value) {

    public static final Codec<SeverityFunction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Type.CODEC.fieldOf("type").forGetter(SeverityFunction::type),
            Codec.FLOAT.fieldOf("value").forGetter(SeverityFunction::value)
    ).apply(instance, SeverityFunction::new));

    float apply(long ticks, float severity) {
        return type.func.apply(severity, ticks, value);
    }

    //TODO make this data-driven as well
    enum Type implements StringIdentifiable {
        LINEAR_DECREASE("linear_decrease", (severity, ticks, value) -> severity - ticks * value);

        private final Identifier name;
        private final SevFunctionInternal func;

        Type(String name, SevFunctionInternal func) {
            this(Pathos.id(name), func);
        }

        Type(Identifier name, SevFunctionInternal func) {
            this.name = name;
            this.func = func;
        }

        @Override
        public String asString() {
            return name.toString();
        }

        public static final Codec<Type> CODEC = StringIdentifiable.createCodec(Type::values);

        @FunctionalInterface
        private interface SevFunctionInternal {

            float apply(float severity, long ticks, float value);
        }
    }
}
