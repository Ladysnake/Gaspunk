package org.ladysnake.pathos.component;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.pathos.Pathos;
import org.ladysnake.pathos.api.Sickness;
import org.ladysnake.pathos.api.SicknessHandler;
import org.ladysnake.pathos.api.SicknessInstance;
import org.ladysnake.pathos.api.event.SicknessEvents;

import java.util.*;
import java.util.function.BiFunction;

public class SicknessHandlerImpl implements SicknessHandler {

    public static final float MIN_SEVERITY_THRESHOLD = 0.000001F;

    private final Map<Sickness, SicknessInstance> sicknesses = new HashMap<>();
    private final List<SicknessInstance> toRemove = new LinkedList<>();
    private final LivingEntity entity;

    public SicknessHandlerImpl(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public LivingEntity getEntity() {
        return entity;
    }

    @Override
    public boolean isSicknessActive(Sickness sickness) {
        return sicknesses.containsKey(sickness);
    }

    @Override
    public void addSickness(SicknessInstance effect, BiFunction<SicknessInstance, SicknessInstance, SicknessInstance> mergeFunction) {
        //TODO allow event to replace merge function
        if (SicknessEvents.ON_ADD.invoker().onSicknessAdd(this, getEntity(), effect, mergeFunction)) {
            sicknesses.merge(effect.getSickness(), effect, mergeFunction);
        }
    }

    @Override
    public Collection<SicknessInstance> getActiveSicknesses() {
        return Collections.unmodifiableCollection(sicknesses.values());
    }

    @Nullable
    @Override
    public SicknessInstance getActiveEffect(Sickness sickness) {
        return sicknesses.get(sickness);
    }

    @Override
    public void cure(Sickness sickness) {
        SicknessInstance effect = sicknesses.getOrDefault(sickness, null);
        if (effect == null) { // no effect to cure
            return;
        }
        sicknesses.remove(sickness);
        effect.onCured(getEntity());
    }

    @Override
    public void serverTick() {
        for (SicknessInstance instance : getActiveSicknesses()) {

            // only perform the effect if the event is not canceled; if an effect is not ticked it also will not be cured naturally
            if (!SicknessEvents.ON_TICK.invoker().onTick(this, getEntity(), instance)) {
                continue;
            }
            instance.performEffect(getEntity());

            if (instance.getRemainingSeverity(getEntity().getWorld().getTime()) < MIN_SEVERITY_THRESHOLD && SicknessEvents.ON_CURE.invoker().onCure(this, getEntity(), instance)) { // consider that effect cured
                toRemove.add(instance);
            }
        }

        if (!toRemove.isEmpty()) {
            toRemove.forEach(effect -> this.cure(effect.getSickness()));
            sync();
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        NbtList list = tag.getList("sicknesses", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < list.size(); i++) {
            NbtCompound effectTag = list.getCompound(i);
            SicknessInstance effect = new SicknessInstance(getEntity().getWorld(), effectTag);
            if (effect.getSickness() == null) {
                Pathos.getLogger().error("Unable to deserialize sickness effect '{}'!", effectTag);
                continue;
            }
            sicknesses.put(effect.getSickness(), effect);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtList list = new NbtList();
        getActiveSicknesses().forEach(effect -> list.add(effect.serializeNBT()));
        tag.put("sicknesses", list);
    }
}
