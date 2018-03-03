package ladysnake.gaspunk.gas.agent;

import ladysnake.gaspunk.api.IGasAgent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class GasAgent implements IGasAgent {
    private boolean toxic;
    private String unlocalizedName;

    public GasAgent(boolean toxic) {
        this.toxic = toxic;
    }

    @Override
    public boolean isToxic() {
        return toxic;
    }

    public void setUnlocalizedName(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
    }

    /**
     * @return the unlocalized name for this gas agent
     * @see Item#getUnlocalizedName()
     * @see Block#getUnlocalizedName()
     */
    public String getUnlocalizedName() {
        return unlocalizedName;
    }
}
