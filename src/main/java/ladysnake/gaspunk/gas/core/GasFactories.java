package ladysnake.gaspunk.gas.core;

import ladysnake.gaspunk.api.basetype.GasTypes;
import ladysnake.gaspunk.gas.Gas;
import ladysnake.gaspunk.gas.GasAgents;
import net.minecraft.potion.Potion;

public class GasFactories {

    public static Gas createGasPotion(Potion potion, int potionDuration, int potionAmplifier) {
        return new Gas(GasTypes.GAS, 0xFF000000 | potion.getLiquidColor(), GasAgents.createPotionAgent(potion.getName(), potionDuration, potionAmplifier, potion), 1.0F);
    }

}
