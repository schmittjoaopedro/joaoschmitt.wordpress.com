package com.github.schmittjoaopedro;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyService {

    protected String solrCoreName;

    public CompanyService(String solrCoreName) {
        super();
        this.solrCoreName = solrCoreName;
    }

    public List<Map<String, Object>> getCompanyValues(String id, String version) {
        String value;
        JsonReader jsonReader;
        Object object;
        JsonArray jsonArray;
        JsonObject jsonObject;
        List<Map<String, Object>> companyMap = null;
        JsonParser jsonParser = new JsonParser();
        value = HttpUtil.getRequest("company_v" + version + "_full");
        jsonReader = new JsonReader(new StringReader(value));
        object = jsonParser.parse(jsonReader);
        jsonArray = (JsonArray) object;
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject = jsonArray.get(i).getAsJsonObject();
            if (jsonObject.get("id").getAsString().equals(id)) {
                companyMap = new ArrayList<>();
                Map<String, Object> company = new HashMap<>();
                company.put("companyId", jsonObject.get("id").getAsInt());
                company.put("companyVersion", jsonObject.get("version").getAsInt());
                company.put("companyName", jsonObject.get("name").getAsString());
                companyMap.add(company);
                break;
            }
        }
        return companyMap;
    }

}
