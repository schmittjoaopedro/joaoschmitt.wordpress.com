package com.github.schmittjoaopedro;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class EmployeeServiceTest {

    private static EmployeeService employeeService;

    @BeforeClass
    public static void beforeClass() {
        employeeService = new EmployeeService(null);
    }

    @Test
    public void testEmployeeServiceFull() {
        List<Map<String, Object>> fullDump = employeeService.getEmployeesAfterDate(null);
        Assert.assertEquals(fullDump.size(), 5, 0);
    }

    @Test
    public void testEmployeeServiceModified() {
        List<Map<String, Object>> fullDump = employeeService.getDeltaEmployees(null);
        Assert.assertEquals(fullDump.size(), 5, 0);
    }

    @Test
    public void testEmployeeServiceDelete() {
        List<Map<String, Object>> fullDump = employeeService.getDeleteEmployees(null);
        Assert.assertEquals(fullDump.size(), 1, 0);
    }

    @Test
    public void testEmployeeServiceSpecific() {
        List<Map<String, Object>> fullDump = employeeService.getEmployee("3", "1");
        Assert.assertEquals(fullDump.size(), 1, 0);
        Assert.assertEquals((int) fullDump.get(0).get("id"), 3, 0);
    }

}
