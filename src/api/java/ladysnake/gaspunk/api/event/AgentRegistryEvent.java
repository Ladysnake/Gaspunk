package ladysnake.gaspunk.api.event;

import ladysnake.gaspunk.api.IGasAgent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.IContextSetter;

import java.util.Map;

/**
 * This event is fired right before gas registration. <br>
 * It is used in much the same way as a {@link net.minecraftforge.event.RegistryEvent.Register forge registry event},
 * except it does not have a full registry attached.
 */
public class AgentRegistryEvent extends Event implements IContextSetter {
    private final Map<ResourceLocation, IGasAgent> registry;

    public AgentRegistryEvent(Map<ResourceLocation, IGasAgent> registry) {
        this.registry = registry;
    }

    public void register(String id, IGasAgent agent) {
        ResourceLocation rl;
        if (id.indexOf(':') < 0) {
            ModContainer active = Loader.instance().activeModContainer();
            if (active == null) throw new IllegalStateException("A gas agent has been registered by an unidentified mod.");
            rl = new ResourceLocation(active.getModId(), id);
        } else {
            rl = new ResourceLocation(id);
        }
        this.registry.put(rl, agent);
    }
}
