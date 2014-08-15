# QAFE Platform
QAFE Platform is a powerful web application that is able to interpret QAML files and serves the interpreted output to website visitors. By default the interpreted output is a GWT application.

Additionally we offer a service to convert Oracle Forms to QAML files to be used with the QAFE Platform. Converting Oracle Forms to ADF is also an option. More information can be found on the [QAFE Website](http://www.qafe.com/ "QAFE Website") or on our [GitHub Page](http://qafedev.github.io/ "QAFE GitHub Page"). 

**Table of contents**

- Checking out from git
- Building QAFE Platform
- Running QAFE Platform applications
- Sample code 
- Further Reading
- Licenses

## Checking out from git

First create a directory in which you going to develop 

(for example on a Mac if username is johndoe: /Users/johndoe/develop/qafe/)

```
mkdir -p /Users/johndoe/develop/qafe/
```

Initialize the directory

```
git init
```

Checkout the source for the development branch from git

```
git clone -b develop https://github.com/qafedev/qafe-platform.git

```

The source code can be found in qafe-platform directory
(so /Users/johndoe/develop/qafe/qafe-platform/


## Building QAFE Platform
Maven version 3.x is used as a project management tool for the QAFE Platform. Maven is an open-source project freely distributed by the Apache Software Foundation. The tool is necessary to be able to build the platform. Before building, make sure that the following tasks are done:

1. Make sure that Maven is installed on your machine. On OSX Mavericks, Maven is not shipped automatically. For this the following can work:
```
brew install maven
```
For other platforms, the latest Maven-binaries can be found [here](http://maven.apache.org/download.cgi). 

1. The location of the local maven repository needs to be changed. In the qafe-platform/settings.xml locate the <localRepository> tag. By doing this you won't harm other maven projects on your system. Check it to for example:
```
<localRepository>/Users/johndoe/develop/qafe/m2/</localRepository>
``` 

1. After setting the proper location of the local Maven repository, you can start the following command to build the platform:
```
mvn clean install -DskipTests
```
The build time should be approximately 10 minutes on a modern machine. The skiptests in included since database testing using a real database needs to be skipped. The end product of the building the platform are two WAR (Web Application Archive) files, one using GWT and one using Mobile GWT. These WAR-files can be run on most web servers including Apache Tomcat, Jetty, Weblogic, Jboss and Glassfish.

**Note**: The Oracle JDBC driver is not allowed to be distributed by maven repositories. For this follow [this link](http://www.mkyong.com/maven/how-to-add-oracle-jdbc-driver-in-your-maven-local-repository/) to make it work. 


## Running QAFE Platform applications

**TODO** add steps for running and browser tests.

## Sample Code

```
<application-mapping xmlns="http://qafe.com/schema"	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"	xsi:schemaLocation="http://qafe.com/schema http://www.qafe.com/schema/2.2/application-mapping.xsd">
	<presentation-tier>
		<view>
			<window id="window1" displayname="Hello World" width="300"  height="250" >
				<rootpanel id="myRootPanel">
					<verticallayout>
						<button id="mylabel" displayname="Hello World" />
						<label id="sourcecode" displayname="Click here for the source code" />
						<panel id="sourcecodepanel" visible="false">
							<autolayout>	
						 <textarea rows="30" height="400px" width="800px" editable="false">
									<value/>
								</textarea>
							</autolayout>
						</panel>
					</verticallayout>
				</rootpanel>
				<events>
					<event id="sourceCodeEvent">
	  					<listeners>
	  						<listenergroup>
	  							<component ref="sourcecode"/>
	  							<listener type="onclick"/>
	  						</listenergroup>
	  					</listeners>
	  					<toggle>
	  						<component ref="sourcecodepanel"/>
	  					</toggle>  					
  					</event>
				</events>
			</window>
		</view>
	</presentation-tier>
</application-mapping> 
```

## Further Reading
Further information regarding the QAML documentation and Forms Conversion can be found on our [Developer Docs](http://www.qafe.com/developer-docs/ "QAFE Developer Docs")

## Licenses
Information regarding the licenses used in the QAFE Platform can be found on the [Depedencies page](https://github.com/qafedev/qafe-platform/blob/master/dependencies.md "QAFE dependencies") on GitHub. 
 