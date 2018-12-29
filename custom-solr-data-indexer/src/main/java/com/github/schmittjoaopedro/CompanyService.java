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

    private static final String REPO_URL = "https://raw.githubusercontent.com/schmittjoaopedro/joaoschmitt.wordpress.com/master/custom-solr-data-indexer/assets/company_v#VERSION.json";

    public CompanyService(String solrCoreName) {
        super();
        this.solrCoreName = solrCoreName;
    }

    public List<Map<String, Object>> getCompanyValues(String id, String version) {
        int currentVersion;
        String value;
        JsonReader jsonReader;
        Object object;
        JsonArray jsonArray;
        JsonObject jsonObject;
        List<Map<String, Object>> companyMap = null;
        JsonParser jsonParser = new JsonParser();
        currentVersion = 1;
        value = HttpUtil.getRequest(getEndpoint(currentVersion));
        jsonReader = new JsonReader(new StringReader(value));
        object = jsonParser.parse(jsonReader);
        jsonArray = (JsonArray) object;
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject = jsonArray.get(i).getAsJsonObject();
            if (jsonObject.get("id").getAsString().equals(id)) {
                companyMap = new ArrayList<>();
                Map<String, Object> company = new HashMap<>();
                company.put("id", jsonObject.get("id").getAsInt());
                company.put("version", jsonObject.get("version").getAsInt());
                company.put("name", jsonObject.get("name").getAsString());
                companyMap.add(company);
                break;
            }
        }
        return companyMap;
    }

    private String getEndpoint(int version) {
        return REPO_URL.replaceAll("#VERSION", String.valueOf(version));
    }
}
