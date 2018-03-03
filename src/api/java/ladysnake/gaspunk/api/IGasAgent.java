package ladysnake.gaspunk.api;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A gas agent that defines the behaviour of a gas.
 */
public interface IGasAgent {

    /**
     * Whether this gas type suffocates entities by default (can be overridden by individual gases)
     */
    boolean isToxic();

    /**
     * Called each tick to affect entities inside a gas cloud
     *
     * @param entity        the entity breathing this gas
     * @param handler       the entity's breathing handler
     * @param concentration the concentration of this gas in the air breathed by the entity
     * @param firstTick     true if this entity was not affected by this gas during the previous tick
     * @param potency       the potency of the agent in the gas
     */
    default void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick, float potency) {
        // NO-OP
    }

    /**
     * Called the tick after an entity has stopped being affected directly by this gas
     * Can be used to clean up toggled effects
     *
     * @param entity  the entity that has stopped breathing this gas
     * @param handler the entity's breathing handler
     */
    default void onExitCloud(EntityLivingBase entity, IBreathingHandler handler) {
        // NO-OP
    }

    @SideOnly(Side.CLIENT)
    default String getLocalizedName() {
        return I18n.format(getUnlocalizedName());
    }

    String getUnlocalizedName();
}
