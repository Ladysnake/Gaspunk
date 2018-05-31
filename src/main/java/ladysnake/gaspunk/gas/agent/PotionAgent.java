package ladysnake.gaspunk.gas.agent;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.api.IBreathingHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionAgent extends GasAgent {
    private Potion potion;
    private ResourceLocation potionId;
    private int potionDuration;
    private int potionAmplifier;

    public PotionAgent(ResourceLocation potion, int potionDuration, int potionAmplifier) {
        super(true);    // default to the potion being toxic, will be changed later
        this.potionId = potion;
        this.potionDuration = potionDuration;
        this.potionAmplifier = potionAmplifier;
    }

    public Potion getPotion() {
        if (potion == null) {
            potion = ForgeRegistries.POTIONS.getValue(potionId);
            if (potion == null) {
                GasPunk.LOGGER.warn("Potion gas agent {} is linked to invalid potion id {}", getRegistryName(), potionId);
                return MobEffects.LUCK;
            }
            this.toxic = potion.isBadEffect();
        }
        return potion;
    }

    @Override
    public void applyEffect(EntityLivingBase entity, IBreathingHandler handler, float concentration, boolean firstTick, float potency, boolean forced) {
        // still check for server-side because doing otherwise is stupid
        if (!entity.world.isRemote && (forced || entity.world.getWorldTime() % 20 == 0)) {
            Potion potion = getPotion();
            entity.addPotionEffect(new PotionEffect(potion, potionDuration, potionAmplifier));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getLocalizedName() {
        return I18n.format(getUnlocalizedName(), I18n.format(getPotion().getName()));
    }
}
