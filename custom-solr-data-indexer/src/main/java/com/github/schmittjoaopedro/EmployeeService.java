package com.github.schmittjoaopedro;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.joda.time.LocalDateTime;
import org.restlet.data.MediaType;

import java.io.StringReader;
import java.util.*;

public class EmployeeService {

    private String solrCoreName;

    private static final String REPO_URL = "https://raw.githubusercontent.com/schmittjoaopedro/joaoschmitt.wordpress.com/master/custom-solr-data-indexer/assets/employee_v#VERSION.json";

    public EmployeeService(String solrCoreName) {
        super();
        this.solrCoreName = solrCoreName;
    }

    public List<Map<String, Object>> getEmployeesAfterDate(Date startDate) {
        // For this sample the startDate will not be considered, but in many real world scenarios this information is important
        int currentVersion = 1; // Full DUMP
        List<Map<String, Object>> employeeMap = getJsonObject(currentVersion);
        return employeeMap;
    }

    public List<Map<String, Object>> getDeltaEmployees(LocalDateTime startDate) {
        // For this sample the startDate will not be considered, but in many real world scenarios this information is important
        int currentVersion = 2; // Modified and created delta records
        List<Map<String, Object>> employeeMap = getJsonObject(currentVersion);
        return employeeMap;
    }

    public List<Map<String, Object>> getDeleteEmployees(LocalDateTime startDate) {
        // For this sample the startDate will not be considered, but in many real world scenarios this information is important
        int currentVersion = 3; // Records to delete
        List<Map<String, Object>> employeeMap = getJsonObject(currentVersion);
        return employeeMap;
    }

    public List<Map<String, Object>> getEmployee(String id, String version) {
        List<Map<String, Object>> objects = getJsonObject(Integer.valueOf(version));
        List<Map<String, Object>> objectFiltered = new ArrayList<>();
        for (Map<String, Object> object : objects) {
            if (String.valueOf(object.get("id")).equals(id)) {
                objectFiltered.add(object);
                break;
            }
        }
        return objectFiltered;
    }

    private List<Map<String, Object>> getJsonObject(int currentVersion) {
        String value;
        JsonReader jsonReader;
        Object object;
        JsonArray jsonArray;
        JsonObject jsonObject;
        List<Map<String, Object>> employeeMap = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();
        value = HttpUtil.getRequest(getEndpoint(currentVersion));
        jsonReader = new JsonReader(new StringReader(value));
        object = jsonParser.parse(jsonReader);
        jsonArray = (JsonArray) object;
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject = jsonArray.get(i).getAsJsonObject();
            Map<String, Object> company = new HashMap<>();
            company.put("id", jsonObject.get("id").getAsInt());
            company.put("version", jsonObject.get("version").getAsInt());
            company.put("firstName", jsonObject.get("firstName").getAsString());
            company.put("lastName", jsonObject.get("lastName").getAsString());
            company.put("age", jsonObject.get("age").getAsString());
            company.put("companyId", jsonObject.get("companyId").getAsInt());
            employeeMap.add(company);
        }
        return employeeMap;
    }

    private String getEndpoint(int version) {
        return REPO_URL.replaceAll("#VERSION", String.valueOf(version));
    }
}
