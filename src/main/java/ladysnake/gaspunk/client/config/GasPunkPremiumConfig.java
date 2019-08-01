package ladysnake.gaspunk.client.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import ladysnake.gaspunk.api.customization.GrenadeSkins;
import ladysnake.gaspunk.common.GasPunk;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.EnumListEntry;

import java.lang.reflect.Field;
import java.util.EnumSet;

public class GasPunkPremiumConfig extends GasPunkClientConfig {
    private GrenadeSkins selectedSkin;
    private transient ImmutableSet<GrenadeSkins> availableSkins;

    public void setAvailableSkins(ImmutableSet<GrenadeSkins> availableSkins) {
        this.availableSkins = availableSkins;
    }

    public void setSelectedSkin(GrenadeSkins selectedSkin) {
        if (availableSkins.contains(selectedSkin)) {
            this.selectedSkin = selectedSkin;
        }
    }

    @Override
    public GrenadeSkins getSelectedSkin() {
        return this.selectedSkin;
    }

    @Override
    public void configure(ConfigBuilder builder) {
        super.configure(builder);
        EnumListEntry skinCfg = ConfigEntryBuilder.create().startEnumSelector("Special Grenade Skin", GrenadeSkins.class, this.selectedSkin)
                .setSaveConsumer(this::setSelectedSkin)
                .setTooltip("The kind of diffuser you will craft (grenade skins are determined by the diffuser used)")
                .build();
        try {
            Field enumValues = EnumListEntry.class.getDeclaredField("values");
            enumValues.setAccessible(true);
            enumValues.set(skinCfg, ImmutableList.copyOf(this.availableSkins));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            GasPunk.LOGGER.warn("[Gaspunk] Failed to hack cloth config");
        }
        builder.getOrCreateCategory("general").addEntry(skinCfg);
    }
}
