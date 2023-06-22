package org.ladysnake.pathos.api;

@FunctionalInterface
public interface SeverityFunction {

    float apply(long ticks, float severity);
}
