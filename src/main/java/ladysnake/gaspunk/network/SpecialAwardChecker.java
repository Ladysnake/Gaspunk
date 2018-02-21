package ladysnake.gaspunk.network;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import ladysnake.gaspunk.GasPunk;
import net.minecraft.entity.player.EntityPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.UUID;

public class SpecialAwardChecker {
    private static ImmutableList<UUID> modOffWinners = ImmutableList.of();

    public static boolean isModOffWinner(EntityPlayer player) {
        return modOffWinners.contains(player.getUniqueID());
    }

    public static void retrieveModOffWinners() {
        try {
            Gson GSON = new Gson();
            URLConnection database = new URL("http://ladysnake.glitch.me/gaspunk/rewards").openConnection();
            database.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(database.getInputStream()));
            String inputLine;
            StringBuilder jsonString = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                jsonString.append(inputLine);
            in.close();
            Type listType = new TypeToken<List<String>>(){}.getType();
            List<String> uuidList = GSON.fromJson(jsonString.toString(), listType);
            modOffWinners = uuidList.stream().map(UUID::fromString).collect(ImmutableList.toImmutableList());
        } catch (IOException e) {
            GasPunk.LOGGER.warn("Could not connect to LadySnake's skin database. Maybe you're offline ?", e);
        } catch (JsonParseException e) {
            GasPunk.LOGGER.error("Bad json coming from the skin database : ", e);
        }
    }
}
