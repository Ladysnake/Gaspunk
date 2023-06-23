package org.ladysnake.pathos.api;

public class SeverityFunctions {

    public static SeverityFunction linearDecrease(float decreasePerTick) {
        return new SeverityFunction(SeverityFunction.Type.LINEAR_DECREASE, decreasePerTick);
    }
}
