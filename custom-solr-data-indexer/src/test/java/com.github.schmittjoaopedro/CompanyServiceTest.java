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
        Assert.assertEquals((int) data.get(0).get("companyId"), 1, 0);
        Assert.assertEquals((int) data.get(0).get("companyVersion"), 1, 0);
        Assert.assertEquals(data.get(0).get("companyName"), "ACME");

        data = companyService.getCompanyValues("2", "1");
        Assert.assertEquals(data.size(), 1, 0);
        Assert.assertEquals((int) data.get(0).get("companyId"), 2, 0);
        Assert.assertEquals((int) data.get(0).get("companyVersion"), 1, 0);
        Assert.assertEquals(data.get(0).get("companyName"), "XPTO");
    }

}
