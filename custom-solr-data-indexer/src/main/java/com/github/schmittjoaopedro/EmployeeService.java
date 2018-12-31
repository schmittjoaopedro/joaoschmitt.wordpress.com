package com.github.schmittjoaopedro;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.joda.time.LocalDateTime;

import java.io.StringReader;
import java.util.*;

public class EmployeeService {

    private String solrCoreName;

    public EmployeeService(String solrCoreName) {
        super();
        this.solrCoreName = solrCoreName;
    }

    public List<Map<String, Object>> getEmployeesAfterDate(Date startDate) {
        // For this sample the startDate will not be considered, but in many real world scenarios this information is important
        String file = "employee_v1_full";// Full DUMP
        List<Map<String, Object>> employeeMap = getJsonObject(file);
        return employeeMap;
    }

    public List<Map<String, Object>> getDeltaEmployees(LocalDateTime startDate) {
        // For this sample the startDate will not be considered, but in many real world scenarios this information is important
        String file = "employee_v2_modified";// Modified and created delta records
        List<Map<String, Object>> employeeMap = getJsonObject(file);
        return employeeMap;
    }

    public List<Map<String, Object>> getDeleteEmployees(LocalDateTime startDate) {
        // For this sample the startDate will not be considered, but in many real world scenarios this information is important
        String file = "employee_v2_removed"; // Records to delete
        List<Map<String, Object>> employeeMap = getJsonObject(file);
        return employeeMap;
    }

    // Return full data about a single employee
    public List<Map<String, Object>> getEmployee(String id, String version) {
        String file = "employee_v" + version + "_full";
        List<Map<String, Object>> objects = getJsonObject(file);
        List<Map<String, Object>> objectFiltered = new ArrayList<>();
        for (Map<String, Object> object : objects) { // Filter the equivalent record from the database result (in memory)
            if (String.valueOf(object.get("id")).equals(id)) {
                objectFiltered.add(object);
                break;
            }
        }
        return objectFiltered;
    }

    private List<Map<String, Object>> getJsonObject(String file) {
        String value;
        JsonReader jsonReader;
        Object object;
        JsonArray jsonArray;
        JsonObject jsonObject;
        List<Map<String, Object>> employeeMap = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();
        value = HttpUtil.getRequest(file); // Get from GIT repo
        jsonReader = new JsonReader(new StringReader(value));
        object = jsonParser.parse(jsonReader);
        jsonArray = (JsonArray) object;
        // Convert from JSON to Map structure (used by SOLR)
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
}
