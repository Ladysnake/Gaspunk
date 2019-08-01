package ladysnake.gaspunk.common.gas;

import ladysnake.gaspunk.api.IGasAgent;
import ladysnake.gaspunk.common.GasPunk;
import ladysnake.gaspunk.common.gas.agent.*;
import ladysnake.gaspunk.common.gas.core.GasAgentDeserializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.SystemUtil;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

public class GasAgents {

    public static final MutableRegistry<IGasAgent> AGENT_REGISTRY = new SimpleRegistry<>();

    public static void init() {
        // No need to register the agent registry to the registry registry, as gases themselves are already synchronized
        GasAgentDeserializer.loadGasAgents();
        Registry.register(GasAgents.AGENT_REGISTRY, GasPunk.id("candyfloss"), new CandyflossAgent());
        Gases.visitRegistry(AGENT_REGISTRY, (rl, ag) -> {
            if (ag instanceof GasAgent && ag.getUnlocalizedName() == null) {
                ((GasAgent) ag).setUnlocalizedName(SystemUtil.createTranslationKey("agent", AGENT_REGISTRY.getId(ag)));
            }
        });
    }

    public static IGasAgent registerDamageAgent(String name, int maxDamage) {
        IGasAgent ret = name(new DamageAgent(maxDamage), name);
        Registry.register(AGENT_REGISTRY, GasPunk.id(name), ret);
        return ret;
    }

    public static IGasAgent registerPotionAgent(String name, int potionDuration, int potionAmplifier, Identifier potion) {
        IGasAgent ret = name(new PotionAgent(potion, potionDuration, potionAmplifier), "potion");
        Registry.register(AGENT_REGISTRY, GasPunk.id(name), ret);
        return ret;
    }

    public static IGasAgent registerSicknessAgent(String name, boolean toxic, boolean ignoreBreath, Identifier sickness) {
        return registerSicknessAgent(name, new LingeringAgent(toxic, ignoreBreath, sickness));
    }

    private static IGasAgent registerSicknessAgent(String name, LingeringAgent agent) {
        name(agent, name);
        Identifier id = GasPunk.id(name);
        Registry.register(AGENT_REGISTRY, id, agent);
        return agent;
    }

    public static <T extends GasAgent> T name(T agent, String name) {
        agent.setUnlocalizedName("agent." + GasPunk.MOD_ID + "." + name);
        return agent;
    }

    public static Identifier getId(IGasAgent agent) {
        return AGENT_REGISTRY.getId(agent);
    }

    public static IGasAgent getAgent(Identifier id) {
        return AGENT_REGISTRY.get(id);
    }
}
