#!/bin/sh

#----------------------------------------------------------------------
# Specify the location of Sun JRE/JDK 6.x, 5.x or 1.4.x here
#----------------------------------------------------------------------
JAVA_HOME=/usr/java/jdk1.6.0_02

#----------------------------------------------------------------------
# The location of DocFlex/XML (Kit) home directory
#----------------------------------------------------------------------
DFH=..

#----------------------------------------------------------------------
# -Xmx option sets the maximum heap size allocated by JVM.
#
# Check this option when you need to process large quantities of data!
#
# Note: DocFlex/XML generator may be especially hungry for memory
# as it stores lots of temporary data in hash-tables in order to boost
# performance. However, according to our tests, even when processing
# such a heavy template application as XSDDoc on the Eclipse UML2 schemas,
# the largest we've seen so far, the 512 MB memory heap was quite enough.
#----------------------------------------------------------------------
OPTIONS="-Xms256m -Xmx512m"

#----------------------------------------------------------------------
# DocFlex/XML (Kit) class path
#----------------------------------------------------------------------
OPTIONS="${OPTIONS} -cp ${DFH}/lib/xml-apis.jar:${DFH}/lib/xercesImpl.jar:${DFH}/lib/docflex-xml-kit.jar"

#----------------------------------------------------------------------
# Run generator with all settings taken from generator.config file
#
# The '-docflexconfig' option specifies the DocFlex main configuration
# file prepared for Linux (located in this directory).
#----------------------------------------------------------------------
${JAVA_HOME}/bin/java ${OPTIONS} com.docflex.xml.Generator \
  -docflexconfig ${DFH}/linux/docflex.config
