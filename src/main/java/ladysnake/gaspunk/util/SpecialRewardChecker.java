package ladysnake.gaspunk.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import ladysnake.gaspunk.GasPunk;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class SpecialRewardChecker {
    // Uses NetHandlerPlayServer as key as it keeps being the same object as long as the player does not disconnect
    private static Map<NetHandlerPlayServer, GrenadeSkins> playerSelectedSkins = new WeakHashMap<>();
    private static ImmutableMap<UUID, ImmutableList<GrenadeSkins>> specialPersons = ImmutableMap.of();

    /**
     * Checks if the given uuid is known to LadySnake's special rewards page
     */
    public static boolean isSpecialPerson(UUID uuid) {
        return specialPersons.containsKey(uuid);
    }

    /**
     * Gets the list of skins a player has been rewarded with
     * @param uuid the uuid of a player
     * @return a list of grenade skins that the corresponding player can obtain
     */
    public static ImmutableList<GrenadeSkins> getRewards(UUID uuid) {
        return specialPersons.getOrDefault(uuid, ImmutableList.of(GrenadeSkins.NONE));
    }

    /**
     * Sets the skin choice for the player associated with the given connection
     */
    public static void setSelectedGrenadeSkin(NetHandlerPlayServer playerHandler, GrenadeSkins skin) {
        // check that the player does have the selected reward
        if (isSpecialPerson(playerHandler.player.getUniqueID()) && getRewards(playerHandler.player.getUniqueID()).contains(skin)) {
            playerSelectedSkins.put(playerHandler, skin);
        }
    }

    /**
     * If the player has multiple grenade skin available, gets the one he selected in his config.
     * Defaults to <code>GrenadeSkins.NONE</code> if the player does not have special skins or has selected an invalid one somehow
     *
     * @return the grenade skin selected by this player
     */
    public static GrenadeSkins getSelectedSkin(EntityPlayer player) {
        GrenadeSkins playerChoice = (player instanceof EntityPlayerMP)
                ? playerSelectedSkins.get(((EntityPlayerMP) player).connection)
                : GasPunk.proxy.getSelectedSkin();
        // double check to make sure that this player does have the skin he selected
        return getRewards(player.getUniqueID()).contains(playerChoice) ? playerChoice : GrenadeSkins.NONE;
    }

    /**
     * Populates <code>specialPersons</code> with entries from our page of people having won special rewards
     */
    public static void retrieveSpecialRewards() {
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
            Type listType = new TypeToken<Map<String, List<String>>>() {}.getType();
            Map<String, List<String>> uuidList = GSON.fromJson(jsonString.toString(), listType);
            specialPersons = uuidList.entrySet().stream()
                    .map(SpecialRewardChecker::deserializeEntry)
                    .filter(Objects::nonNull) // remove invalid entries
                    .collect(ImmutableMap.toImmutableMap(Pair::getKey, Pair::getValue));
            GasPunk.proxy.onSpecialRewardsRetrieved();
        } catch (IOException e) {
            GasPunk.LOGGER.warn("Could not connect to LadySnake's reward page. Maybe you're offline ?", e);
        } catch (JsonParseException e) {
            GasPunk.LOGGER.error("Bad json coming from LadySnake's reward page. This should be reported.", e);
        }
    }

    /**
     * @param entry a serialized entry containing a uuid and a list of skins
     * @return a pair representing the deserialized entry, or null if an error occured
     */
    @Nullable
    private static Pair<UUID, ImmutableList<GrenadeSkins>> deserializeEntry(Map.Entry<String, List<String>> entry) {
        ImmutableList.Builder<GrenadeSkins> builder = ImmutableList.builder();
        // Oh look this skin is offered even if you did not win it !
        builder.add(GrenadeSkins.NONE);
        // map to the respective enum object
        try {
            entry.getValue().stream()
                    .map(String::toUpperCase)
                    .map(GrenadeSkins::valueOf)
                    .forEach(builder::add);
            return Pair.of(
                    UUID.fromString(entry.getKey()),
                    builder.build()
            );
        } catch (IllegalArgumentException e) {
            GasPunk.LOGGER.error("Either an UUID is wrong or you're using an outdated version. If you do have the latest version, please report this issue", e);
        }
        return null;
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
