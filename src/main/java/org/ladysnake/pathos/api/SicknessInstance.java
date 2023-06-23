package org.ladysnake.pathos.api;


import com.google.common.base.MoreObjects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.ladysnake.pathos.api.event.SicknessEvents;

/**
 * @see net.minecraft.entity.effect.StatusEffectInstance
 */
public class SicknessInstance {

    private final World world;
    private final Sickness sickness;
    private float initialSeverity;
    private long infectionTimestamp;

    /**
     * Creates a new api effect
     *
     * @param world
     * @param sickness        the sickness proxied by this instance
     * @param initialSeverity the severity of this effect
     */
    public SicknessInstance(World world, Sickness sickness, float initialSeverity) {
        this(world, sickness, initialSeverity, 0);
    }

    /**
     * Creates a new sickness instance with the provided properties. <br>
     * This constructor is intended to be used for deserialization purposes
     *
     * @param world
     * @param sickness           the sickness proxied by this instance
     * @param initialSeverity    the severity of this effect
     * @param infectionTimestamp the amount of ticks passed since the beginning of the effect
     */
    public SicknessInstance(World world, Sickness sickness, float initialSeverity, long infectionTimestamp) {
        this.world = world;
        this.sickness = sickness;
        this.initialSeverity = initialSeverity;
        this.infectionTimestamp = infectionTimestamp;
    }

    /**
     * Recreates a sickness instance from its NBT representation
     *
     * @param world
     * @param nbt   the serialized effect
     */
    public SicknessInstance(World world, NbtCompound nbt) {
        this.world = world;
        this.sickness = Sickness.getRegistry(world).get(new Identifier(nbt.getString("sickness")));
        this.initialSeverity = nbt.getFloat("severity");
        this.infectionTimestamp = nbt.getLong("infection_timestamp");
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
        this.initialSeverity = severity;
    }

    /**
     * @return the severity of this api effect
     */
    public float getInitialSeverity() {
        return initialSeverity;
    }

    /**
     * Merges this api effect with another of the same type, cumulating their severity
     *
     * @param other another api effect of the same type as this one
     * @return this effect
     */
    public SicknessInstance mergeSeverities(SicknessInstance other) {
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
        infectionTimestamp++;
        this.sickness.effects().forEach(effect -> {
            effect.performEffect(carrier, this);
        });
    }

    /**
     * @return the total amount of ticks passed since the entity has contracted this disease
     */
    public long getTicksSinceBeginning(long currentTime) {
        return currentTime - infectionTimestamp;
    }

    public long getInfectionTimestamp() {
        return infectionTimestamp;
    }

    public float getRemainingSeverity(long currentTime) {
        return MathHelper.clamp(sickness.severityFunction().apply(getTicksSinceBeginning(currentTime), getInitialSeverity()), 0.0F, 1.0F);
    }

    public NbtCompound serializeNBT() {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("sickness", Sickness.getRegistry(world).getId(this.getSickness()).toString());
        nbt.putFloat("initial_severity", this.getInitialSeverity());
        nbt.putLong("infection_timestamp", this.getInfectionTimestamp());
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
        this.sickness.effects().forEach(effect -> effect.onCured(this, carrier));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sickness", Sickness.getRegistry(world).getId(this.getSickness()))
                .add("initial_severity", getInitialSeverity())
                .add("infection_timestamp", getInfectionTimestamp())
                .add("remaining_severity", getRemainingSeverity(world.getTime()))
                .toString();
    }
}
