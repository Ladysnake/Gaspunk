package ladysnake.gaspunk.common.compat;

import io.github.prospector.modmenu.api.ModMenuApi;
import ladysnake.CalledThroughReflection;
import ladysnake.gaspunk.client.config.GasPunkClientConfig;
import ladysnake.gaspunk.common.GasPunk;
import ladysnake.gaspunk.common.config.GasPunkConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Function;

@CalledThroughReflection
public final class ModMenuCompat implements ModMenuApi {
    @Override
    public String getModId() {
        return GasPunk.MOD_ID;
    }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return (parent) -> {
            ConfigBuilder configBuilder = ConfigBuilder.create().setParentScreen(parent)
                    .setTitle("gaspunk.config.screen_title");
            GasPunkConfig cfg = GasPunkConfig.get();
            if (cfg instanceof GasPunkClientConfig) {
                ((GasPunkClientConfig) cfg).configure(configBuilder);
            }
            return configBuilder.build();
        };
    }
}
