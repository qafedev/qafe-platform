@echo off

::----------------------------------------------------------------------
:: Specify the location of Sun JRE/JDK 1.6.x, 1.5.x or 1.4.x here
::----------------------------------------------------------------------
::set JAVA_HOME=C:\jre1.6

::----------------------------------------------------------------------
:: Specify the location of DocFlex/XML (Kit) home directory.
::
:: NOTE: This command file has been prepared in assumption that it will 
:: be started from its parent directory (i.e. that directory is selected 
:: as the Windows current directory).
::
:: If you encounter an error like: 
:: 'Exception in thread "main" java.lang.NoClassDefFoundError: com/docflex/xml/Generator'
:: apparently, you are running this file from a different location.
::
:: In that case, please specify the absolute pathname of your 
:: DocFlex/XML installation here, e.g.
::
:: set DFH=C:\docflex-xml-kit
::----------------------------------------------------------------------
set DFH=.

::----------------------------------------------------------------------
:: -Xmx option sets the maximum heap size allocated by JVM.
::
:: Check this option when you need to process large quantities of data!
::
:: Note: DocFlex/XML generator may be especially hungry for memory
:: as it stores lots of temporary data in hash-tables in order to boost
:: performance. However, according to our tests, even when processing
:: such a heavy template application as XSDDoc on the Eclipse UML2 schemas,
:: the largest we've seen so far, the 512 MB memory heap was quite enough
::----------------------------------------------------------------------
set OPTIONS=-Xms256m -Xmx512m

::----------------------------------------------------------------------
:: DocFlex/XML (Kit) class path
::----------------------------------------------------------------------
set OPTIONS=%OPTIONS% -cp %DFH%\lib\xml-apis.jar;%DFH%\lib\xercesImpl.jar;%DFH%\lib\docflex-xml-kit.jar

::----------------------------------------------------------------------
:: Running generator with all settings taken from generator.config file
::----------------------------------------------------------------------
%JAVA_HOME%\bin\java %OPTIONS% com.docflex.xml.Generator
