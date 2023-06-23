package org.ladysnake.gaspunk.init;

import org.ladysnake.gaspunk.sickness.EyeIrritationSicknessEffect;
import org.ladysnake.gaspunk.sickness.LungControlLossSicknessEffect;
import org.ladysnake.pathos.api.SicknessEffect;

public class GPSicknessEffects {

    public static final SicknessEffect EYE_IRRITATION = new EyeIrritationSicknessEffect(); //sev: -0.001/t
    public static final SicknessEffect LUNG_CONTROL_LOSS = new LungControlLossSicknessEffect(); //sev: -0.004/t
}
