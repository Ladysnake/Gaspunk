package ladysnake.gaspunk.gas.core;

import ladysnake.gaspunk.gas.GasAgents;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class GasAgentBuilder {
    private Map<String, String> params = new HashMap<>();
    private String name;

    public GasAgentBuilder(String name) {
        this.name = name;
    }

    public GasAgentBuilder set(String param, String value) {
        this.params.put(param, value);
        return this;
    }

    public void make() {
        switch (String.valueOf(this.params.get("factory"))) {
            case "sickness":
                GasAgents.createSicknessAgent(
                        name,
                        Boolean.valueOf(params.get("toxic")),
                        Boolean.valueOf(params.get("ignoreBreath")),
                        new ResourceLocation(params.get("sickness"))
                );
                break;
            case "damage":
                GasAgents.createDamageAgent(name, Integer.valueOf(params.get("maxDamage")));
                break;
            case "potion":
                GasAgents.createPotionAgent(
                        name,
                        Integer.valueOf(params.get("duration")),
                        Integer.valueOf(params.get("amplifier")),
                        new ResourceLocation(params.get("potion"))
                );
                break;
            default:
                throw new RuntimeException("Unrecognized agent factory : "+ this.params.get("factory"));
        }
    }

}
