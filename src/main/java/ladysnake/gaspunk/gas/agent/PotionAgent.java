package ladysnake.gaspunk.gas.agent;

import ladysnake.gaspunk.api.IBreathingHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionAgent extends GasAgent {
    private Potion potion;
    private int potionDuration;
    private int potionAmplifier;

    public PotionAgent(Potion potion, int potionDuration, int potionAmplifier) {
        super(potion.isBadEffect());
        this.potion = potion;
        this.potionDuration = potionDuration;
        this.potionAmplifier = potionAmplifier;
    }

    @Override
    public void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick, float potency, boolean forced) {
        // still check for server-side because doing otherwise is stupid
        if (!entity.world.isRemote && (forced || entity.world.getWorldTime() % 20 == 0))
            entity.addPotionEffect(new PotionEffect(potion, potionDuration, potionAmplifier));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getLocalizedName() {
        return I18n.format(getUnlocalizedName(), I18n.format(potion.getName()));
    }
}
