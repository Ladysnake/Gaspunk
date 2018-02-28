package ladysnake.gaspunk.gas;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.api.IBreathingHandler;
import ladysnake.gaspunk.api.IGasType;
import ladysnake.gaspunk.api.ILingeringGas;
import ladysnake.pathos.capability.CapabilitySickness;
import ladysnake.pathos.api.ISickness;
import ladysnake.pathos.api.SicknessEffect;
import net.minecraft.entity.EntityLivingBase;

import java.util.Objects;
import java.util.function.Function;

public class LingeringGas extends Gas implements ILingeringGas {
    private final Function<LingeringGas, ISickness> sicknessSupplier;
    protected float toxicity;
    protected boolean ignoreBreath;
    protected ParticleTypes particleType;

    public LingeringGas(Function<LingeringGas, ISickness> sicknessSupplier, IGasType type, int color, int bottleColor, float toxicity, boolean ignoreBreath, ParticleTypes particleType) {
        super(type, color, bottleColor);
        this.sicknessSupplier = sicknessSupplier;
        this.toxicity = toxicity;
        this.ignoreBreath = ignoreBreath;
        this.particleType = particleType;
    }

    @Override
    public ISickness createSickness() {
        return sicknessSupplier.apply(this).setRegistryName(Objects.requireNonNull(this.getRegistryName()));
    }

    @Override
    public boolean isToxic() {
        return getToxicity() > 0;
    }

    /**
     * @return this gas' toxicity
     * @see #addEffectToEntity(EntityLivingBase, float)
     */
    public float getToxicity() {
        return this.toxicity;
    }

    /**
     * Adds a sickness effect to the entity breathing this gas
     * By default this will add a new effect with a severity computed as
     * <pre> {@code (concentration * toxicity) / 20f}</pre>
     * The resulting effect severity will be cumulated with the previous one, if present
     * (this can be changed by other implementations)
     *
     * @param entity        the entity breathing this gas
     * @param concentration the concentration in the air breathed by this entity
     */
    @Override
    public void addEffectToEntity(EntityLivingBase entity, float concentration) {
        CapabilitySickness.getHandler(entity).ifPresent(h -> h.addSickness(new SicknessEffect(LINGERING_EFFECTS.get(this), (getToxicity() * concentration) / 20)));
    }

    @Override
    public void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick) {
        if (ignoreBreath || handler.getAirSupply() <= 0)
            addEffectToEntity(entity, concentration);
    }

    @Override
    public ParticleTypes getParticleType() {
        return particleType;
    }

    /**
     * {@code LingeringGas.Builder} is used for creating a {@code LingeringGas} from
     * various gas parameters.
     */
    public static class Builder {
        private Function<LingeringGas, ISickness> sicknessFactory;
        private IGasType gasType;
        private int color, liquidColor;
        private float toxicity;
        private boolean ignoreBreath;
        private ParticleTypes particleType;

        public Builder setSicknessFactory(Function<LingeringGas, ISickness> factory) {
            this.sicknessFactory = Objects.requireNonNull(factory);
            return this;
        }

        public Builder setGasType(IGasType gasType) {
            this.gasType = Objects.requireNonNull(gasType);
            return this;
        }

        public Builder setToxicity(float toxicity) {
            this.toxicity = toxicity;
            return this;
        }

        public Builder setIgnoreBreath() {
            this.ignoreBreath = true;
            return this;
        }

        public Builder setColor(int color) {
            return setColor(color, color);
        }

        public Builder setColor(int color, int liquidColor) {
            this.color = color;
            this.liquidColor = liquidColor;
            return this;
        }

        public Builder setParticleType(ParticleTypes type) {
            this.particleType = type;
            return this;
        }

        public LingeringGas build() {
            if (sicknessFactory == null)
                throw new IllegalStateException("gas sickness factory not provided");
            if (gasType == null)
                throw new IllegalStateException("gas type not provided");
            if (particleType == null)
                particleType = gasType.getParticleType();
            if (toxicity == 0f)
                // display the calling line for easier debugging
                GasPunk.LOGGER.warn("Lingering gas created with 0 toxicity, it will not do anything.", Thread.currentThread().getStackTrace()[2]);
            return new LingeringGas(sicknessFactory, gasType, color, liquidColor, toxicity, ignoreBreath, particleType);
        }
    }

}
