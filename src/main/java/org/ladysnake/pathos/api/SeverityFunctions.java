package org.ladysnake.pathos.api;

public class SeverityFunctions {

    public static SeverityFunction linearDecrease(float decreasePerTick) {
        return (t, severity) -> Math.max(0, severity + t * -decreasePerTick);
    }

    public static SeverityFunction linear(float increasePerTick) {
        return (t, severity) -> Math.max(0, severity + t * increasePerTick);
    }

    public static SeverityFunction exponential(float base) {
        return (t, severity) -> (float) Math.max(0, Math.exp(t * base));
    }
}
