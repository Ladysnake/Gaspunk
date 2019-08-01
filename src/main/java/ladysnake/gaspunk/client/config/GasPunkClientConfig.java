package ladysnake.gaspunk.client.config;

import ladysnake.gaspunk.common.config.GasPunkCommonConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;

import java.util.Collections;

public class GasPunkClientConfig extends GasPunkCommonConfig {
    private boolean useShaders = true;

    private boolean renderGasOverlays = false;

    @Override
    public boolean shouldUseShaders() {
        return this.useShaders;
    }

    @Override
    public boolean shouldRenderGasOverlays() {
        return this.renderGasOverlays;
    }

    public void configure(ConfigBuilder builder) {
        builder.getOrCreateCategory("general")
                .addEntry(
                        ConfigEntryBuilder.create().startBooleanToggle("Use Fast Gas", this.fastGas)
                                .setDefaultValue(false)
                                .setTooltip("Disables gas / smoke / vapor clouds checking for a clear path to entities and make them only check straight distance instead")
                                .setSaveConsumer(b -> this.fastGas = b)
                                .build()
                )
                .addEntry(
                        ConfigEntryBuilder.create().startStrList("Other Gas Masks", this.otherGasMasks)
                                .setDefaultValue(Collections.emptyList())
                                .setTooltip(
                                        "The items which ID's are added here will be considered as gas masks when breathing gas",
                                        "You can specify entire armor sets by separating items with \"&\" in a single entry. Using \"*\" instead of an item id will match anything.",
                                        "Examples: \"minecraft:diamond_helmet\", \"minecraft:golden_helmet&*&minecraft:chainmail_leggings\""
                                )
                                .setSaveConsumer(l -> this.otherGasMasks = l)
                                .build()
                );
        builder.getOrCreateCategory("client")
                .addEntry(
                        ConfigEntryBuilder.create().startBooleanToggle("Use Shaders", this.useShaders)
                                .setDefaultValue(true)
                                .setTooltip("Enables the use of shaders to render the gas overlay, won't do anything if renderGasOverlays is set to false")
                                .setSaveConsumer(b -> this.useShaders = b)
                                .build()
                )
                .addEntry(
                        ConfigEntryBuilder.create().startBooleanToggle("Render Gas Overlays", this.renderGasOverlays)
                                .setDefaultValue(false)
                                .setTooltip("Display a custom overlay when inside a gas cloud, combine with useShaders for a dynamically generated overlay")
                                .setSaveConsumer(b -> this.renderGasOverlays = b)
                                .build()
                );
    }
}
