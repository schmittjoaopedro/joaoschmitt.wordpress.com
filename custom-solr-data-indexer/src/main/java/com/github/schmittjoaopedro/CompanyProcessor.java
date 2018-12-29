package com.github.schmittjoaopedro;

import org.apache.solr.handler.dataimport.Context;
import org.apache.solr.handler.dataimport.EntityProcessorBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;

public class CompanyProcessor extends EntityProcessorBase {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private CompanyService companyService;

    @Override
    public void init(Context context) {
        super.init(context);
        companyService = new CompanyService(context.getSolrCore().getName());
    }

    @Override
    public Map<String, Object> nextRow() {
        if (rowIterator == null) {
            initCompanyValues();
        }
        return getNext();
    }

    private void initCompanyValues() {
        String id = String.valueOf(context.getVariableResolver().resolve("employee.companyId"));
        String version = String.valueOf(context.getVariableResolver().resolve("employee.version"));
        if (isValidParam(id) && isValidParam(version)) {
            rowIterator = companyService.getCompanyValues(id, version).iterator();
            LOG.info("Loading values of company " + id + "/" + version);
        } else {
            LOG.info("Not found values of company " + id + "/" + version);
        }
    }

    private boolean isValidParam(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
