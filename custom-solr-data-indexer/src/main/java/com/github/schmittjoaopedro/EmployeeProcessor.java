package com.github.schmittjoaopedro;

import org.apache.solr.handler.dataimport.Context;
import org.apache.solr.handler.dataimport.EntityProcessorBase;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EmployeeProcessor extends EntityProcessorBase {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private EmployeeService employeeService;

    @Override
    public void init(Context context) {
        super.init(context);
        employeeService = new EmployeeService(context.getSolrCore().getName());
    }

    @Override
    public Map<String, Object> nextRow() {
        if (rowIterator == null) {
            initEmployees(); // Load all employees to be indexed in the SOLR index
        }
        return getNext(); // Get next record to be indexed
    }

    @Override
    public Map<String, Object> nextModifiedRowKey() {
        if (rowIterator == null) {
            initModifiedEmployees();
        }
        return getNext();
    }

    @Override
    public Map<String, Object> nextDeletedRowKey() {
        if (rowIterator == null) {
            initDeleteEmployees();
        }
        return getNext();
    }

    private void initEmployees() {
        Date startDate;
        List<Map<String, Object>> employeesList;
        String id;
        String revision;
        if (Context.FULL_DUMP.equals(context.currentProcess())) {
            // Define a start date to consider the database, this information could be ignored if necessary
            startDate = new DateTime(1990, 01, 01, 0, 0).toDate();
            employeesList = employeeService.getEmployeesAfterDate(startDate);
            LOG.info("Full import of " + employeesList.size() + " employees");
            rowIterator = employeesList.iterator();
        } else if (Context.DELTA_DUMP.equals(context.currentProcess())) {
            // Get information about what record we want to update
            id = String.valueOf(context.getVariableResolver().resolve("dih.delta.id"));
            revision = String.valueOf(context.getVariableResolver().resolve("dih.delta.version"));
            // We expect one record to be updated
            employeesList = employeeService.getEmployee(id, revision);
            if (employeesList.size() == 1) {
                LOG.info("Delta header of grouper " + employeesList.get(0).get("id") + "");
                rowIterator = employeesList.iterator();
            }
        }
    }

    private void initModifiedEmployees() {
        // Select next data to updated based on last_index_time information kept in the dataimport.properties files.
        String dateString = context.getVariableResolver().resolve("dih.employee.last_index_time").toString();
        LocalDateTime startDate = LocalDateTime.parse(dateString, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        List<Map<String, Object>> materialList = employeeService.getDeltaEmployees(startDate);
        if (!materialList.isEmpty()) {
            LOG.info("Found " + materialList.size() + " modified groupers");
            rowIterator = materialList.iterator();
        }
    }

    private void initDeleteEmployees() {
        // Select next data to delete based on last_index_time information kept in the dataimport.properties files.
        String dateString = context.getVariableResolver().resolve("dih.employee.last_index_time").toString();
        LocalDateTime startDate = LocalDateTime.parse(dateString, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        List<Map<String, Object>> materialList = employeeService.getDeleteEmployees(startDate);
        if (!materialList.isEmpty()) {
            LOG.info("Found " + materialList.size() + " modified groupers");
            rowIterator = materialList.iterator();
        }
    }
}
