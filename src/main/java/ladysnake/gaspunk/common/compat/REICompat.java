package ladysnake.gaspunk.common.compat;

import ladysnake.gaspunk.common.item.GasPunkItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class REICompat implements IModPlugin {

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
        subtypeRegistry.useNbtForSubtypes(GasPunkItems.GAS_TUBE, GasPunkItems.GRENADE);
    }

}
