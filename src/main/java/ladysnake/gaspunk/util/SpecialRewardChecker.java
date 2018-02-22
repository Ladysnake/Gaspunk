package ladysnake.gaspunk.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import ladysnake.gaspunk.GasPunk;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class SpecialRewardChecker {
    private static Map<NetHandlerPlayServer, GrenadeSkins> playerSelectedSkins = new WeakHashMap<>();
    private static ImmutableMap<UUID, ImmutableList<GrenadeSkins>> modOffWinners = ImmutableMap.of();

    public static boolean isSpecialPerson(UUID uuid) {
        return modOffWinners.containsKey(uuid);
    }

    public static ImmutableList<GrenadeSkins> getRewards(UUID uuid) {
        return modOffWinners.getOrDefault(uuid, ImmutableList.of());
    }

    public static void setSelectedReward(NetHandlerPlayServer playerHandler, GrenadeSkins skin) {
        // check that the player does have the selected reward
        if (isSpecialPerson(playerHandler.player.getUniqueID()) && getRewards(playerHandler.player.getUniqueID()).contains(skin)) {
            playerSelectedSkins.put(playerHandler, skin);
        }
    }

    public static GrenadeSkins getSelectedReward(EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            // double check to make sure that this player does have the skin
            return getRewards(player.getUniqueID()).stream()
                    .filter(g -> g.equals(playerSelectedSkins.get(((EntityPlayerMP) player).connection)))
                    .findAny()
                    .orElse(GrenadeSkins.NONE);
        }
        return GasPunk.proxy.getSelectedSkin();
    }

    public static void retrieveModOffWinners() {
        try {
            Gson GSON = new Gson();
            URLConnection rewardPage = new URL("https://ladysnake.glitch.me/gaspunk/users").openConnection();
            rewardPage.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(rewardPage.getInputStream()));
            String inputLine;
            StringBuilder jsonString = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                jsonString.append(inputLine);
            in.close();
            Type listType = new TypeToken<Map<String, List<String>>>() {
            }.getType();
            Map<String, List<String>> uuidList = GSON.fromJson(jsonString.toString(), listType);
            modOffWinners = uuidList.entrySet().stream()
                    .map(SpecialRewardChecker::deserializeEntry)
                    .collect(ImmutableMap.toImmutableMap(Pair::getKey, Pair::getValue));
            GasPunk.proxy.onSpecialRewardsRetrieved();
        } catch (IOException e) {
            GasPunk.LOGGER.warn("Could not connect to LadySnake's reward page. Maybe you're offline ?", e);
        } catch (JsonParseException e) {
            GasPunk.LOGGER.error("Bad json coming from LadySnake's reward page: ", e);
        } catch (IllegalArgumentException e) {
            GasPunk.LOGGER.error("Either we failed miserably at writing a UUID down or something is very wrong. I'd advise you to report this on Gaspunk's issues page.", e);
        }
    }

    private static Pair<UUID, ImmutableList<GrenadeSkins>> deserializeEntry(Map.Entry<String, List<String>> e) {
        ImmutableList.Builder<GrenadeSkins> builder = ImmutableList.builder();
        // Oh look this skin is offered even if you did not win it !
        builder.add(GrenadeSkins.NONE);
        // map to the respective enum object
        e.getValue().stream()
                .map(String::toUpperCase)
                .map(GrenadeSkins::valueOf)
                .forEach(builder::add);
        return Pair.of(
                UUID.fromString(e.getKey()),
                builder.build()
        );
    }

    public enum GrenadeSkins {
        NONE(0F), MODOFF(1F);

        public static final GrenadeSkins[] VALUES = values();

        /**
         * The texture used by the item
         */
        public final float textureId;

        GrenadeSkins(float textureId) {
            this.textureId = textureId;
        }
    }
}
