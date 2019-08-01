package ladysnake.gaspunk.common.config;

import ladysnake.gaspunk.api.customization.GrenadeSkins;
import net.fabricmc.loader.api.SemanticVersion;

import java.util.List;
import java.util.function.UnaryOperator;

public interface GasPunkConfig {

    static GasPunkConfig get() {
        return ConfigHolder.config;
    }

    static void load(Class<? extends GasPunkConfig> configClass) {
        load(configClass, UnaryOperator.identity());
    }

    static <C extends GasPunkConfig> void load(Class<C> configClass, UnaryOperator<C> postProcessor) {
        ConfigHolder.loadConfig(configClass, postProcessor);
    }

    SemanticVersion getConfigVersion();

    boolean useFastGas();

    List<String> getOtherGasMasks();

    boolean shouldUseShaders();

    boolean shouldRenderGasOverlays();

    GrenadeSkins getSelectedSkin();
}
