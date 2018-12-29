package com.github.schmittjoaopedro;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class CompanyServiceTest {

    private static CompanyService companyService;

    @BeforeClass
    public static void beforeClass() {
        companyService = new CompanyService("teste");
    }

    @Test
    public void testGetVersion1() {
        List<Map<String, Object>> data = companyService.getCompanyValues("1", "1");
        Assert.assertEquals(data.size(), 1, 0);
        Assert.assertEquals((int) data.get(0).get("id"), 1, 0);
        Assert.assertEquals((int) data.get(0).get("version"), 1, 0);
        Assert.assertEquals(data.get(0).get("name"), "ACME");

        data = companyService.getCompanyValues("2", "1");
        Assert.assertEquals(data.size(), 1, 0);
        Assert.assertEquals((int) data.get(0).get("id"), 2, 0);
        Assert.assertEquals((int) data.get(0).get("version"), 1, 0);
        Assert.assertEquals(data.get(0).get("name"), "XPTO");
    }

}
