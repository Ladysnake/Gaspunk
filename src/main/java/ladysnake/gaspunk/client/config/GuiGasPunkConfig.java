package ladysnake.gaspunk.client.config;

import ladysnake.gaspunk.GasPunk;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;

public class GuiGasPunkConfig extends GuiConfig {
    public GuiGasPunkConfig(GuiScreen parentScreen, List<IConfigElement> additionalElements, String title) {
        super(parentScreen, GasPunk.MOD_ID, title);
        this.configElements.addAll(additionalElements);
    }
}
