# QAFE Platform
QAFE Platform is a powerful web application that is able to interpret QAML files and serves the interpreted output to website visitors. By default the interpreted output is a GWT application.

Additionally we offer a service to convert Oracle Forms to QAML files to be used with the QAFE Platform. Converting Oracle Forms to ADF is also an option. More information can be found on the [QAFE Website](http://www.qafe.com/ "QAFE Website") or on our [GitHub Page](http://qafedev.github.io/ "QAFE GitHub Page"). 

# Prerequisites

- Git: [Tutorial](http://git-scm.com/book/en/v2/) - [Downloads](https://git-scm.com/downloads) - [Download Windows Tool (Git Bash)](https://msysgit.github.io/)
- Maven 3.x: [Tutorial](https://maven.apache.org/guides/getting-started/) - [Downloads](https://maven.apache.org/download.cgi)
- Java 1.6.x: [Downloads](http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase6-419409.html)
- Google Web Toolkit 2.5.1: [Tutorial](http://www.gwtproject.org/doc/latest/DevGuide.html)

**Optional but recommended**

- Eclipse Kepler: [Downloads](http://eclipse.org/kepler/)
- Google Chrome

# Setting up the IDE

## Getting the software

To get started, first we need to checkout the source code from git.
The source code can be found in GitHub under the following url: *https://github.com/qafedev/qafe-platform.git*

After determining your development directory and browsing to that directory with a Git tool, you should run the following command to checkout the development branch:
```
git clone -b develop https://github.com/qafedev/qafe-platform.git
```
After running this command the source code can be found in the *qafe-platform* subdirectory.

## Building the platform

Next we need to build the QAFE platform.

**Prerequisites**: Following Oracle libraries needs to be available in your privately maintained maven repository. Reason is because, libraries provided by Oracle are not allowed to be distributed by public maven repositories.
 
- ojdbc5-11.1.0.7.0.jar
- orai18n-11.2.0.3.jar
- coherence-3.7.1.0b27797.jar

Once the above mentioned libraries are in your maven repository, make entry in the parent pom or introduce settings.xml file with credentials for the privately maintained maven repository so that these libraries are available during the build process.

Open your Maven tool and browse to the *qafe-platform* directory containing the *pom.xml* file. Run the following command to build to QAFE platform: 
```
mvn clean install -DskipTests
```
The build should take approximately 10 minutes on a modern machine. The *-DskipTests* is included because database testing using a real database needs to be skipped. The end product of the building the platform are two WAR files, one using GWT and one using Mobile GWT. These WAR files can be run on most web servers including Apache Tomcat, Jetty, Weblogic, JBoss and Glassfish.



##  Importing the software

The QAFE Platform project can be imported in each IDE supporting Maven. Import the *pom.xml* located inside the *qafe-platform* folder to your IDE of choice (as an existing Maven project). The code including the proper dependencies should be ready to use.  

### Eclipse

It's possible Eclipse asks to install certain Maven plugins during the import. These plugins should be installed else the platform might not work.

# Debugging code

## Eclipse

### Server

Before you start debugging make sure the project has been built successfully.
Go to 'Debug Configurations', select the the **first** qafe-web-gwt under Java Application and press 'Debug'.
The qafe-web-gwt is a configuration that should already be available in Eclipse for this project.

A possible place for debugging the server is the `public void init(ServletContext servletContext)` method in the `com.qualogy.qafe.web.ContextLoader` class. This is the starting point for loading the server.

**Note:** If you want to debug both server and client, press the 'Launch Default Browser' button. Next, remove everything of the url after the *hostname:port*. Finally, skip the next chapter until after the `mvn gwt:run-codeserver` part.

### Debugging client-side code in the browser

Client-side code needs additional steps to debug. It uses the 'Super Dev Mode' funtionality of GWT. This enables client-side debugging in the Google Chrome browser without the need for any plug-ins. Currently only browsers that support sourcemaps allow for debugging client-side code. In this section we've used Google Chrome for explaining things. 

Before you start debugging make sure the project has been built successfully. 
The first step is to start the GWT codeserver using the following Maven command on the *platform/qafe-web-gwt* folder:
```
mvn gwt:run-codeserver
```
When the code server has been started successfully open the link stated at the end of the console output. This opens up a web page, which includes two bookmarklets. Add those bookmarklets to your bookmarks-bar by dragging them to that bar. If this does not work, copy the link location of the bookmarklets and create the necessary bookmarks manually.

The next step is to start qafe-web-gwt module using the following Maven command on the *platform/qafe-web-gwt* folder:
```
mvn gwt:run
```
This should open the 'GWT Development Mode' application. Press the 'Launch Default Browser' button to open the application in your browser. Now enable 'Super Dev Mode' by opening the 'Dev Mode On' bookmark. 

After (automatic) reloading of the page, open Chrome's Developer Tools and go to the 'Sources' tab. There should be one domain that corresponds to the *hostname:port* of the codeserver. Inside this domain there should be a *sourcemaps/qafegwt* folder. Select a file inside this folder and click on a line number to create a breakpoint.

A possible entry point for debugging the client could be `public void onModuleLoad()` method in the `com.qualogy.qafe.gwt.client.QAFEGWTWeb` class. This is a starting point for loading the UI.

Another possible entry point could be `private static AsyncCallback<?> createCallBack` method in the `com.qualogy.qafe.gwt.client.ui.renderer.events.CallbackHandler` class. This method is called when, for example, actions are performed on UI elements (like clicking a button).

### Debugging client-side code in IDE

To configure SuperDevMode in Eclipse, follow the steps below:
**Note:** If Eclipse is currently open, close it or restart it after step 1.

1. Add the following to your eclipse.ini: -Dchrome.location=<path to chrome executable> (no quotes)
  * This will be used by the SDBG plugin
2. Install Eclipse debugger for GWT SuperDevMode (SDBG) using the following update site: [http://sdbg.github.io/p2](http://sdbg.github.io/p2)
3. Run the GWT codeserver using the following maven command from the qafe-web-gwt folder: `mvn gwt:run-codeserver`
4. When the code server is running (reported in the console "The code server is ready.") start the application  from the qafe-web-gwt folder using: `mvn gwt:run`
5. With the application running, go to Run -> Run Configurations -> Launch Chrome (added by the SDBG plugin) and create a new profile similar to the image below:
In the sources tab select workspace or the project you want to debug.
6. Run the profile in debug mode
7. When running the chrome profile, a new chrome browser with the application should start. Now we need to generate proper source maps. Do these steps from the newly opened chrome browser.
  a. go to your code server url, can be found in the console from where you started the codeserver
  b. drag the Dev Mode On button to your bookmark bar
  c. go back to the application in chrome opened by the launch profile and click the Dev Mode On button (your application will begin to compile and generate the correct sourcemaps)
8. You should now be able to debug from within eclipse. Go to debug perspective, and in the threads view, select the " JS Thread" , you should now see the objects on the heap, etc.

# Logging
QAFE Platform uses the Java Logging API as its logging framework. Therefore, configuring logging behaviour can be accomplished by applying changes to the logging.properties of your JRE's lib/logging.properties file. Also, a logging.properties file location can be specified using the system property java.util.logging.config.file.

When deployed into a tomcat container different rules apply as tomcat implements its own Log Manager. See http://tomcat.apache.org/tomcat-7.0-doc/logging.html under java.util.logging for the specifics.

In case of developing QAML using eclipse and the QAML Builder plugins the logging.properties file can be found in the src/main/resources folder of your QAML project. When debugging an application more log output could be desirable, as by default only WARNING or SEVERE messages are logged. Open the logging.properties file and find the following line:

```
java.util.logging.ConsoleHandler.level = WARNING
```

and change it to:
```
java.util.logging.ConsoleHandler.level = INFO
```
See for a complete list of log levels: http://docs.oracle.com/javase/7/docs/api/java/util/logging/Level.html.

# Running QAFE Platform applications
The WAR-file for the QAFE-platform based on GWT is found in the target-folder of the qafe-webapps project after building the platform. This file contains the platform and a basic Hello World application. A web server is necessary to run the application, as discussed in the build-section. Copy the file to the application-folder of your web server (i.e. the webapps-folder in Tomcat) and run the server. 

The link to the application is dependent on the port number specified in your web server and the filename of the WAR-file without the extension. For example, if the port number is 8080 (standard for Tomcat) and the filename is qafe-web-gwt.war, then the link would be:

```
http://localhost:8080/qafe-web-gwt/
```

# Sample Code
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

# Create an QAFE application using the QAFE Platform
The WAR-file for the QAFE-platform based on GWT is found in the target-folder of the qafe-webapps project after building the platform. First unpack the WAR-file to a different folder (e.g. C:\qafe-web-gwt-x.x.x). After unpacking the WAR-file, create a new QAFE Project in QAML-builder (Eclipse plugin) and set the "Path to QAFE" setting to match the location of the unpacked WAR-file (in our example C:\qafe-web-gwt-x.x.x).

# Updating QAFE Platform on existing QAFE projects
To update an existing project, modify the project's main pom.xml file and change the fileset configuration in the prepare-package phase to match the required platform version (e.g.):
```
...
<phase>prepare-package</phase>
	<configuration>
		<tasks>
			<copy todir="target/${project.artifactId}-${project.version}" overwrite="true">
				<fileset dir="C:\qafe-web-gwt-x.x.x" />
			</copy>
			...
		</tasks>
	</configuration>
	...
```

QAML-builder should now recognize the QAFE platform and also be able to create and run new QAFE applications.

# Further Reading
Further information regarding the QAML documentation and Forms Conversion can be found on our [QAFE Developer Documentation](https://github.com/qafedev/qafedev.github.io/blob/master/README.md "QAFE Developer Documentation")

# Webservice
Information on how to build, run and use the QAFE SOAP webservice see the [Webservice README](https://github.com/qafedev/qafe-platform/tree/develop/qafe-webservice/README.md "QAFE Webservice Documentation").

# Licenses
Information regarding the licenses used in the QAFE Platform can be found on the [Depedencies page](https://github.com/qafedev/qafe-platform/blob/master/dependencies.md "QAFE dependencies") on GitHub. 