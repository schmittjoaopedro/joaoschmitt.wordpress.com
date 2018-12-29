# SOLR Configuration

Download latest version (in this example the version was 7.6.0) from the official [SOLR link](http://www.apache.org/dyn/closer.lua/lucene/solr/7.6.0/solr-7.6.0.tgz).

Extract the binary to some folder from your file system, e.g. *C:\java\solr-7.6.0*.

Start the SOLR using the command line:

> cd C:\java\solr-7.6.0\bin <br /> solr start

After that, create a new core using the following command:

> solr create_core -c solr-custom-data-indexer-core

Then, create a folder *lib* and copy the required dependency that will supply the custom solr extensions.

> cd server\solr\solr-custom-data-indexer-core <br />mkdir lib

Then download and copy the following jars into the lib folder:
* [ojdbc13.jar](solr-configuration/lib/ojdbc14.jar)
* [solr-dataimporterhandler-7.6.0](solr-configuration/lib/solr-dataimporthandler-7.6.0.jar)

# Configuring Managed Schema

As we are simulating here a example scenario, the data is not real (company and employee).
The data used here is available on the [Assets ](assets) folder.
The dataset contains a relationship between employees with departments (by means of the *departmentId* field).
To persist this data in our SOLR core, first is necessary to configure the *managed-schema* file.
Therefore add the following entries at the *managed-schema* file after the id entry.

```xml
...
<field name="id" type="string" indexed="true" stored="true" required="true" multiValued="false" />
<field name="firstName" type="string" indexed="true" stored="true" multiValued="false" />
<field name="lastName" type="string" indexed="true" stored="true" multiValued="false" />
<field name="age" type="pint" indexed="true" stored="true" multiValued="false" />
<field name="departmentId" type="pint" indexed="true" stored="true" multiValued="false" />
<field name="departmentName" type="string" indexed="true" stored="true" multiValued="false" />
...
```

# Configuring solrconfig.xml

Here we will need to add a new entry to determine the dataimport handler support.
Add the following entry in the *solrconfig.xml* file:

```xml
...
<requestHandler name="/dataimport" class="org.apache.solr.handler.dataimport.DataImportHandler">
    <lst name="defaults">
        <str name="config">data-config.xml</str>
    </lst>
</requestHandler>
...
```

And create the *data-config.xml* file inside the */conf* folder inside the solr core.
The *data-config.xml* file hold the configuration that determine how the data import handler will process the data to be indexed.
Our file is customized because we are extending the default engine of SOLR to support a indexation mechanins by REST service.
The following content must to be put inside the *data-config.xml* file.

```xml
<dataConfig>
    <document>
        <entity
            name="employee"
            pk="id"
            query="true"
            deltaQuery="true"
            deltaImportQuery="true"
            deletedPkQuery="true"
            processor="com.github.schmittjoaopedro.EmployeeProcessor" >
            <entity 
                name="company"
                query="true"
                processor="com.github.schmittjoaopedro.CompanyProcessor">
            </entity>
            <field name="id" column="id" />
            <field name="firstName" column="firstName" />
            <field name="lastName" column="lastName" />
            <field name="age" column="age" />
            <field name="departmentId" column="departmentId" />
            <field name="departmentName" column="departmentName" />
        </entity>
    </document>
</dataConfig>
```

# Executing custom jar on SOLR

To execute our custom data indexer on SOLR, first build the application using the maven command:

> mvn clean package

After that, copy the .jar generated under the lib folder into the SOLR core folder (C:\java\solr-7.6.0\server\solr\solr-custom-data-indexer-core\lib).
Besides that, copy the following dependencies too:

* https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.5
* https://mvnrepository.com/artifact/org.jboss.resteasy/resteasy-client/3.6.2.Final

To debug our source code start the solr using the following commands. First stop all SOLR instances:

> solr stop -all

Then start a new instance in debug mode:

> solr start -a "-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=4044"

And configure a remote debug in IntelliJ (or eclipse) like the following image:

![Remote solr debug](imgs/remote_solr_debug.png)

