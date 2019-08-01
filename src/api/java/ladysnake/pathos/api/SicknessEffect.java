package ladysnake.pathos.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraftforge.registries.RegistryManager;

import java.util.Objects;

/**
 * @see net.minecraft.entity.effect.StatusEffect
 */
public class SicknessEffect {
    private ISickness sickness;
    private float severity;
    private int ticksSinceBeginning;
    private int ticksSinceLastPerform;

    /**
     * Creates a new sickness effect
     * @param sickness the sickness proxied by this effect
     * @param severity the severity of this effect
     */
    public SicknessEffect(ISickness sickness, float severity) {
        this(sickness, severity, 0, 0);
    }

    /**
     * Creates a new sickness effect with the provided properties. <br>
     * This constructor is intended to be used for deserialization purposes
     *
     * @param sickness the sickness proxied by this effect
     * @param severity the severity of this effect
     * @param ticksSinceBeginning the amount of ticks passed since the beginning of the effect
     * @param ticksSinceLastPerform the amount of ticks passed since the last true perform
     */
    public SicknessEffect(ISickness sickness, float severity, int ticksSinceBeginning, int ticksSinceLastPerform) {
        this.sickness = sickness;
        this.severity = severity;
        this.ticksSinceBeginning = ticksSinceBeginning;
        this.ticksSinceLastPerform = ticksSinceLastPerform;
    }

    /**
     * Recreates a sickness effect from its NBT representation
     * @param nbt the serialized effect
     */
    public SicknessEffect(CompoundTag nbt) {
        this.sickness = RegistryManager.ACTIVE.getRegistry(ISickness.class).getValue(new Identifier(nbt.getString("effect")));
        this.severity = nbt.getFloat("severity");
        this.ticksSinceLastPerform = nbt.getInt("ticksSinceLastPerform");
        this.ticksSinceBeginning = nbt.getInt("ticksSinceBeginning");
    }

    public ISickness getSickness() {
        return sickness;
    }

    /**
     * Sets the severity of this effect.
     * If the severity is 0 at any given tick, this effect will get cleared.
     *
     * @param severity the new severity for this sickness effect
     * @see #onCured(LivingEntity)
     */
    public void setSeverity(float severity) {
        this.severity = severity;
    }

    /**
     * @return the severity of this sickness effect
     */
    public float getSeverity() {
        return severity;
    }

    /**
     * Merges this sickness effect with another of the same type, cumulating their severity
     * @param other another sickness effect of the same type as this one
     * @return this effect
     */
    public SicknessEffect mergeSeverities(SicknessEffect other) {
        if (other.getSickness() == this.getSickness()) {
            this.setSeverity(this.getSeverity() + other.getSeverity());
            other.setSeverity(0);
        }
        return this;
    }

    /**
     * Affects the passed in entity with the disease. <br>
     * Also resets <code>ticksSinceLastPerform</code> if the effect was performed correctly by the proxied sickness
     *
     * @param carrier the entity being affected
     * @see ladysnake.pathos.api.event.SicknessEvent.SicknessTickEvent
     */
    public void performEffect(LivingEntity carrier) {
        if(this.sickness.performEffect(carrier, this))
            this.ticksSinceLastPerform = 0;
        ticksSinceLastPerform++;
        ticksSinceBeginning++;
    }

    /**
     * @return the total amount of ticks passed since the entity has contracted this disease
     */
    public int getTicksSinceBeginning() {
        return ticksSinceBeginning;
    }

    /**
     * @return the time in ticks passed since this effect has last acted upon the entity
     */
    public int getTicksSinceLastPerform() {
        return ticksSinceLastPerform;
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("effect", Objects.requireNonNull(this.sickness.getRegistryName()).toString());
        nbt.putFloat("severity", this.severity);
        nbt.putInt("ticksSinceLastPerform", this.ticksSinceLastPerform);
        nbt.putInt("ticksSinceBeginning", this.ticksSinceBeginning);
        return nbt;
    }

    /**
     * Called when this effect is about to be removed from the entity's list of active sicknesses <br>
     * This method is generally called when the effect's <code>severity</code> falls to 0
     *
     * @param carrier the entity afflicted by this effect
     *                
     * @see ladysnake.pathos.api.event.SicknessEvent.SicknessCureEvent
     */
    public void onCured(LivingEntity carrier) {
        this.sickness.onCured(this, carrier);
    }

    @Override
    public String toString() {
        return "SicknessEffect{" +
                "sickness=" + sickness +
                ", severity=" + severity +
                ", ticksSinceBeginning=" + ticksSinceBeginning +
                ", ticksSinceLastPerform=" + ticksSinceLastPerform +
                '}';
    }
}
