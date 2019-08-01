package ladysnake.gaspunk.common.sickness;

import ladysnake.gaspunk.common.GasPunk;
import ladysnake.pathos.api.SicknessEffect;
import ladysnake.pathos.api.event.SicknessEvent;
import ladysnake.pathos.capability.CapabilitySickness;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.List;

public class SicknessTearGas extends SicknessGas {

//    private static final UUID TEAR_SLOWNESS_ID = UUID.fromString("6372ad90-c462-4223-8638-898c1166f824");
//    private static final AttributeModifier TEAR_SLOWNESS = new AttributeModifier(TEAR_SLOWNESS_ID, "Tear gas slowness penalty", -0.1D, 2);
//
    public SicknessTearGas() {
        super(0.001f);
    }
//
//    @Override
//    public boolean performEffect(LivingEntity carrier, SicknessEffect effect) {
//        if (effect.getTicksSinceBeginning() == 0) {
//            if (!carrier.world.isRemote) {
//                IAttributeInstance attribute = carrier.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
//                if (attribute.getModifier(TEAR_SLOWNESS_ID) == null)
//                    attribute.applyModifier(TEAR_SLOWNESS);
//                return true;
//            }
//        }
//        return super.performEffect(carrier, effect);
//    }
//
//    @Override
//    public void onCured(SicknessEffect sicknessEffect, LivingEntity carrier) {
//        if (!carrier.world.isRemote) {
//            carrier.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(TEAR_SLOWNESS_ID);
//        }
//    }

    @Override
    public boolean isSynchronized() {
        return true;
    }

    /**
     * This class has been adapted from Blur's source code under MIT License
     * https://github.com/tterrag1098/Blur/blob/master/src/main/java/com/tterrag/blur/Blur.java<br>
     * <p>See <em>notice.md</em> in assets/minecraft/shaders</p>
     *
     * @author tterag
     */
    @Environment(EnvType.CLIENT)
    @Mod.EventBusSubscriber(modid = GasPunk.MOD_ID, value = Side.CLIENT)
    public static class ClientTearEffect {

        private static MethodHandle _listShaders;

        static {
            try {
                Field f = ReflectionHelper.findField(ShaderEffect.class, "field_148031_d", "listShaders");
                _listShaders = MethodHandles.lookup().unreflectGetter(f);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        @SubscribeEvent
        public static void onSicknessAdd(SicknessEvent.SicknessAddEvent event) {
            if (!event.getEntity().world.isRemote || !(event.getEffect().getSickness() instanceof SicknessTearGas)) return;
            EntityRenderer er = MinecraftClient.getInstance().gameRenderer;
            if (!er.isShaderActive()) {
                er.loadShader(new Identifier("shaders/post/fade_in_blur.json"));
            }
        }

        @SubscribeEvent
        public static void onRenderTick(TickEvent.RenderTickEvent event) {
            if (_listShaders != null && event.phase == TickEvent.Phase.END && MinecraftClient.getInstance().gameRenderer.isShaderActive()) {
                // check that the player has an active eye irritation effect
                float progress = CapabilitySickness.getHandler(MinecraftClient.getInstance().player)
                        .map(h -> h.getActiveEffect(GasPunkSicknesses.EYE_IRRITATION))
                        .map(ClientTearEffect::getProgress).orElse(0F);
                ShaderEffect sg = MinecraftClient.getInstance().gameRenderer.getShaderGroup();
                try {
                    @SuppressWarnings("unchecked")
                    List<PostProcessShader> shaders = (List<PostProcessShader>) _listShaders.invoke(sg);
                    for (PostProcessShader s : shaders) {
                        GlUniform su = s.getShaderManager().getShaderUniform("GaspunkProgress");
                        if (su != null) {
                            su.set(progress);
                        }
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }

        private static float getProgress(SicknessEffect effect) {
            return effect == null ? 0 : effect.getSeverity() * 2;
        }

    }
}
