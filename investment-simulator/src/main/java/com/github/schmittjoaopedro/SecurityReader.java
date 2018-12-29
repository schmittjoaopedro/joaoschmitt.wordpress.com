package com.github.schmittjoaopedro;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SecurityReader {

    private static Gson gson;

    static {
        gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
    }

    public static List<Security> getInvestments(String file) {
        List<Security> investments = new ArrayList<>();
        JsonParser jsonParser;
        JsonReader jsonReader;
        Object object;
        JsonObject jsonObject;
        try {
            jsonParser = new JsonParser();
            jsonReader = new JsonReader(new FileReader(file));
            object = jsonParser.parse(jsonReader);
            jsonObject = (JsonObject) object;
            JsonArray securities = jsonObject.getAsJsonArray("securities");
            for (int i = 0; i < securities.size(); i++) {
                investments.add(createInvestment(securities.get(i).getAsJsonObject()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        return investments;
    }

    private static Security createInvestment(JsonObject jsonInvestment) {
        return gson.fromJson(jsonInvestment, Security.class);
    }

}
