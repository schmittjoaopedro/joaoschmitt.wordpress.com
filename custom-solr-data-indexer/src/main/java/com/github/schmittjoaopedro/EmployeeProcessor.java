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
        // In the context object we receive a lot of metadata to support
        // our custom import handler operation.
        // For example, here we are storing the SOLR core name being
        // indexed (in case of multiples cores this information can be
        // useful to reuse source code).
        employeeService = new EmployeeService(context.getSolrCore().getName());
    }

    // SOLR always call this method to retrieve the next row to be processed.
    // In the first call (when a full or delta import is called) the rowIterator
    // is null, then at this time, we query the external service to load the data
    // to be processed.
    @Override
    public Map<String, Object> nextRow() {
        if (rowIterator == null) {
            initEmployees(); // Load all employees to be indexed in the SOLR index
        }
        return getNext(); // Get next record to be indexed
    }

    // This method is called to identify what entries must be created or updated.
    // In this case, a smart query can be used to load only the ID information,
    // because after that for each entry the nextRow() method will be called again
    // to retrieve the full entry to be indexed.
    @Override
    public Map<String, Object> nextModifiedRowKey() {
        if (rowIterator == null) {
            initModifiedEmployees();
        }
        return getNext();
    }

    // This method is called after the nextModifiedRowKey() have finished to define
    // the records to be deleted. Then SOLR can make the difference between modified
    // records and removed ones to save processing time.
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
        if (Context.FULL_DUMP.equals(context.currentProcess())) { // If the full-import was selected in the combobox
            // Define a start date to consider the database, this information could be ignored if necessary
            startDate = new DateTime(1990, 01, 01, 0, 0).toDate();
            employeesList = employeeService.getEmployeesAfterDate(startDate);
            LOG.info("Full import of " + employeesList.size() + " employees");
            rowIterator = employeesList.iterator();
        } else if (Context.DELTA_DUMP.equals(context.currentProcess())) { // If the delta-import was selected in the combobox
            // Get information about what record we want to update, this method is called for each entry.
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
        // After the import was finished, the last_index_time is updated with the new date value for the next queries.
        String dateString = context.getVariableResolver().resolve("dih.employee.last_index_time").toString();
        LocalDateTime startDate = LocalDateTime.parse(dateString, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        List<Map<String, Object>> materialList = employeeService.getDeltaEmployees(startDate); // Return the delta records modified
        if (!materialList.isEmpty()) {
            LOG.info("Found " + materialList.size() + " modified groupers");
            rowIterator = materialList.iterator();
        }
    }

    private void initDeleteEmployees() {
        // Select next data to delete based on last_index_time information kept in the dataimport.properties files.
        // After the import was finished, the last_index_time is updated with the new date value for the next queries.
        String dateString = context.getVariableResolver().resolve("dih.employee.last_index_time").toString();
        LocalDateTime startDate = LocalDateTime.parse(dateString, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        List<Map<String, Object>> materialList = employeeService.getDeleteEmployees(startDate); // Return the records to delete
        if (!materialList.isEmpty()) {
            LOG.info("Found " + materialList.size() + " modified groupers");
            rowIterator = materialList.iterator();
        }
    }
}
