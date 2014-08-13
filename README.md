# QAFE Platform

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
Checkout the source from git

```
git clone https://github.com/qafedev/qafe-platform.git
```

The source code can be found in qafe-platform directory
(so /Users/johndoe/develop/qafe/qafe-platform/

## Building QAFE Platform
Before building a couple things need to be checked:

1. Maven version 3 needs to be used. On OSX Mavericks maven is not shipped automatically. For this the following can work:
```
brew install maven
```
For other platforms, see [this link](http://maven.apache.org/download.cgi)

1. The location of the local maven repository needs to be changed. In the qafe-platform/settings.xml locate the <localRepository> tag. By doing this you won't harm other maven projects on your system. Check it to for example:
```
<localRepository>/Users/johndoe/develop/qafe/m2/</localRepository>
``` 

1. If this is done, you can start the following command to build the platform:
```
mvn -Pbuildall clean install -DskipTests
```
The build time is about 10 minutes. The skiptests in included since database testing using a real database needs to be skipped.

**Note**: The Oracle JDBC driver is not allowed to be distributed by maven repositories. For this follow [this link](http://www.mkyong.com/maven/how-to-add-oracle-jdbc-driver-in-your-maven-local-repository/) to make it work. 


How to run a sample application
@TODO add steps
