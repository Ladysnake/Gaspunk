package org.ladysnake.pathos.api;


import com.google.common.base.MoreObjects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.gaspunk.api.SeverityFunction;
import org.ladysnake.pathos.api.event.SicknessEvents;

/**
 * @see net.minecraft.entity.effect.StatusEffect
 */
public class SicknessEffect {
    private final Sickness sickness;
    private float severity;
    private int ticksSinceBeginning;
    private int ticksSinceLastPerform;

    /**
     * Creates a new api effect
     *
     * @param sickness the api proxied by this effect
     * @param severity the severity of this effect
     */
    public SicknessEffect(Sickness sickness, float severity) {
        this(sickness, severity, 0, 0);
    }

    /**
     * Creates a new api effect with the provided properties. <br>
     * This constructor is intended to be used for deserialization purposes
     *
     * @param sickness              the api proxied by this effect
     * @param severity              the severity of this effect
     * @param ticksSinceBeginning   the amount of ticks passed since the beginning of the effect
     * @param ticksSinceLastPerform the amount of ticks passed since the last true perform
     */
    public SicknessEffect(Sickness sickness, float severity, int ticksSinceBeginning, int ticksSinceLastPerform) {
        this.sickness = sickness;
        this.severity = severity;
        this.ticksSinceBeginning = ticksSinceBeginning;
        this.ticksSinceLastPerform = ticksSinceLastPerform;
    }

    /**
     * Recreates a api effect from its NBT representation
     *
     * @param nbt the serialized effect
     */
    public SicknessEffect(NbtCompound nbt) {
        this.sickness = getSickness(nbt);
        this.severity = nbt.getFloat("severity");
        this.ticksSinceLastPerform = nbt.getInt("ticksSinceLastPerform");
        this.ticksSinceBeginning = nbt.getInt("ticksSinceBeginning");
    }

    @Nullable
    public static Sickness getSickness(NbtCompound nbt) {
        return Sickness.REGISTRY.get(new Identifier(nbt.getString("effect")));
    }

    public Sickness getSickness() {
        return sickness;
    }

    /**
     * Sets the severity of this effect.
     * If the severity is 0 at any given tick, this effect will get cleared.
     *
     * @param severity the new severity for this api effect
     * @see #onCured(net.minecraft.entity.LivingEntity)
     */
    public void setSeverity(float severity) {
        this.severity = severity;
    }

    /**
     * @return the severity of this api effect
     */
    public float getInitialSeverity() {
        return severity;
    }

    /**
     * Merges this api effect with another of the same type, cumulating their severity
     *
     * @param other another api effect of the same type as this one
     * @return this effect
     */
    public SicknessEffect mergeSeverities(SicknessEffect other) {
        if (other.getSickness() == this.getSickness()) {
            this.setSeverity(this.getInitialSeverity() + other.getInitialSeverity());
            other.setSeverity(0);
        }
        return this;
    }

    /**
     * Affects the passed in entity with the disease. <br>
     * Also resets <code>ticksSinceLastPerform</code> if the effect was performed correctly by the proxied api
     *
     * @param carrier the entity being affected
     * @see SicknessEvents.SicknessTickEvent
     */
    public void performEffect(LivingEntity carrier) {
        ticksSinceLastPerform++;
        ticksSinceBeginning++;
        if (this.sickness.performEffect(carrier, this)) {
            this.ticksSinceLastPerform = 0;
        }
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

    public float getRemainingSeverity(SeverityFunction function) {
        return function.apply(getTicksSinceBeginning(), getInitialSeverity());
    }

    public NbtCompound serializeNBT() {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("effect", Sickness.REGISTRY.getId(this.getSickness()).toString());
        nbt.putFloat("severity", this.getInitialSeverity());
        nbt.putInt("ticksSinceLastPerform", this.getTicksSinceLastPerform());
        nbt.putInt("ticksSinceBeginning", this.getTicksSinceBeginning());
        return nbt;
    }

    /**
     * Called when this effect is about to be removed from the entity's list of active sicknesses <br>
     * This method is generally called when the effect's <code>severity</code> falls to 0
     *
     * @param carrier the entity afflicted by this effect
     * @see SicknessEvents.SicknessCureEvent
     */
    public void onCured(LivingEntity carrier) {
        this.sickness.onCured(this, carrier);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sickness", sickness.getId())
                .add("severity", severity)
                .add("ticksSinceBeginning", ticksSinceBeginning)
                .add("ticksSinceLastPerform", ticksSinceLastPerform)
                .toString();
    }
}
