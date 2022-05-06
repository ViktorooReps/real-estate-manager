package ru.msu.cmc.realestatemanager.controller.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Converter {
    public static <T> Map<String, T> getMap(String jsonString) {
        if (jsonString == null)
            return new HashMap<>();
        return new Gson().fromJson(jsonString, new TypeToken<HashMap<String, T>>() {}.getType());
    }

    public static <T> String getJson(Map<String, T> map) {
        if (map == null)
            return "";
        return new Gson().toJson(map, new TypeToken<HashMap<String, T>>() {}.getType());
    }

    public static String explainIntMap(Map<String, Integer> map) {
        if (map == null)
            return "";
        StringBuilder res = new StringBuilder();
        for (String key : map.keySet()) {
            res.append(key).append(": ").append(map.get(key)).append(", ");
        }
        return res.toString();
    }

    public static String explainBoolMap(Map<String, Boolean> map) {
        if (map == null)
            return "";
        StringBuilder res = new StringBuilder();
        for (String key : map.keySet()) {
            res.append(key).append(": ").append(map.get(key)).append(", ");
        }
        return res.toString();
    }
}
