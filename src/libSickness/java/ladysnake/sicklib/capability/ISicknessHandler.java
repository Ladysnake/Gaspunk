package ladysnake.sicklib.capability;

import ladysnake.sicklib.sickness.SicknessEffect;

import java.util.Collection;

public interface ISicknessHandler {

    void addSickness(SicknessEffect effect);

    /**
     * Ticks this handler, making all afflictions perform their effect
     */
    void tick();

    /**
     * @return all the sickness effects this handler is afflicted with
     */
    Collection<SicknessEffect> getActiveSicknesses();

}
