package ladysnake.gaspunk.gas;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.api.IGasAgent;
import ladysnake.gaspunk.gas.agent.*;
import ladysnake.gaspunk.sickness.SicknessTearGas;
import ladysnake.gaspunk.sickness.SicknessSarin;
import ladysnake.pathos.api.ISickness;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GasAgents {
    /**
     * Maps agents to their respective sickness
     */
    public static final BiMap<LingeringAgent, ISickness> LINGERING_EFFECTS = HashBiMap.create();
    public static final Map<ResourceLocation, IGasAgent> AGENT_MAP = new HashMap<>();

    public static final IGasAgent TEAR_GAS = createSicknessAgent("tear_gas", true, true, SicknessTearGas::new);
    public static final IGasAgent DAMAGE_AGENT = createDamageAgent("toxic_smoke");
    public static final IGasAgent SARIN = createSicknessAgent("sarin_gas", SarinAgent::new, SicknessSarin::new);

    public static IGasAgent createDamageAgent(String name) {
        IGasAgent ret = name(new DamageAgent(20), name);
        AGENT_MAP.put(new ResourceLocation(GasPunk.MOD_ID, name), ret);
        return ret;
    }

    public static IGasAgent createPotionAgent(Potion potion, int potionDuration, int potionAmplifier) {
        return name(new PotionAgent(potion, potionDuration, potionAmplifier), "potion");
    }

    public static IGasAgent createSicknessAgent(String name, boolean toxic, boolean ignoreBreath, Supplier<ISickness> sicknessSupplier) {
        return createSicknessAgent(name, () -> new LingeringAgent(toxic, ignoreBreath), sicknessSupplier);
    }

    public static IGasAgent createSicknessAgent(String name, Supplier<LingeringAgent> agentSupplier, Supplier<ISickness> sicknessSupplier) {
        LingeringAgent agent = name(agentSupplier.get(), name);
        ISickness sickness = sicknessSupplier.get();
        ResourceLocation id = new ResourceLocation(GasPunk.MOD_ID, name);
        sickness.setRegistryName(id);
        LINGERING_EFFECTS.put(agent, sickness);
        AGENT_MAP.put(id, agent);
        return agent;
    }

    public static <T extends GasAgent> T name(T agent, String name) {
        agent.setUnlocalizedName("agent." + GasPunk.MOD_ID + "." + name);
        return agent;
    }
}
