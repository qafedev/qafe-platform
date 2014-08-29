# QAFE Platform
QAFE Platform is a powerful web application that is able to interpret QAML files and serves the interpreted output to website visitors. By default the interpreted output is a GWT application.

Additionally we offer a service to convert Oracle Forms to QAML files to be used with the QAFE Platform. Converting Oracle Forms to ADF is also an option. More information can be found on the [QAFE Website](http://www.qafe.com/ "QAFE Website") or on our [GitHub Page](http://qafedev.github.io/ "QAFE GitHub Page"). 

**Table of contents**

- Checking out from git
- Building QAFE Platform
- Modifying QAFE Platform
- Debugging QAFE Platform
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

Initialize local repository

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

For Building QAFE Java is needed. Java6 can be found [here](http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase6-419409.html) for Mac OSX Mavericks see this [link](http://support.apple.com/kb/DL1572) 

Maven version 3.x is used as a project management tool for the QAFE Platform. Maven is an open-source project freely distributed by the Apache Software Foundation. The tool is necessary to be able to build the platform. Before building, make sure that the following tasks are done:

1\. Make sure that Maven is installed on your machine. On Mac OS X Mavericks, Maven is not shipped automatically. To install Maven on OS X, the following command should work:
```
brew install maven
```
For other platforms, the latest Maven-binaries can be found [here](http://maven.apache.org/download.cgi). 

2\. The location of the local maven repository needs to be changed. In the qafe-platform/settings.xml locate the <localRepository> tag. By doing this you won't harm other maven projects on your system. Check it to for example:
```
<localRepository>/Users/johndoe/develop/qafe/m2/</localRepository>
``` 

3\. After setting the proper location of the local Maven repository, you can start the following command to build the platform:
```
mvn clean install -DskipTests
```
The build time should be approximately 10 minutes on a modern machine. The skiptests in included since database testing using a real database needs to be skipped. The end product of the building the platform are two WAR (Web Application Archive) files, one using GWT and one using Mobile GWT. These WAR-files can be run on most web servers including Apache Tomcat, Jetty, Weblogic, Jboss and Glassfish.

**Note**: The Oracle JDBC driver is not allowed to be distributed by maven repositories. For this follow [this link](http://www.mkyong.com/maven/how-to-add-oracle-jdbc-driver-in-your-maven-local-repository/) to make it work. 

## Modifying QAFE Platform
The QAFE Platform project can be imported in each IDE supporting Maven. Import the pom.xml in the folder *qafe-platform* in your IDE of choice as an existing Maven project. The code including the proper dependencies should be ready to use.  

## Debugging QAFE Platform
Server-side code can be debugged using the debug-settings in Eclipse. Client-side code on the other hand needs additional steps to properly debug. This is done using the Super Dev Mode-functionality of GWT. This enables client-side debugging in the Chrome browser without the need for any plug-ins. Make sure that the platform-project has been built successfully before debugging. First step is to start the GWT Code server using the following Maven- command in the platform/qafe-web-gwt-folder. **Note: Debugging currently only works in Chrome.**

```
mvn gwt:run-codeserver
```  

Open the link stated at the end of the Maven-command. This opens up a web page, which includes two bookmarklets. Add those bookmarklets to your bookmarks-bar by dragging them to bar. If this does not work, copy the link location of the bookmarklets and create the necessary bookmarks manually.

The next step is to start QAFE-Web-GWT with the following Maven-command in the platform/qafe-web-gwt-folder.

```
mvn gwt:run
```

This should open the application in your browser. Now enable Super Dev Mode by opening the Dev Mode On-bookmark. You should now be able to debug the client-side code by using the Chrome Developer Tools. (Menu --> Tools --> Developer Tools or use the shortcut: F12) All client-side Java code can be debugged from the source-tab. Debugging i done by selecting a file and clicking on the line number creating a breakpoint.  

## Running QAFE Platform applications
The WAR-file for the QAFE-platform based on GWT is found in the target-folder of the qafe-webapps project after building the platform. This file contains the platform and a basic Hello World application. A web server is necessary to run the application, as discussed in the build-section. Copy the file to the application-folder of your web server (i.e. the webapps-folder in Tomcat) and run the server. 

The link to the application is dependent on the port number specified in your web server and the filename of the WAR-file without the extension. For example, if the port number is 8080 (standard for Tomcat) and the filename is qafe-web-gwt.war, then the link would be:

```
http://localhost:8080/qafe-web-gwt/
```

## Sample Code
The sample code underneath is a simple Hello World application which includes a window with a button and a textfield inside. When the user clicks on the button, a dialog panel will show up containing "Hello World" as the title and the content of the textfield as a message.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<application-mapping xmlns="http://qafe.com/schema"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://qafe.com/schema http://www.qafe.com/schema/2.2/application-mapping.xsd">
	<presentation-tier>
		<view>
			<window id="window1" displayname="Hello World" width="300"
				height="250">
				<rootpanel id="myRootPanel">
					<verticallayout>
						<textfield id="myTextfield"/>
						<button id="myButton" displayname="Hello World" />
					</verticallayout>
				</rootpanel>
				<events>
					<event id="sourceCodeEvent">
						<listeners>
							<listenergroup>
								<component ref="myButton" />
								<listener type="onclick" />
							</listenergroup>
						</listeners>
						<dialog>
							<title value="Hello World"/>
							<message ref="myTextfield" src="component"/>
						</dialog>
					</event>
				</events>
			</window>
		</view>
	</presentation-tier>
</application-mapping>  
```

## Further Reading
Further information regarding the QAML documentation and Forms Conversion can be found on our [QAFE Developer Documentation](https://github.com/qafedev/qafedev.github.io/blob/master/README.md "QAFE Developer Documentation")

## Licenses
Information regarding the licenses used in the QAFE Platform can be found on the [Depedencies page](https://github.com/qafedev/qafe-platform/blob/master/dependencies.md "QAFE dependencies") on GitHub. 
 
