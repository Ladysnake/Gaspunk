package ladysnake.gaspunk.sickness;

import ladysnake.gaspunk.gas.LingeringGas;
import ladysnake.gaspunk.network.PacketHandler;
import ladysnake.gaspunk.network.ShaderMessage;
import ladysnake.pathos.api.SicknessEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.UUID;

public class SicknessTearGas extends SicknessGas {

    private static final UUID TEAR_SLOWNESS_ID = UUID.fromString("6372ad90-c462-4223-8638-898c1166f824");
    private static final AttributeModifier TEAR_SLOWNESS = new AttributeModifier(TEAR_SLOWNESS_ID, "Tear gas slowness penalty", -0.2D, 2);

    public SicknessTearGas(LingeringGas gas) {
        super(gas, 0.001f);
    }

    @Override
    public boolean performEffect(EntityLivingBase carrier, SicknessEffect effect) {
        if (effect.getTicksSinceBeginning() == 0) {
            if (!carrier.world.isRemote) {
                if (carrier instanceof EntityPlayerMP)
                    PacketHandler.NET.sendTo(new ShaderMessage("shaders/post/blur.json"), (EntityPlayerMP) carrier);
                carrier.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(TEAR_SLOWNESS);
                return true;
            }
        }
        return super.performEffect(carrier, effect);
    }

    @Override
    public void onCured(SicknessEffect sicknessEffect, EntityLivingBase carrier) {
        if (!carrier.world.isRemote) {
            carrier.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(TEAR_SLOWNESS_ID);
            if (carrier instanceof EntityPlayerMP)
                PacketHandler.NET.sendTo(new ShaderMessage(null), (EntityPlayerMP) carrier);
        }
    }
}
