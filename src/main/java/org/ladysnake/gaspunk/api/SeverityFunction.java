package org.ladysnake.gaspunk.api;

@FunctionalInterface
public interface SeverityFunction {

    float apply(long ticks, float severity);
}
