package me.srb.minirpg.utils;

import com.google.gson.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class ItemStackDeserializer implements JsonDeserializer<ItemStack> {
    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String materialString = jsonObject.get("material").getAsString();
        int amount = jsonObject.get("amount").getAsInt();
        Material material;
        try {
            material = Material.valueOf(materialString); // Bez zamiany na wielkie litery
        } catch (IllegalArgumentException e) {
            // Obsługa błędu, na przykład jeśli materiał nie istnieje
            e.printStackTrace();
            return null;
        }
        return new ItemStack(material, amount);
    }
}