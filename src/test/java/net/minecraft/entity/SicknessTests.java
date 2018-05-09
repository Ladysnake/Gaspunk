// why in this package ? Well, to let mockito access protected methods from entity
package net.minecraft.entity;

import ladysnake.gaspunk.api.IBreathingHandler;
import ladysnake.gaspunk.api.IGasAgent;
import ladysnake.gaspunk.api.basetype.GasTypes;
import ladysnake.gaspunk.gas.Gas;
import ladysnake.gaspunk.gas.GasAgents;
import ladysnake.gaspunk.gas.SuspendableGas;
import ladysnake.gaspunk.gas.agent.LingeringAgent;
import ladysnake.gaspunk.gas.core.CapabilityBreathing;
import ladysnake.gaspunk.init.ModSicknesses;
import ladysnake.pathos.api.ISicknessHandler;
import ladysnake.pathos.api.SicknessEffect;
import ladysnake.pathos.capability.CapabilitySickness;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("ConstantConditions")
public class SicknessTests {

    private static final IGasAgent NERVE = GasAgents.createSicknessAgent("sarin_gas", true, true, ModSicknesses.LUNG_CONTROL_LOSS);
    public static final Gas SARIN_GAS = new SuspendableGas(GasTypes.GAS, 0x00FFFFFF, NERVE, 0.8F);
    private EntityCreeper mockedCreeper;

    static {
        Launch.blackboard = new HashMap<>();
        Launch.blackboard.put("fml.deobfuscatedEnvironment", true);
        Bootstrap.register();
        CapabilityBreathing.register();
        CapabilitySickness.register();
        //noinspection unchecked
        CapabilityBreathing.CAPABILITY_BREATHING = (Capability<IBreathingHandler>) mock(Capability.class);
        //noinspection unchecked
        CapabilitySickness.CAPABILITY_SICKNESS = (Capability<ISicknessHandler>) mock(Capability.class);
    }

    @Before
    public void setUp() {
        mockedCreeper = mock(EntityCreeper.class);
        // prevent entity.world.isRemote check from crashing
        mockedCreeper.world = mock(World.class);
        // allow the testing of the air supply attribute
        AbstractAttributeMap attributeMap = new AttributeMap();
        attributeMap.registerAttribute(CapabilityBreathing.MAX_AIR_SUPPLY);
        when(mockedCreeper.getAttributeMap()).thenReturn(attributeMap);
        when(mockedCreeper.getEntityAttribute(any())).then(InvocationOnMock::callRealMethod);
        // setup capabilities
        IBreathingHandler breathingHandler = new CapabilityBreathing.DefaultBreathingHandler(mockedCreeper);
        ISicknessHandler sicknessHandler = new CapabilitySickness.DefaultSicknessHandler(mockedCreeper);
        when(mockedCreeper.getCapability(any(), any()))
                .then(invocation -> invocation.getArgument(0) == CapabilitySickness.CAPABILITY_SICKNESS
                        ? sicknessHandler
                        : breathingHandler);
        when(mockedCreeper.getItemStackFromSlot(any())).thenReturn(ItemStack.EMPTY);
        when(mockedCreeper.decreaseAirSupply(anyInt())).then(InvocationOnMock::callRealMethod);
    }

    @Test
    public void testSetup() {
        assertTrue(CapabilityBreathing.getHandler(mockedCreeper).isPresent());
        assertTrue(CapabilitySickness.getHandler(mockedCreeper).isPresent());
    }

    @Test
    public void testGasAdd() {
        CapabilityBreathing.getHandler(mockedCreeper).ifPresent(h -> h.setConcentration(SARIN_GAS, 0.5f));
        assertTrue(CapabilityBreathing.getHandler(mockedCreeper).map(IBreathingHandler::getGasConcentrations).map(h -> h.get(SARIN_GAS)).isPresent());
    }

    @Test
    public void testGasTick() {
        IBreathingHandler handler = CapabilityBreathing.getHandler(mockedCreeper).get();
        Gas gas = new Gas(GasTypes.SMOKE, 0xFF);
        handler.setConcentration(gas, 0.5f);
        assertTrue(handler.getGasConcentrations().containsKey(gas));
        handler.tick();
        assertFalse(handler.getGasConcentrations().containsKey(gas));
    }

    @Test
    public void testSarinTick() {
        IBreathingHandler handler = CapabilityBreathing.getHandler(mockedCreeper).get();
        handler.setConcentration(SARIN_GAS, 0.5f);
        handler.tick();
        ISicknessHandler sicknessHandler = CapabilitySickness.getHandler(mockedCreeper).get();
        assertNotNull(sicknessHandler.getActiveEffect(((LingeringAgent) NERVE).getSickness()));
    }

    @Test
    public void testSarinTick2() {
        IBreathingHandler breathingHandler = CapabilityBreathing.getHandler(mockedCreeper).get();
        final float concentration = 0.5f;
        final float time = 5;
        for (int i = 0; i < time; i++) {
            breathingHandler.setConcentration(SARIN_GAS, concentration);
            breathingHandler.tick();
        }
        ISicknessHandler sicknessHandler = CapabilitySickness.getHandler(mockedCreeper).get();
        LingeringAgent sarinAgent = (LingeringAgent) NERVE;
        SicknessEffect effect = sicknessHandler.getActiveEffect(sarinAgent.getSickness());
        float potency = SARIN_GAS.getAgents().get(0).getPotency();
        float toxicityPerTick = potency * concentration / 20;
        float oracle = toxicityPerTick * time;
        assertEquals(oracle, effect.getSeverity(), 1E-8f);
    }

    @Test
    public void testMustardGas() {
        IBreathingHandler breathingHandler = CapabilityBreathing.getHandler(mockedCreeper).get();
        final float concentration = 1.0f;
        final float time = 5;
        Gas gas = new Gas(GasTypes.GAS, 0xFF, NERVE, 1);
        for (int i = 0; i < time; i++) {
            breathingHandler.setConcentration(gas, concentration);
            breathingHandler.tick();
        }
        float potency = gas.getAgents().get(0).getPotency();
        float oracle = potency * time;
        // TODO make a test for evolution in gas concentration
//        assertEquals(oracle, breathingHandler.getGasConcentrations().get(ModGases.MUSTARD_GAS), 1E-8f);
    }
}