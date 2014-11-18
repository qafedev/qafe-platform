# QAFE platform webservice
The QAFE platform webservice exposes a running QAFE application with a SOAP-based API.

**Table of contents**
* [1. Building the QAFE webservice](#1-building-the-qafe-webservice)
* [2. Running the QAFE webservice](#2-running-the-qafe-webservice)
* [3. Using the QAFE webservice](#3-using-the-qafe-webservice)
* [4. Sample Code](#4-sample-code)
* [5. Create an QAFE application using the QAFE webservice]()
* [6. Updating QAFE webservice on existing QAFE projects]

## 1. Building the QAFE webservice
For Building QAFE webservice, Java 7 is needed. See [this link for the downloads](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)

Maven version 3.x is used as a project management tool for the QAFE webservice. Maven is an open-source project freely distributed by the Apache Software Foundation. The tool is necessary to be able to build the platform. Before building, make sure that the following tasks are done:

1\. Make sure that Maven is installed on your machine. The latest Maven-binaries can be found [here](http://maven.apache.org/download.cgi).

**Note:** On Mac OS X Mavericks, Maven is not shipped by default. To install Maven on OS X, the following command should work:
```
brew install maven
```

2\. The location of the local maven repository needs to be changed. In the qafe-platform/settings.xml locate the <localRepository> tag. By doing this you won't harm other maven projects on your system. Set it to for example (Windows machine):
```
<localRepository>/Users/johndoe/develop/qafe/m2/</localRepository>
``` 

3\. After setting the proper location of the local Maven repository, you can start the following command to build the platform:
```
mvn clean install
```
The build time should take less than a minute on a modern machine. The end product of the building the webservice is a WAR (Web Application Archive) file. The WAR-file can be run on most web servers including Apache Tomcat, Jetty, Weblogic, Jboss and Glassfish.

## 2. Running the QAFE webservice
The WAR-file for the QAFE-webservice is found in the target-folder of the project after the build process is completed. A web server is necessary to run the application, as discussed in the build-section. Copy the file to the application-folder of your web server (i.e. the webapps-folder in Tomcat) and run the server. 

The endpoint of the SOAP webservice is dependent on the port number specified in your web server and the configuration settings in the ${webservice-root}/WEB-INF/sun-jaxws.xml and ${webservice-root}/WEB-INF/web.xml files. For example if the port number is 8080 (standard for Tomcat) and if using the default webservice configuration, the endpoint would be:
```
http://domain.tld:8080/qafeWS
```

## 3. Using the QAFE webservice
Through the webservice a business action can be run against the running application, defined by QAML files. In order to do so the executeBusinessAction SOAP method can be invoked against the running webservice.
The executeBusinessAction method takes 3 arguments:
- The appId (String)
- The businessActionId (String)
- The input variables (defined in the QAML file) for the businessAction as a JSON string.

To test the webservice, [SOAP UI](http://www.soapui.org) can be used.

## 4. Sample Code
For example, observe the definition of this QAFE application (QAML formatted):
```
<?xml version="1.0" encoding="UTF-8"?>
<application-mapping xmlns="http://qafe.com/schema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://qafe.com/schema http://www.qafe.com/schema/application-mapping.xsd">  	
	<business-tier> 
		<business-actions>
			<business-action id="selectEmployeeBySalary"> 
				<transaction managed="no"/>  
				<service ref="hrXE" method-ref="selectEmployeeBySalary"> 
					<in name="salary" ref="salary" />
					<out name="result" ref="result"/> 
				</service> 
			</business-action>  
		</business-actions> 
	</business-tier>  
	<integration-tier> 
		<services> 
			<service id="hrXE" resource-ref="hrXEResource"> 
				<method id="selectEmployeeBySalary" name="selectEmployeeBySalary">
					<in name="salary" ref="salary"/>
					<out name="result"/>
				</method>
			</service> 
		</services> 
	</integration-tier>  
	<resource-tier> 
		<resources> 
			<drivermanager-datasource id="hrXEResource" url="jdbc:oracle:thin:@localhost:1521:xe" username="hr" password="hr" driver-classname="oracle.jdbc.OracleDriver" statements-file-url="sample-sql.xml"/> 
		</resources> 
	</resource-tier> 
</application-mapping>
```

As you can see the resource is defined as an oracle database, it also contains some statements defined in an xml-formatted file:
```
<?xml version="1.0" encoding="UTF-8"?>
<statements xmlns="http://qafe.com/schema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://qafe.com/schema http://www.qafe.com/schema/application-statements.xsd">  
	<select id="selectEmployeeBySalary"><![CDATA[select * from EMPLOYEES where SALARY < :salary]]></select>
</statements>
```

If we wanted to invoke the selectEmployeeBySalary business-action, the SOAP call would look like this:
```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://soap.ws.qualogy.com/">
   <soapenv:Header/>
   <soapenv:Body>
	  <soap:executeBusinessAction>
		<arg0>demoApp</arg0>
		<arg1>selectEmployeeBySalary</arg1>
		<arg2>{
			"salary":12000
		}</arg2>
	  </soap:executeBusinessAction>
   </soapenv:Body>
</soapenv:Envelope>
```

## 5. Create an QAFE application using the QAFE webservice
See the [QAFE Platform README](https://github.com/qafedev/qafe-platform/blob/develop/README.md#create-an-qafe-application-using-the-qafe-platform) documentation for more information on how to create QAFE applications.

## 6. Updating QAFE webservice on existing QAFE projects
See the [QAFE Platform README](https://github.com/qafedev/qafe-platform/blob/develop/README.md#updating-qafe-platform-on-existing-qafe-projects) documentation for more information on how to update the QAFE webservice on existing QAFE projects.
