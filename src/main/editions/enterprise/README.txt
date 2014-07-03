QAFE Platform Enterprise Edition 1.2

Copyright 2006- 2011 Qualogy B.V.. All rights reserved.
Visit QAFE at http://www.qafe.com


This product includes software developed by:
Apache Software Foundation: apache.org (commons libraries); 
Google Corp.              : http://code.google.com/webtoolkit (Google Web Toolkit)
Adobe Corp.               : http://www.adobe.com/flex   (Adobe Flex)
JIBX                      : http://jibx.org (JIBX).
Sun Development           : https://truelicense.dev.java.net/ (TrueLicense software)

For license information see LICENSE.txt for more information. 

 
HOW TO INSTALL?
---------------
Deploy the war found in the zipfile.
The license key file must be stored in the WEB-INF directory of the deployed QAFE file war.
This goes for any QAFE warfile that is deployed
For retrieving a license key file, please visit http://www.qafe.com/registration

QAFE Web Flex
-------------

When choosing to deploy the Flex version of the QAFE Engine (qafe-web-flex.war), the webservice-import-export-server needs
to be deployed to in order to work with Import/Export functionality on the datagrid component.


In order to use the webservice-import-export-server module, the following property should be specified in the external.properties file, located in the WEB-INF directory:
- webservice.url: represents the URL of the webservice, it has the format: http://<SERVER_NAME>:<SERVER_PORT>/webservice-import-export-server

In order to run a QAFE AIR desktop application, the following properties should be specified in the external.properties file, located in the WEB-INF directory:
- air.application.name: represents the name of the QAFE AIR desktop application, only digits and letters are allowed, also do not use the reserved word "main", default is "qafe"
- air.application.version: represents the version number, when this differs from the current installed QAFE AIR desktop application, 
  the update confirmation dialog will be shown when starting the QAFE AIR desktop application
- air.application.notes: represents the release notes, this will be displayed in the update dialog
- air.application.icon48: represents the icon (48x48) for the QAFE AIR desktop application, default is qafe icon
- air.server.url: represents the URL of the remote server, it has the format: http://<SERVER_NAME>:<SERVER_PORT>/qafe-web-flex

The QAFE AIR desktop application can be deployed on the client machine with the following URL address:
- http://<SERVER_NAME>:<SERVER_PORT>/qafe-web-flex/deploy_air.html

Click on the “Install AIR application” button when it becomes available, and follow the installation instructions.


CONTACT DETAILS
----------------
QAFE, division of Qualogy
De Bruyn Kopsstraat 9
Rijswijk (ZH)
2288 EC
The Netherlands
Telephone: +31 (0)70 319 5000

If you have any purchasing-related questions, please call or contact us at: info@qafe.com.

Make sure to bookmark http://www.qafe.com/news/ for upcoming product and technology news, or follow us on:
 * twitter:  http://twitter.com/qafe 
 * facebook: http://www.facebook.com/qafeframework
