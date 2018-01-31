package ladysnake.gaspunk.event;

import ladysnake.gaspunk.gas.CapabilityBreathing;
import ladysnake.gaspunk.gas.IBreathingHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * GasImmunityEvent is fired whenever an entity needs to find out if they're affected by surrounding gases
 * in {@link CapabilityBreathing.DefaultBreathingHandler#isImmune()}.<br>
 *
 * <br>
 * All children of this event are fired on the {@link MinecraftForge#EVENT_BUS}.<br>
 */
public class GasImmunityEvent extends LivingEvent {
    private final IBreathingHandler breathHandler;
    private boolean immune;

    public GasImmunityEvent(EntityLivingBase entity, IBreathingHandler breathHandler, boolean immune) {
        super(entity);
        this.breathHandler = breathHandler;
        this.immune = immune;
    }

    public IBreathingHandler getBreathHandler() {
        return breathHandler;
    }

    public boolean isImmune() {
        return immune;
    }

    public void setImmune(boolean immune) {
        this.immune = immune;
    }
}
