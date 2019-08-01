package ladysnake.gaspunk.common.config;

import ladysnake.gaspunk.api.customization.GrenadeSkins;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.util.version.VersionParsingException;

import java.util.Collections;
import java.util.List;

public class GasPunkCommonConfig implements GasPunkConfig {

    public static final String CONFIG_VERSION = "1.0.0";

    protected boolean fastGas = false;

    protected List<String> otherGasMasks = Collections.emptyList();

    private SemanticVersion configVersion;

    {
        try {
            configVersion = SemanticVersion.parse(CONFIG_VERSION);
        } catch (VersionParsingException e) {
            throw new IllegalStateException(CONFIG_VERSION + " is not a semantic version");
        }
    }

    @Override
    public SemanticVersion getConfigVersion() {
        return configVersion;
    }

    @Override
    public boolean useFastGas() {
        return this.fastGas;
    }

    @Override
    public List<String> getOtherGasMasks() {
        return this.otherGasMasks;
    }

    @Override
    public boolean shouldUseShaders() {
        return false;
    }

    @Override
    public boolean shouldRenderGasOverlays() {
        return false;
    }

    @Override
    public GrenadeSkins getSelectedSkin() {
        return GrenadeSkins.NONE;
    }

}
