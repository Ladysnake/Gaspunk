package ladysnake.gaspunk.gas.core;

import ladysnake.gaspunk.gas.Gas;
import ladysnake.gaspunk.gas.GasAgents;
import net.minecraft.potion.Potion;

public class GasFactories {

    public static Gas createGasPotion(Potion potion, int potionDuration, int potionAmplifier) {
        return new Gas(GasTypes.VAPOR, potion.getLiquidColor(), GasAgents.createPotionAgent(potion, potionDuration, potionAmplifier), 1.0F);
    }

}
