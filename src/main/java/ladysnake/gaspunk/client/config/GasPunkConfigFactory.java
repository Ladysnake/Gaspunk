package ladysnake.gaspunk.client.config;

import ladysnake.gaspunk.client.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(Side.CLIENT)
public class GasPunkConfigFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraftInstance) {
        // NO-OP
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        List<IConfigElement> additionalElements = new ArrayList<>();
        ClientProxy.getGrenadeSkinProperty().map(ConfigElement::new).ifPresent(additionalElements::add);
        return new GuiGasPunkConfig(parentScreen, additionalElements, "Gaspunk Config");
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }
}
