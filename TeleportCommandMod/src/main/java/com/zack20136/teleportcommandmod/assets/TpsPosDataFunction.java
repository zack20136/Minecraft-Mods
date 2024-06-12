package com.zack20136.teleportcommandmod.assets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.math.BlockPos;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpsPosDataFunction {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static String getCoordinatesFolder() {
        return "mods/tps/coordinates/";
    }

    private static String getPlayerCoordinatesFile(UUID playerUUID) {
        return getCoordinatesFolder() + playerUUID + ".json";
    }

    public static Map<String, TpsPosData> loadCoordinates(UUID playerUUID) {
        Map<String, TpsPosData> coordinates;
        try{
            File file = new File(getPlayerCoordinatesFile(playerUUID));
            if (!file.exists()) {
                file.createNewFile();
                coordinates = new HashMap<>();
                saveCoordinates(playerUUID, coordinates);
                return coordinates;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            JsonObject jsonObject = GSON.fromJson(reader, JsonObject.class);
            coordinates = new HashMap<>();

            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String name = entry.getKey();
                JsonObject coordinateObject = entry.getValue().getAsJsonObject();
                int x = coordinateObject.get("x").getAsInt();
                int y = coordinateObject.get("y").getAsInt();
                int z = coordinateObject.get("z").getAsInt();
                String dimension = coordinateObject.get("dimension").getAsString();
                BlockPos pos = new BlockPos(x, y, z);
                String description = coordinateObject.has("description") ? coordinateObject.get("description").getAsString() : "";
                TpsPosData coordinateData = new TpsPosData(pos, dimension, description);
                coordinates.put(name, coordinateData);
            }

            reader.close();
            return coordinates;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, TpsPosData> saveCoordinates(UUID playerUUID, Map<String, TpsPosData> coordinates) {
        try{
            File file = new File(getPlayerCoordinatesFile(playerUUID));
            FileWriter writer = new FileWriter(file);
            JsonObject jsonObject = new JsonObject();

            for (Map.Entry<String, TpsPosData> entry : coordinates.entrySet()) {
                String name = entry.getKey();
                TpsPosData coordinateData = entry.getValue();
                JsonObject coordinateObject = new JsonObject();
                coordinateObject.addProperty("x", coordinateData.getPos().getX());
                coordinateObject.addProperty("y", coordinateData.getPos().getY());
                coordinateObject.addProperty("z", coordinateData.getPos().getZ());
                coordinateObject.addProperty("dimension", coordinateData.getDimension());
                if (!coordinateData.getDescription().isEmpty()) {
                    coordinateObject.addProperty("description", coordinateData.getDescription());
                }
                jsonObject.add(name, coordinateObject);
            }

            writer.write(GSON.toJson(jsonObject));
            writer.close();

            return coordinates;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
