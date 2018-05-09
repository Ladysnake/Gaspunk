package ladysnake.gaspunk.compat;

import ladysnake.gaspunk.api.IGas;
import ladysnake.gaspunk.api.customization.GrenadeSkins;
import ladysnake.gaspunk.init.ModGases;
import ladysnake.gaspunk.init.ModItems;
import ladysnake.gaspunk.item.ItemGasTube;
import ladysnake.gaspunk.item.ItemGrenade;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.brewing.*;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;
import java.util.List;

public class ThaumcraftCompat {

    public static void registerAspects() {
        ThaumcraftApi.registerObjectTag(
                new ItemStack(ModItems.SULFUR),
                new AspectList()
                        .add(Aspect.FIRE, 10)
                        .add(Aspect.EARTH, 8)
                        .add(Aspect.ENTROPY, 2)
        );
        ThaumcraftApi.registerObjectTag(
                new ItemStack(ModItems.ASH),
                new AspectList()
                        .add(Aspect.FIRE, 7)
                        .add(Aspect.ENTROPY, 5)
                        .add(Aspect.DEATH, 10)
        );
        ThaumcraftApi.registerObjectTag(
                new ItemStack(ModItems.GRENADE),
                new AspectList(new ItemStack(ModItems.DIFFUSER))
                        .add(Aspect.ALCHEMY, 5)
                        .add(Aspect.ENERGY, 7)
                        .add(Aspect.AIR, 7)
        );
        ThaumcraftApi.registerObjectTag(
                new ItemStack(ModItems.SMOKE_POWDER),
                new AspectList()
                        .add(divide(AspectHelper.getObjectAspects(new ItemStack(Items.GUNPOWDER)), 1.5))
                        .add(Aspect.SENSES, 10)
                        .add(Aspect.AIR, 6)
        );
        ThaumcraftApi.registerObjectTag(
                new ItemStack(ModItems.EMPTY_GRENADE),
                new AspectList(new ItemStack(ModItems.DIFFUSER))
                        .add(Aspect.VOID, 8)
        );
        // register the aspects for smoke first
        registerGasAspect(ModGases.SMOKE, new AspectList());
        for (IGas gas : ModGases.REGISTRY.getValues())
            registerGasAspect(gas, new AspectList());
    }

    private static AspectList generateAspectsFromBrewing(ItemStack stack) {
        List<AspectList> all = new ArrayList<>();
        for (IBrewingRecipe recipe : BrewingRecipeRegistry.getRecipes()) {
            if (recipe instanceof AbstractBrewingRecipe) {
                ItemStack output = ((AbstractBrewingRecipe) recipe).getOutput();
                if (ItemGasTube.getContainedGas(output) == ItemGasTube.getContainedGas(stack)) {
                    AspectList aspects = new AspectList();
                    aspects.add(AspectHelper.getObjectAspects(((AbstractBrewingRecipe) recipe).getInput()));
                    if (recipe instanceof BrewingRecipe) {
                        aspects.add(AspectHelper.getObjectAspects(((BrewingRecipe) recipe).getIngredient()));
                    } else if (recipe instanceof BrewingOreRecipe) {
                        for (ItemStack ingredient : ((BrewingOreRecipe) recipe).getIngredient()) {
                            AspectList list = AspectHelper.getObjectAspects(ingredient);
                            for (Aspect aspect : list.getAspects()) {
                                aspects.add(aspect, list.getAmount(aspect) / list.size());
                            }
                        }
                    }
                    all.add(aspects);
                }
            }
        }
        AspectList ret = new AspectList();
        for (AspectList list : all) {
            for (Aspect aspect : list.getAspects()) {
                ret.add(aspect, list.getAmount(aspect) / all.size());
            }
        }
        return AspectHelper.cullTags(ret);
    }

    private static AspectList divide(AspectList aspects, double amount) {
        AspectList ret = new AspectList();
        if (aspects != null) {
            for (Aspect a : aspects.getAspects()) {
                ret.add(a, MathHelper.floor(aspects.getAmount(a) / amount));
            }
        }
        return ret;
    }

    private static void registerGasAspect(IGas gas, AspectList aspects) {
        ItemStack gasTube = ((ItemGasTube)ModItems.GAS_TUBE).getItemStackFor(gas);
        aspects.add(divide(generateAspectsFromBrewing(gasTube), 1.1));
        ThaumcraftApi.registerObjectTag(gasTube, aspects.copy());
        ItemStack grenade = ((ItemGrenade)ModItems.GRENADE).getItemStackFor(gas);
//        aspects = divide(aspects, 1.2);
        aspects.add(divide(AspectHelper.getObjectAspects(new ItemStack(ModItems.GRENADE)), 1.8));
        for (GrenadeSkins skin : GrenadeSkins.VALUES) {
            // need to register for every skin as thaumcraft counts an itemstack with different nbt as unique
            ((ItemGrenade)ModItems.GRENADE).setSkin(grenade, skin);
            ThaumcraftApi.registerObjectTag(grenade, aspects.copy());
        }
    }
}
