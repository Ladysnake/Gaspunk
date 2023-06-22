package org.ladysnake.gaspunk.init;

import org.ladysnake.gaspunk.api.SeverityFunctions;
import org.ladysnake.gaspunk.sickness.GasSickness;
import org.ladysnake.gaspunk.sickness.SarinGasSickness;
import org.ladysnake.pathos.api.Sickness;

public class GPSicknesses {

    public static final Sickness TEAR_GAS = new GasSickness(SeverityFunctions.linearDecrease(0.001F)); // eye_irritation
    public static final Sickness SARIN_GAS = new SarinGasSickness(SeverityFunctions.linearDecrease(0.004F)); // lung_control_loss
}
