package ladysnake.gaspunk.api;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * A starting point for an implementation of {@link IGas} using one or more {@link IGasAgent}
 * to perform its effects
 */
public abstract class AbstractGas extends IForgeRegistryEntry.Impl<IGas> implements IGas {

    // this will hold a reference to an internal method at runtime
    private static Supplier<Builder> builderSupplier;

    /**
     * Returns a new builder. The generated builder is an implementation of {@link Builder} using references
     * to internal classes.
     */
    @Nonnull
    public static Builder builder() {
        if (builderSupplier == null) throw new IllegalStateException("The gas class has not yet been initialized");
        return builderSupplier.get();
    }

    /**
     * Gets the agents that produce this gas' effect.
     * Agent effects are made up of a {@link IGasAgent gas agent} and a potency,
     * with the potency defining the fraction of power the agent has by default (further reduced by concentration).
     * @return a list of {@link AgentEffect} used to perform this gas' effect
     */
    public abstract ImmutableList<AgentEffect> getAgents();

    /**
     * Gets the tooltip displayed by this gas on corresponding items, one entry per line
     * @return an array containing the default tooltip lines
     */
    public abstract String[] getTooltipLines();

    public static class AgentEffect {
        private final IGasAgent agent;
        private final float potency;

        public AgentEffect(IGasAgent agent, float potency) {
            this.agent = agent;
            this.potency = potency;
        }

        public IGasAgent getAgent() {
            return agent;
        }

        public float getPotency() {
            return potency;
        }
    }

    /**
     * A builder for creating {@code AbstractGas} instances. Example: <pre>   {@code
     *
     *   static final AbstractGas CHEESE =
     *       AbstractGas.builder()
     *           .setType(GasTypes.GAS)
     *           .setColor(0xffcc66)
     *           .addTooltipLine("gas.cheesemod.cheese.tooltip")
     *           .build();}</pre>
     *
     * <p>Building does not change the state of the builder, so it is still possible to add more
     * elements and to build again.
     *
     */
    public interface Builder {

        Builder setType(IGasType type);

        Builder setParticleType(IGasParticleType type);

        Builder setColor(int color);

        Builder setBottleColor(int color);

        Builder addAgent(IGasAgent agent, float potency);

        Builder addTooltipLine(String tooltipLine);

        AbstractGas build();
    }
}
