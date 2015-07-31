
OpenExchange - OpenXDS - With ODD support
=========================================

This is a forked version of the [original OpenXDS](https://www.projects.openhealthtools.org/sf/projects/openxds/) with added support for [the on-demand documents option](http://wiki.ihe.net/index.php?title=ITI_On-Demand_Documents). These additions were done to enable the use of OpenXDS as an XDS registry for the [OpenSHR project](https://github.com/jembi/openshr) which is a reference tool for [OpenHIE](http://ohie.org/).

Building and running
--------------------

To build this code base you need to copy the folder in the `m2repo` directory to your maven repository (`<user_home>.m2/`). Then run `mvn clean install`. This will produce a `build` folder that contains an openxds .jar file which you can run with `java -jar openxds-1.1.0-SNAPSHOT.jar`.

What is it?
-----------

OpenExchange is a Charter project under Open Health Tools (openhealthtools.org)
providing standards based core infrastructure to exchange patient health
information in a secure and timely manner, to advance the quality, safety
and efficiency of healthcare delivery. The OpenExchange platform is a critical
element of HIE infrastructure that provides clinicians and other members of
the healthcare community with the right information at the right time by
making patient information available across organizational boundaries. It
facilitates and streamlines the sharing of patient information throughout
an HIE community.

OpenExchange consists of open source projects that use Integrating the
Healthcare Enterprise (IHE.net) profiles as the set of fundamental
requirements. The core set of open source projects under OpenExchange
include OpenPIXPDQ, OpenXDS (with XCA), OpenATNA and OpenAuth.

OpenXDS, as a child project of OpenExchange, implements the Document
Repository and Document Registry actors of the Cross-Enterprise Document
Sharing (XDS) profile and the Initiating Gateway and Responding Gateway
actors of the Cross Community Access (XCA)profile specified by IHE.


Contributors
------------
The OpenExchange (OpenATNA, OpenAuth, OpenPIXPDQ and OpenXDS) developement
has been led by Misys Open Source Solutions (MOSS) and University of Cardiff.
Other contributors include Sysnet International, Topicus Zorg and a number
of individuals. OpenXDS also contains software developed by freebXML.org
and NIST. (ODD support added by Jembi Health Systems)


Contents
--------

  Included in this release are the following:

```
  README.txt 	               This file
  LICENSE.txt	               Software license
  NOTICE.txt	   		       Copyright and contribution Notice
  applicationContext.xml     Spring context configuration file
  commons-logging.properties common-logging file
  log4j.xml                  log4j configuration
  omar.properties            omar configuration
  openxds.jar                Main openxds executable file
  openxds.properties         openxds configuration
  repository.jdbc.cfg.xml    omar repository jdbc configuration
  conf/actors/               All the actor configuration files
  conf/actors/cert/          keystore and truststore
  conf/axis2repository/      Embedded axis2 repository
  conf/axis2repository/module/
                             Embedded axis2 modules
  conf/axis2repository/services/
                             Embedded axis2 services
  conf/audit/healthcare-security-audit.xsd
                             Audit schema
  conf/schema/v2/            ebxml v2 schema
  conf/schema/v3/            ebxml v3 schema
  lib/                       All the libs needed for running tje OpenXDS server
  licenses/	               All the third party license files
  misc/create_database_schema_mysql.sql
                             OpenXDS database creation script for MySQL
  misc/create_database_schema_postgres.sql
                             OpenXDS database creation script for Postgresql
  misc/create_database_schema_sqlserver2005.sql
                             OpenXDS database creation script for SQL Server 2005
  misc/openxds-web.war       OpenXDS web application file.
```


Requirements
------------

JDK Version: OpenXDS supports JDK 1.6 or higher.

Databases: OpenXDS supports Postgresql, MySQL, SQL Server. Our tested database is Postgresql 8.3.


Installation and Configuration
------------------------------

Installation and configuration guide is available on the OpenXDS Project
web site on Open Health Tools (OHT)
<https://openxds.projects.openhealthtools.org>.


Documentation
-------------

Documentation is available on the OpenXDS Project web site
on Open Health Tools (OHT) <https://openxds.projects.openhealthtools.org>.


The Latest Version
------------------

For this fork the latest releases will be uploaded here when available: https://github.com/jembi/openxds/releases


Problems
---------

Our web page at https://openxds.projects.openhealthtools.org has pointers
where you can post questions, report bugs or request features. You'll also
find information on how to subscribe to our dev list and discussion forum.


Licensing
---------

This software is licensed under the terms you may find in the file
named "LICENSE.txt" in this directory.


Support
---------

For commercial support, please contact Kondayya.Mullapudi@misys.com.

Major changes in OpenXDS 1.1.0
------------------------------
 * Added ODD support (Jembi Health Systems)

Major changes in OpenXDS 1.0.1
------------------------------
* Additional MySQL and SQL Server databases support
* Added audit log with TLS transaction
* Generic actor configuration refactoring
* Integrated with OpenExchange
* Added property and spring configuration framework
* Added configuration pre and post processes interface
* Provided OpenXDS extension hook
* Some bug fixes

Thanks for using OpenExchange - OpenXDS.

Open Health Tools (OHT)  <http://www.openhealthtools.org>
Misys Open Source Solutions (MOSS) <http://www.misysoss.com/>
University of Cardiff <http://www.cs.cardiff.ac.uk>
Jembi Health Systems <http://www.jembi.org/>
