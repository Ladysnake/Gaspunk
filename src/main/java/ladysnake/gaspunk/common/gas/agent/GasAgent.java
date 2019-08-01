package ladysnake.gaspunk.common.gas.agent;

import ladysnake.gaspunk.api.IGasAgent;
import ladysnake.gaspunk.common.gas.GasAgents;
import net.minecraft.util.Identifier;

public class GasAgent implements IGasAgent {
    protected boolean toxic;
    private String unlocalizedName;

    public GasAgent(boolean toxic) {
        this.toxic = toxic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isToxic() {
        return toxic;
    }

    public void setUnlocalizedName(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
    }

    /**
     * {@inheritDoc}
     */
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public Identifier getRegistryName() {
        return GasAgents.getId(this);
    }

    @Override
    public String toString() {
        return getLocalizedName();
    }
}
