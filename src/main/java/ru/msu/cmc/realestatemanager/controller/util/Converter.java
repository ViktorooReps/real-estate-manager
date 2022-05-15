package ru.msu.cmc.realestatemanager.controller.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.*;

@Service
public class Converter {
    private static class MapDeserializer implements JsonDeserializer<Map<String, Object>> {

        public Map<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Map<String, Object> m = new LinkedHashMap<String, Object>();
            JsonObject jo = json.getAsJsonObject();
            for (Map.Entry<String, JsonElement> mx : jo.entrySet()) {
                String key = mx.getKey();
                JsonElement v = mx.getValue();
                if (v.isJsonArray()) {
                    m.put(key, converter.fromJson(v, List.class));
                } else if (v.isJsonPrimitive()) {
                    Number num = null;
                    ParsePosition position=new ParsePosition(0);
                    String vString=v.getAsString();
                    try {
                        num = NumberFormat.getInstance(Locale.ROOT).parse(vString,position);
                    } catch (Exception ignored) {}
                    //Check if the position corresponds to the length of the string
                    if(position.getErrorIndex() < 0 && vString.length() == position.getIndex()) {
                        if (num != null) {
                            m.put(key, num);
                            continue;
                        }
                    }
                    JsonPrimitive prim = v.getAsJsonPrimitive();
                    if (prim.isBoolean()) {
                        m.put(key, prim.getAsBoolean());
                    } else if (prim.isString()) {
                        m.put(key, prim.getAsString());
                    } else {
                        m.put(key, null);
                    }

                } else if (v.isJsonObject()) {
                    m.put(key, converter.fromJson(v, Map.class));
                }

            }
            return m;
        }
    }

    private static class ListDeserializer implements JsonDeserializer<List<Object>> {

        public List<Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            List<Object> m = new ArrayList<Object>();
            JsonArray arr = json.getAsJsonArray();
            for (JsonElement jsonElement : arr) {
                if (jsonElement.isJsonObject()) {
                    m.add(converter.fromJson(jsonElement, Map.class));
                } else if (jsonElement.isJsonArray()) {
                    m.add(converter.fromJson(jsonElement, List.class));
                } else if (jsonElement.isJsonPrimitive()) {
                    Number num = null;
                    try {
                        num = NumberFormat.getInstance().parse(jsonElement.getAsString());
                    } catch (Exception ignored) {}
                    if (num != null) {
                        m.add(num);
                        continue;
                    }
                    JsonPrimitive prim = jsonElement.getAsJsonPrimitive();
                    if (prim.isBoolean()) {
                        m.add(prim.getAsBoolean());
                    } else if (prim.isString()) {
                        m.add(prim.getAsString());
                    } else {
                        m.add(null);
                    }
                }
            }
            return m;
        }
    }

    private static final Gson converter = new GsonBuilder().registerTypeAdapter(Map.class, new MapDeserializer()).registerTypeAdapter(List.class, new ListDeserializer()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static <T> Map<String, T> getMap(String jsonString) {
        if (jsonString == null)
            return new HashMap<>();
        return converter.fromJson(jsonString, new TypeToken<HashMap<String, T>>() {}.getType());
    }

    public static Map<String, Integer> getIntMap(String jsonString) {
        if (jsonString == null)
            return new HashMap<>();
        Map<String, Number> unconverted = converter.fromJson(jsonString, new TypeToken<HashMap<String, Integer>>() {}.getType());

        Map<String, Integer> converted = new LinkedHashMap<>();
        for (String key : unconverted.keySet())
            converted.put(key, unconverted.get(key).intValue());

        return converted;
    }

    public static <T> String getJson(Map<String, T> map) {
        if (map == null)
            return "";
        return converter.toJson(map, new TypeToken<HashMap<String, T>>() {}.getType());
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
