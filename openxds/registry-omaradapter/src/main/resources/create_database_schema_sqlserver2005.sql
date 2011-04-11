--
--  Copyright (c) 2009-2011 Misys Open Source Solutions (MOSS) and others
--
--  Licensed under the Apache License, Version 2.0 (the "License");
--  you may not use this file except in compliance with the License.
--  You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
--  Unless required by applicable law or agreed to in writing, software
--  distributed under the License is distributed on an "AS IS" BASIS,
--  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
--  implied. See the License for the specific language governing
--  permissions and limitations under the License.
--
--  Contributors:
--    Misys Open Source Solutions - initial API and implementation
--    -
--

USE openxds
GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.Association') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.Association;
CREATE TABLE Association (
--Identifiable Attributes
  id				VARCHAR(256) NOT NULL PRIMARY KEY,
  home                      VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256) CHECK (objectType = 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association'),
  status			VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256),

--Association attributes
  associationType	VARCHAR(256) NOT NULL,
  sourceObject		VARCHAR(256) NOT NULL,
  targetObject  	VARCHAR(256) NOT NULL
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.AuditableEvent') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.AuditableEvent;
CREATE TABLE AuditableEvent (
--Identifiable Attributes
  id				VARCHAR(256) NOT NULL PRIMARY KEY,
  home                      VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256) CHECK (objectType = 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:AuditableEvent'),
  status			VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256),

--AuditableEvent attributes
  requestId             VARCHAR(256) NOT NULL,
  eventType			VARCHAR(256) NOT NULL,
  timeStamp_        VARCHAR(30) NOT NULL,
  user_				VARCHAR(256) NOT NULL
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.AffectedObject') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.AffectedObject;
CREATE TABLE AffectedObject (

--Each row is a relationship between a RegistryObject and an AuditableEvent
--Enables many-to-many relationship between effected RegistryObjects and AuditableEvents
  id                            VARCHAR(256) NOT NULL,
  home                      VARCHAR(256),
  eventId                   VARCHAR(256) NOT NULL,

  PRIMARY KEY (id, eventId)
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.repository') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.repository;
CREATE TABLE repository
(
  documentuniqueid character varying(255) NOT NULL PRIMARY KEY,
  mimetype character varying(255) NOT NULL,
  documentsize INT NOT NULL,
  hash character varying(255) NOT NULL,
  "content" varbinary(max)
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.personidentifier') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.personidentifier;
CREATE TABLE personidentifier
(
  registrypatientid character varying(255) NOT NULL PRIMARY KEY,
  assigningauthority character varying(255) NOT NULL,
  patientid character varying(255) NOT NULL, 
  deleted varchar(1),
  merged varchar(1),
  survivingpatientid character varying(255)  
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.Classification') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.Classification;
CREATE TABLE Classification (
--Identifiable Attributes
  id				VARCHAR(256) NOT NULL PRIMARY KEY,
  home                      VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256) CHECK (objectType = 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification'),
  status			VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256),

--Classfication attributes.
  classificationNode		VARCHAR(256),
  classificationScheme		VARCHAR(256),
  classifiedObject		VARCHAR(256) NOT NULL,
  nodeRepresentation		VARCHAR(256)
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.ClassificationNode') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.ClassificationNode;
CREATE TABLE ClassificationNode (
--Identifiable Attributes
  id				VARCHAR(256) NOT NULL PRIMARY KEY,
  home                      VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256) CHECK (objectType = 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode'),
  status			VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256),

--ClassficationNode attributes
  code					VARCHAR(256),
  parent				VARCHAR(256),
  path					VARCHAR(1024)
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.ClassScheme') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.ClassScheme;
CREATE TABLE ClassScheme (
--Identifiable Attributes
  id				VARCHAR(256) NOT NULL PRIMARY KEY,
  home                      VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256) CHECK (objectType = 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme'),
  status			VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256),

--ClassificationScheme attributes
  isInternal		VARCHAR(1) NOT NULL,
  nodeType		VARCHAR(256) NOT NULL
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.ExternalIdentifier') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.ExternalIdentifier;
CREATE TABLE ExternalIdentifier (
--Identifiable Attributes
  id				VARCHAR(256) NOT NULL PRIMARY KEY,
  home                      VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256) CHECK (objectType = 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier'),
  status			VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256),

--ExternalIdentifier attributes
  registryObject	VARCHAR(256) NOT NULL,
  identificationScheme		VARCHAR(256) NOT NULL,
  value				VARCHAR(256) NOT NULL
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.ExternalLink') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.ExternalLink;
CREATE TABLE ExternalLink (
  id				VARCHAR(256) NOT NULL PRIMARY KEY,
  home                      VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256),
  status			VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256),

--ExternalLink attributes
  externalURI		VARCHAR(256) NOT NULL
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.ExtrinsicObject') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.ExtrinsicObject;
CREATE TABLE ExtrinsicObject (
  id				VARCHAR(256) NOT NULL PRIMARY KEY,
  home                      VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256),
  status			VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256),

--ExtrinsicObject attributes
  isOpaque			VARCHAR(1) NOT NULL,
  mimeType			VARCHAR(256),

--contentVersionInfo flattened
  contentVersionName		VARCHAR(16),
  contentVersionComment	VARCHAR(256)

);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.Federation') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.Federation;
CREATE TABLE Federation (
  id				VARCHAR(256) NOT NULL PRIMARY KEY,
  home                      VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256) CHECK (objectType = 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Federation'),
  status			VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256),

--Federation attributes: currently none defined
--xsd:duration stored in string form since no corresponding SQL type. Is 32 long enough?
  replicationSyncLatency    VARCHAR(32)
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.Name_') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.Name_;
CREATE TABLE Name_ (
--LocalizedString attributes flattened for Name
  charset			VARCHAR(32),
  lang				VARCHAR(32) NOT NULL,
  value				VARCHAR(1024) NOT NULL,
--The RegistryObject id for the parent RegistryObject for which this is a Name
  parent			VARCHAR(256) NOT NULL,
  PRIMARY KEY (parent, lang)
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.Description') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.Description;
CREATE TABLE Description (
--LocalizedString attributes flattened for Description
  charset			VARCHAR(32),
  lang				VARCHAR(32) NOT NULL,
  value				VARCHAR(1024) NOT NULL,
--The RegistryObject id for the parent RegistryObject for which this is a Name
  parent			VARCHAR(256) NOT NULL,
  PRIMARY KEY (parent, lang)
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.UsageDescription') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.UsageDescription;
CREATE TABLE UsageDescription (
--LocalizedString attributes flattened for UsageDescription
  charset			VARCHAR(32),
  lang				VARCHAR(32) NOT NULL,
  value				VARCHAR(1024) NOT NULL,
--The RegistryObject id for the parent RegistryObject for which this is a Name
  parent			VARCHAR(256) NOT NULL,
  PRIMARY KEY (parent, lang)
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.ObjectRef') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.ObjectRef;
CREATE TABLE ObjectRef (
--Stores remote ObjectRefs only
--Identifiable Attributes
  id				VARCHAR(256) NOT NULL PRIMARY KEY,
  home                          VARCHAR(256)
);
GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.Organization') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.Organization;
CREATE TABLE Organization (
  id				VARCHAR(256) NOT NULL PRIMARY KEY,
  home                      VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256) CHECK (objectType = 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Organization'),
  status			VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256),

--Organization attributes
--Organization.address attribute is in PostalAddress table
  parent			VARCHAR(256),
--primary contact for Organization, points to a User.
  primaryContact	VARCHAR(256)
--Organization.telephoneNumbers attribute is in TelephoneNumber table
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.RegistryPackage') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.RegistryPackage;
CREATE TABLE RegistryPackage (
  id				VARCHAR(256) NOT NULL PRIMARY KEY,
  home                      VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256) CHECK (objectType = 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage'),
  status			VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256)

--RegistryPackage attributes: currently none defined
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.PostalAddress') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.PostalAddress;
CREATE TABLE PostalAddress (
  city				VARCHAR(64),
  country			VARCHAR(64),
  postalCode		VARCHAR(64),
  state				VARCHAR(64),
  street			VARCHAR(64),
  streetNumber		VARCHAR(32),
--The parent object that this is an address for
  parent			VARCHAR(256) NOT NULL
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.EmailAddress') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.EmailAddress;
CREATE TABLE EmailAddress (
  address			VARCHAR(64) NOT NULL,
  type				VARCHAR(256),
--The parent object that this is an email address for
  parent			VARCHAR(256) NOT NULL
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.Registry') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.Registry;
CREATE TABLE Registry (
  id				VARCHAR(256) NOT NULL PRIMARY KEY,
  home                      VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256) CHECK (objectType = 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Registry'),
  status		VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256),

--Registry attributes
--xsd:duration stored in string form since no corresponding SQL type. Is 32 long enough?
  catalogingSyncLatency    VARCHAR(32) DEFAULT 'P1D',
  conformanceProfile      VARCHAR(16),
  operator  VARCHAR(256) NOT NULL,

--xsd:duration stored in string form since no corresponding SQL type. Is 32 long enough?
  replicationSyncLatency    VARCHAR(32) DEFAULT 'P1D',
  specificationVersion    VARCHAR(8) NOT NULL
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.Service') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.Service;
CREATE TABLE Service (
  id			VARCHAR(256) NOT NULL PRIMARY KEY,
  home                  VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256) CHECK (objectType = 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Service'),
  status			VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256)

--Service attributes: currently none defined
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.ServiceBinding') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.ServiceBinding;
CREATE TABLE ServiceBinding (
  id			VARCHAR(256) NOT NULL PRIMARY KEY,
  home                  VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256) CHECK (objectType = 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ServiceBinding'),
  status			VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256),

--ServiceBinding attributes
  service			VARCHAR(256) NOT NULL,
  accessURI			VARCHAR(256),
  targetBinding		VARCHAR(256)
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.Slot') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.Slot;
CREATE TABLE Slot (
--Multiple rows of Slot make up a single Slot
  sequenceId		INT NOT NULL,
  name_				VARCHAR(256) NOT NULL,
  slotType			VARCHAR(256),
  value				VARCHAR(256),
--The parent RegistryObject that this is a Slot for
  parent			VARCHAR(256) NOT NULL,
  PRIMARY KEY (parent, name_, sequenceId)
);


GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.SpecificationLink') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.SpecificationLink;
CREATE TABLE SpecificationLink (
  id			VARCHAR(256) NOT NULL PRIMARY KEY,
  home                  VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256) CHECK (objectType = 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:SpecificationLink'),
  status			VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256),

--SpecificationLink attributes
  serviceBinding	VARCHAR(256) NOT NULL,
  specificationObject VARCHAR(256) NOT NULL
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.Subscription') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.Subscription;
CREATE TABLE Subscription (
  id			VARCHAR(256) NOT NULL PRIMARY KEY,
  home                  VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256) CHECK (objectType = 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Subscription'),
  status			VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256),

--Subscription attributes
  selector              VARCHAR(256) NOT NULL,
  endTime               VARCHAR(30),

--xsd:duration stored in string form since no corresponding SQL type. Is 32 long enough?
  notificationInterval  VARCHAR(32) DEFAULT 'P1D',
  startTime             VARCHAR(30)
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.NotifyAction') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.NotifyAction;
CREATE TABLE NotifyAction (
  notificationOption       VARCHAR(256) NOT NULL,

--Either a ref to a Service, a String representing an email address in form: mailto:user@server,
--or a String representing an http URLin form: http://url
  endPoint                    VARCHAR(256) NOT NULL,

--Parent Subscription reference
  parent			VARCHAR(256) NOT NULL
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.Notification') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.Notification;
CREATE TABLE Notification (
  id				VARCHAR(256) NOT NULL PRIMARY KEY,
  home                      VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256) CHECK (objectType = 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Notification'),
  status		VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256),

--Notification attributes
  subscription        VARCHAR(256) NOT NULL
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.NotificationObject') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.NotificationObject;
CREATE TABLE NotificationObject (

--Each row is a relationship between a RegistryObject and a Notification
--Enables a Notification to have multiple RegistryObjects
  notificationId             VARCHAR(256) NOT NULL,
  registryObjectId           VARCHAR(256) NOT NULL,

  PRIMARY KEY (notificationId, registryObjectId)
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.AdhocQuery') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.AdhocQuery;
CREATE TABLE AdhocQuery (
  id			VARCHAR(256) NOT NULL PRIMARY KEY,
  home                  VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256) CHECK (objectType = 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:AdhocQuery'),
  status			VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256),

--AdhocQuery attributes. Flattend QueryExpression attributes
  queryLanguage		VARCHAR(256) NOT NULL,
  query			VARCHAR(4096) NOT NULL
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.UsageParameter') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.UsageParameter;
CREATE TABLE UsageParameter (
  value				VARCHAR(1024) NOT NULL,
--The parent SpecificationLink that this is a usage parameter for
  parent			VARCHAR(256) NOT NULL
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.TelephoneNumber') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.TelephoneNumber;
CREATE TABLE TelephoneNumber (
  areaCode			VARCHAR(8),
  countryCode		VARCHAR(8),
  extension			VARCHAR(8),
  -- we use "number_" instead of number, which is reserved in Oracle
  number_			VARCHAR(16),
  phoneType			VARCHAR(256),
  parent			VARCHAR(256) NOT NULL
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.User_') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.User_;
CREATE TABLE User_ (
  id			VARCHAR(256) NOT NULL PRIMARY KEY,
  home                  VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256) CHECK (objectType = 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Person:User'),
  status			VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256),

--User attributes
--address is in PostalAddress table
--email is in EMailAddress table
--personName flattened
  personName_firstName	VARCHAR(64),
  personName_middleName	VARCHAR(64),
  personName_lastName	VARCHAR(64)
--telephoneNumbers is in TelephoneNumber table
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.Person') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.Person;
CREATE TABLE Person (
  id			VARCHAR(256) NOT NULL PRIMARY KEY,
  home                  VARCHAR(256),
--RegistryObject Attributes
  lid				VARCHAR(256) NOT NULL,
  objectType		VARCHAR(256) CHECK (objectType = 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Person'),
  status			VARCHAR(256) NOT NULL,

--VersionInfo flattened
  versionName		VARCHAR(16),
  comment_		VARCHAR(256),

--User attributes
--address is in PostalAddress table
--email is in EMailAddress table
--personName flattened
  personName_firstName	VARCHAR(64),
  personName_middleName	VARCHAR(64),
  personName_lastName	VARCHAR(64)
--telephoneNumbers is in TelephoneNumber table
);

GO
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'dbo.RepositoryItem') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
DROP TABLE dbo.RepositoryItem;

CREATE TABLE RepositoryItem
(
  lid character varying(256) NOT NULL,
  versionname character varying(16) NOT NULL,
  "content" varbinary(max), 
  PRIMARY KEY(lid, versionname)
);
GO
IF OBJECT_ID ('dbo.Identifiable', 'V') IS NOT NULL
DROP VIEW dbo.Identifiable ;
GO
CREATE VIEW Identifiable (
--Identifiable Attributes
  id,
  home
) AS
 SELECT
--Identifiable Attributes
  id,
  home
 FROM AdhocQuery
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home
 FROM Association
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home
 FROM AuditableEvent
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home
 FROM Classification
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home
 FROM ClassificationNode
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home
 FROM ClassScheme
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home
 FROM ExternalIdentifier
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home
 FROM ExternalLink
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home
 FROM ExtrinsicObject
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home
 FROM Federation
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home
 FROM Organization
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home
 FROM Registry
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home
 FROM RegistryPackage
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home
 FROM Service
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home
 FROM ServiceBinding
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home
 FROM SpecificationLink
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home
 FROM Subscription
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home
 FROM User_
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home
 FROM Person
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home
 FROM ObjectRef
;
GO
IF OBJECT_ID ('dbo.RegistryObject', 'V') IS NOT NULL
DROP VIEW dbo.RegistryObject ;
GO
CREATE VIEW RegistryObject (
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
) AS
 SELECT
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
 FROM AdhocQuery
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
 FROM Association
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
 FROM AuditableEvent
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
 FROM Classification
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
 FROM ClassificationNode
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
 FROM ClassScheme
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
 FROM ExternalIdentifier
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
 FROM ExternalLink
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
 FROM ExtrinsicObject
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
 FROM Federation
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
 FROM Organization
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
 FROM Registry
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
 FROM RegistryPackage
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
 FROM Service
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
 FROM ServiceBinding
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
 FROM SpecificationLink
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
 FROM Subscription
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
 FROM User_
 UNION ALL
 SELECT
--Identifiable Attributes
  id,
  home,
--RegistryObject Attributes
  lid,
  objectType,
  status,
--VersionInfo flattened
  versionName,
  comment_
 FROM Person
;



--All index definitions are non-normative

---
-- FOR ORACLE, YOU MUST COMMMENT ALL THE FOLLOWING LINES BECAUSE ORACLE DOES
-- NOT ALLOW CREATE INDEX ON PRIMARY KEYS
--

--lid index
--IF NOT EXISTS (SELECT name FROM sysindexes WHERE name = 'idx_acct_no')
--	create index [idx_acct_no] ON [dbo].[accounts] ([acct_no])
GO
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_AdhQuery_idx') DROP INDEX AdhocQuery.lid_AdhQuery_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_Assoc_idx') DROP INDEX Association.lid_Assoc_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_AUEVENT_idx') DROP INDEX AuditableEvent.lid_AUEVENT_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_Class_idx') DROP INDEX Classification.lid_Class_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_Node_idx') DROP INDEX ClassificationNode.lid_Node_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_SCHEME_idx') DROP INDEX ClassScheme.lid_SCHEME_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_ExLink_idx') DROP INDEX ExternalLink.lid_ExLink_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_EID_idx') DROP INDEX ExternalIdentifier.lid_EID_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_EXTOBJ_idx') DROP INDEX ExtrinsicObject.lid_EXTOBJ_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_FED_idx') DROP INDEX Federation.lid_FED_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_ORG_idx') DROP INDEX Organization.lid_ORG_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_Registry_idx') DROP INDEX Registry.lid_Registry_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_PKG_idx') DROP INDEX RegistryPackage.lid_PKG_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_Service_idx') DROP INDEX Service.lid_Service_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_BIND_idx') DROP INDEX ServiceBinding.lid_BIND_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_SLnk_idx') DROP INDEX SpecificationLink.lid_SLnk_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_SUBS_idx') DROP INDEX Subscription.lid_SUBS_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_User_idx') DROP INDEX User_.lid_User_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_Person_idx') DROP INDEX Person.lid_Person_idx;


CREATE INDEX lid_AdhQuery_idx ON AdhocQuery(lid);
CREATE INDEX lid_Assoc_idx ON Association(lid);
CREATE INDEX lid_AUEVENT_idx ON AuditableEvent(lid);
CREATE INDEX lid_Class_idx ON Classification(lid);
CREATE INDEX lid_Node_idx ON ClassificationNode(lid);
CREATE INDEX lid_SCHEME_idx ON ClassScheme(lid);
CREATE INDEX lid_EID_idx ON ExternalIdentifier(lid);
CREATE INDEX lid_ExLink_idx ON ExternalLink(lid);
CREATE INDEX lid_EXTOBJ_idx ON ExtrinsicObject(lid);
CREATE INDEX lid_FED_idx ON Federation(lid);
CREATE INDEX lid_ORG_idx ON Organization(lid);
CREATE INDEX lid_Registry_idx ON Registry(lid);
CREATE INDEX lid_PKG_idx ON RegistryPackage(lid);
CREATE INDEX lid_Service_idx ON Service(lid);
CREATE INDEX lid_BIND_idx ON ServiceBinding(lid);
CREATE INDEX lid_SLnk_idx ON SpecificationLink(lid);
CREATE INDEX lid_SUBS_idx ON Subscription(lid);
CREATE INDEX lid_User_idx ON User_(lid);
CREATE INDEX lid_Person_idx ON Person(lid);

--id index
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_AdhQuery_idx') DROP INDEX AdhocQuery.id_AdhQuery_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_Assoc_idx') DROP INDEX Association.id_Assoc_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_AUEVENT_idx') DROP INDEX AuditableEvent.id_AUEVENT_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_Class_idx') DROP INDEX Classification.id_Class_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_Node_idx') DROP INDEX ClassificationNode.id_Node_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_SCHEME_idx') DROP INDEX ClassScheme.id_SCHEME_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_ExLink_idx') DROP INDEX ExternalLink.id_ExLink_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_EID_idx') DROP INDEX Externaidentifier.id_EID_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_EXTOBJ_idx') DROP INDEX ExtrinsicObject.id_EXTOBJ_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_FED_idx') DROP INDEX Federation.id_FED_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_ObjectRef_idx') DROP INDEX ObjectRef.id_ObjectRef_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_ORG_idx') DROP INDEX Organization.id_ORG_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_Registry_idx') DROP INDEX Registry.id_Registry_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_PKG_idx') DROP INDEX RegistryPackage.id_PKG_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_Service_idx') DROP INDEX Service.id_Service_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_BIND_idx') DROP INDEX ServiceBinding.id_BIND_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_SLnk_idx') DROP INDEX SpecificationLink.id_SLnk_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_SUBS_idx') DROP INDEX Subscription.id_SUBS_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_User_idx') DROP INDEX User_.id_User_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_Person_idx') DROP INDEX Person.id_Person_idx;

CREATE INDEX id_AdhQuery_idx ON AdhocQuery(id);
CREATE INDEX id_Assoc_idx ON Association(id);
CREATE INDEX id_AUEVENT_idx ON AuditableEvent(id);
CREATE INDEX id_Class_idx ON Classification(id);
CREATE INDEX id_Node_idx ON ClassificationNode(id);
CREATE INDEX id_SCHEME_idx ON ClassScheme(id);
CREATE INDEX id_EID_idx ON ExternalIdentifier(id);
CREATE INDEX id_ExLink_idx ON ExternalLink(id);
CREATE INDEX id_EXTOBJ_idx ON ExtrinsicObject(id);
CREATE INDEX id_FED_idx ON Federation(id);
CREATE INDEX id_ObjectRef_idx ON ObjectRef(id);
CREATE INDEX id_ORG_idx ON Organization(id);
CREATE INDEX id_Registry_idx ON Registry(id);
CREATE INDEX id_PKG_idx ON RegistryPackage(id);
CREATE INDEX id_Service_idx ON Service(id);
CREATE INDEX id_BIND_idx ON ServiceBinding(id);
CREATE INDEX id_SLnk_idx ON SpecificationLink(id);
CREATE INDEX id_SUBS_idx ON Subscription(id);
CREATE INDEX id_User_idx ON User_(id);
CREATE INDEX id_Person_idx ON Person(id);


--home index
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'home_AdhQuery_idx') DROP INDEX AdhocQuery.home_AdhQuery_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'home_Assoc_idx') DROP INDEX Association.home_Assoc_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'home_AUEVENT_idx') DROP INDEX AuditableEvent.home_AUEVENT_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'home_Class_idx') DROP INDEX Classification.home_Class_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'home_Node_idx') DROP INDEX ClassificationNode.home_Node_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'home_SCHEME_idx') DROP INDEX ClassScheme.home_SCHEME_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'home_ExLink_idx') DROP INDEX ExternalLink.home_ExLink_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'home_EID_idx') DROP INDEX Externaidentifier.home_EID_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'home_EXTOBJ_idx') DROP INDEX ExtrinsicObject.home_EXTOBJ_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'home_FED_idx') DROP INDEX Federation.home_FED_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'home_ORG_idx') DROP INDEX Organization.home_ORG_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'home_Registry_idx') DROP INDEX Registry.home_Registry_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'home_PKG_idx') DROP INDEX RegistryPackage.home_PKG_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'home_Service_idx') DROP INDEX Service.home_Service_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'home_BIND_idx') DROP INDEX ServiceBinding.home_BIND_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'home_SLnk_idx') DROP INDEX SpecificationLink.home_SLnk_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'home_SUBS_idx') DROP INDEX Subscription.home_SUBS_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'home_User_idx') DROP INDEX User_.home_User_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'home_Person_idx') DROP INDEX Person.home_Person_idx;

CREATE INDEX home_AdhQuery_idx ON AdhocQuery(home);
CREATE INDEX home_Assoc_idx ON Association(home);
CREATE INDEX home_AUEVENT_idx ON AuditableEvent(home);
CREATE INDEX home_Class_idx ON Classification(home);
CREATE INDEX home_Node_idx ON ClassificationNode(home);
CREATE INDEX home_SCHEME_idx ON ClassScheme(home);
CREATE INDEX home_EID_idx ON ExternalIdentifier(home);
CREATE INDEX home_ExLink_idx ON ExternalLink(home);
CREATE INDEX home_EXTOBJ_idx ON ExtrinsicObject(home);
CREATE INDEX home_FED_idx ON Federation(home);
CREATE INDEX home_ORG_idx ON Organization(home);
CREATE INDEX home_Registry_idx ON Registry(home);
CREATE INDEX home_PKG_idx ON RegistryPackage(home);
CREATE INDEX home_Service_idx ON Service(home);
CREATE INDEX home_BIND_idx ON ServiceBinding(home);
CREATE INDEX home_SLnk_idx ON SpecificationLink(home);
CREATE INDEX home_SUBS_idx ON Subscription(home);
CREATE INDEX home_User_idx ON User_(home);
CREATE INDEX home_Person_idx ON Person(home);

IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_evId_AFOBJ_idx') DROP INDEX AffectedObject.id_evId_AFOBJ_idx;
CREATE INDEX id_evId_AFOBJ_idx ON AffectedObject(id, eventId);

--
-- Following indexes should be OK to use in all databases
--

--index on AffectedObject

IF EXISTS (SELECT name FROM sysindexes WHERE name = 'id_AFOBJ_idx') DROP INDEX AffectedObject.id_AFOBJ_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'evid_AFOBJ_idx') DROP INDEX AffectedObject.evid_AFOBJ_idx;
CREATE INDEX id_AFOBJ_idx ON AffectedObject(id);
CREATE INDEX evid_AFOBJ_idx ON AffectedObject(eventid);


--index on AuditableEvent
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lid_AUEVENT_evtTyp') DROP INDEX AuditableEvent.lid_AUEVENT_evtTyp;
CREATE INDEX lid_AUEVENT_evtTyp ON AuditableEvent(eventType);

--name index
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'value_Name_idx') DROP INDEX Name_.value_Name_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lngval_Name_idx') DROP INDEX Name_.lngval_Name_idx;
CREATE INDEX value_Name_idx ON Name_(value);
CREATE INDEX lngval_Name_idx ON Name_(lang, value);

--description index
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'value_Desc_idx') DROP INDEX Description.value_Desc_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lngval_Desc_idx') DROP INDEX Description.lngval_Desc_idx;
CREATE INDEX value_Desc_idx ON Description(value);
CREATE INDEX lngval_Desc_idx ON Description(lang, value);

--UsageDesc index
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'value_UsgDes_idx') DROP INDEX UsageDescription.value_UsgDes_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lngval_UsgDes_idx') DROP INDEX UsageDescription.lngval_UsgDes_idx;
CREATE INDEX value_UsgDes_idx ON UsageDescription(value);
CREATE INDEX lngval_UsgDes_idx ON UsageDescription(lang, value);


--Indexes on Assoc
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'src_Ass_idx') DROP INDEX Association.src_Ass_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'tgt_Ass_idx') DROP INDEX Association.tgt_Ass_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'type_Ass_idx') DROP INDEX Association.type_Ass_idx;
CREATE INDEX src_Ass_idx ON Association(sourceObject);
CREATE INDEX tgt_Ass_idx ON Association(targetObject);
CREATE INDEX type_Ass_idx ON Association(associationType);


--Indexes on Class
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'clsObj_Class_idx') DROP INDEX Classification.clsObj_Class_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'node_Class_idx') DROP INDEX Classification.node_Class_idx;
CREATE INDEX clsObj_Class_idx ON Classification(classifiedObject);
CREATE INDEX node_Class_idx ON Classification(classificationNode);


--Indexes on ClassNode
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'parent_Node_idx') DROP INDEX ClassificationNode.parent_Node_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'code_Node_idx') DROP INDEX ClassificationNode.code_Node_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'path_Node_idx') DROP INDEX ClassificationNode.path_Node_idx;
CREATE INDEX parent_Node_idx ON ClassificationNode(parent);
CREATE INDEX code_Node_idx ON ClassificationNode(code);
CREATE INDEX path_Node_idx ON ClassificationNode(path);

--Indexes on ExIdentifier
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'ro_EID_idx') DROP INDEX ExternalIdentifier.ro_EID_idx;
CREATE INDEX ro_EID_idx ON ExternalIdentifier(registryObject);


--Indexes on ExLink
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'uri_ExLink_idx') DROP INDEX ExternalLink.uri_ExLink_idx;
CREATE INDEX uri_ExLink_idx ON ExternalLink(externalURI);


--Indexes on ExtrinsicObject

--Indexes on Organization
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'parent_ORG_idx') DROP INDEX Organization.parent_ORG_idx;
CREATE INDEX parent_ORG_idx ON Organization(parent);


--Indexes on PostalAddress

IF EXISTS (SELECT name FROM sysindexes WHERE name = 'parent_PstlAdr_idx') DROP INDEX PostalAddress.parent_PstlAdr_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'city_PstlAdr_idx') DROP INDEX PostalAddress.city_PstlAdr_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'cntry_PstlAdr_idx') DROP INDEX PostalAddress.cntry_PstlAdr_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'pCode_PstlAdr_idx') DROP INDEX PostalAddress.pCode_PstlAdr_idx;
CREATE INDEX parent_PstlAdr_idx ON PostalAddress(parent);
CREATE INDEX city_PstlAdr_idx ON PostalAddress(city);
CREATE INDEX cntry_PstlAdr_idx ON PostalAddress(country);
CREATE INDEX pCode_PstlAdr_idx ON PostalAddress(postalCode);

--Indexes on EmailAddress
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'parent_EmlAdr_idx') DROP INDEX EmailAddress.parent_EmlAdr_idx;
CREATE INDEX parent_EmlAdr_idx ON EmailAddress(parent);


--Indexes on ServiceBinding
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'service_BIND_idx') DROP INDEX ServiceBinding.service_BIND_idx;
CREATE INDEX service_BIND_idx ON ServiceBinding(service);


--Indexes on Slot
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'parent_Slot_idx') DROP INDEX Slot.parent_Slot_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'name_Slot_idx') DROP INDEX Slot.name_Slot_idx;
CREATE INDEX parent_Slot_idx ON Slot(parent);
CREATE INDEX name_Slot_idx ON Slot(name_);

--Indexes on SpecLink
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'binding_SLnk_idx') DROP INDEX SpecificationLink.binding_SLnk_idx;
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'spec_SLnk_idx') DROP INDEX SpecificationLink.spec_SLnk_idx;
CREATE INDEX binding_SLnk_idx ON SpecificationLink(serviceBinding);
CREATE INDEX spec_SLnk_idx ON SpecificationLink(specificationObject);


--Indexes on TelephoneNumber

IF EXISTS (SELECT name FROM sysindexes WHERE name = 'parent_Phone_idx') DROP INDEX TelephoneNumber.parent_Phone_idx;
CREATE INDEX parent_Phone_idx ON TelephoneNumber(parent);


--Indexes on User
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lastNm_User_idx') DROP INDEX User_.lastNm_User_idx;
CREATE INDEX lastNm_User_idx ON User_(personName_lastName);


--Indexes on Person
IF EXISTS (SELECT name FROM sysindexes WHERE name = 'lastNm_Person_idx') DROP INDEX Person.lastNm_Person_idx;
CREATE INDEX lastNm_Person_idx ON Person(personName_lastName);

--Grant Privilages
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Association TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  AuditableEvent TO openxds;
GRANT  DELETE, INSERT, SELECT, UPDATE ON  AffectedObject TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Classification TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  ClassificationNode TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  ClassScheme TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Description TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  ExternalIdentifier TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  ExternalLink TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  ExtrinsicObject TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Federation TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Name_ TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  UsageDescription TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  ObjectRef TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Organization TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  RegistryPackage TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  PostalAddress TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  EmailAddress TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Registry TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Service TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  ServiceBinding TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Slot TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  SpecificationLink TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Subscription TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  NotifyAction TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Notification TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  NotificationObject TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  AdhocQuery TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  UsageParameter TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  TelephoneNumber TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  User_ TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Person TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  repository TO openxds; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  personidentifier TO openxds;
GRANT  DELETE, INSERT, SELECT, UPDATE ON  RepositoryItem TO openxds;


--Insert factory defined Users

--RegistryOperator
INSERT INTO User_ VALUES('urn:freebxml:registry:predefinedusers:registryoperator', null, 'urn:freebxml:registry:predefinedUser:RegistryOperator', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Person:User', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.0', null,  'Registry', null, 'Operator');
INSERT INTO PostalAddress VALUES('Burlington', 'USA', '01803', 'MA', 'Network Dr.', '1', 'urn:freebxml:registry:predefinedusers:registryoperator');
INSERT INTO TelephoneNumber VALUES('781', '01', '', '442-0703', 'OfficePhone', 'urn:freebxml:registry:predefinedusers:registryoperator');
INSERT INTO EmailAddress VALUES('registryOperator@ebxmlrr.com', 'OfficeEmail', 'urn:freebxml:registry:predefinedusers:registryoperator');
-- We make the owner of the User object to be the user itself.
INSERT INTO AuditableEvent VALUES('urn:freebxml:registry:predefinedEvent:createRegistryOperator', null, 'urn:freebxml:registry:predefinedEvent:createRegistryOperator', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:AuditableEvent', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.0', null,  '//TODO', 'urn:oasis:names:tc:ebxml-regrep:EventType:Created', '2003-12-10 10:53:24', 'urn:freebxml:registry:predefinedusers:registryoperator');
INSERT INTO AffectedObject VALUES('urn:freebxml:registry:predefinedusers:registryoperator', null, 'urn:freebxml:registry:predefinedEvent:createRegistryOperator');

--RegistryGuest
INSERT INTO User_ VALUES('urn:freebxml:registry:predefinedusers:registryguest', null, 'urn:freebxml:registry:predefinedusers:registryguest', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Person:User', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted',   '1.0', null,  'Registry', null, 'Guest');
INSERT INTO PostalAddress VALUES('Burlington', 'USA', '01803', 'MA', 'Network Dr.', '1', 'urn:freebxml:registry:predefinedusers:registryguest');
INSERT INTO TelephoneNumber VALUES('781', '01', '', '442-0703', 'OfficePhone', 'urn:freebxml:registry:predefinedusers:registryguest');
INSERT INTO EmailAddress VALUES('registryGuest@ebxmlrr.com', 'OfficeEmail', 'urn:freebxml:registry:predefinedusers:registryguest');
-- We make the owner of the User object to be the user itself.
INSERT INTO AuditableEvent VALUES('urn:freebxml:registry:predefinedEvent:createRegistryGuest', null, 'urn:freebxml:registry:predefinedEvent:createRegistryGuest', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:AuditableEvent', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.0', null,  '//TODO', 'urn:oasis:names:tc:ebxml-regrep:EventType:Created', '2003-12-10 10:53:24', 'urn:freebxml:registry:predefinedusers:registryguest');
INSERT INTO AffectedObject VALUES('urn:freebxml:registry:predefinedusers:registryguest', null, 'urn:freebxml:registry:predefinedEvent:createRegistryGuest');

--Test user Farrukh
INSERT INTO User_ VALUES('urn:freebxml:registry:predefinedusers:farrukh', null, 'urn:freebxml:registry:predefinedusers:farrukh', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Person:User', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted',  '1.0', null,  'Farrukh', 'Salahudin', 'Najmi');
INSERT INTO PostalAddress VALUES('Burlington', 'USA', '01803', 'MA', 'Network Dr.', '1', 'urn:freebxml:registry:predefinedusers:farrukh');
INSERT INTO TelephoneNumber VALUES('781', '01', '', '442-0703', 'OfficePhone', 'urn:freebxml:registry:predefinedusers:farrukh');
INSERT INTO EmailAddress VALUES('farrukh.najmi@sun.com', 'OfficeEmail', 'urn:freebxml:registry:predefinedusers:farrukh');
-- We make the owner of the User object to be the user itself.
INSERT INTO AuditableEvent VALUES('urn:freebxml:registry:predefinedEvent:createFarrukh', null, 'urn:freebxml:registry:predefinedEvent:createFarrukh', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:AuditableEvent', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.0', null,  '//TODO', 'urn:oasis:names:tc:ebxml-regrep:EventType:Created', '2003-12-10 10:53:24', 'urn:freebxml:registry:predefinedusers:farrukh');
INSERT INTO AffectedObject VALUES('urn:freebxml:registry:predefinedusers:farrukh', null, 'urn:freebxml:registry:predefinedEvent:createFarrukh');

--Test user Nikola
INSERT INTO User_ VALUES('urn:freebxml:registry:predefinedusers:nikola', null, 'urn:freebxml:registry:predefinedusers:nikola', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Person:User', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted',  '1.0', null,  'Nikola', null, 'Stojanovic');
INSERT INTO PostalAddress VALUES('Ithaca', 'France', null, 'NY', 'Terazije', '19', 'urn:freebxml:registry:predefinedusers:nikola');
INSERT INTO TelephoneNumber VALUES('11', '381', '', '222-2222', 'OfficePhone', 'urn:freebxml:registry:predefinedusers:nikola');
INSERT INTO TelephoneNumber VALUES('11', '381', '', '444-4444', 'HomePhone', 'urn:freebxml:registry:predefinedusers:nikola');
INSERT INTO EmailAddress VALUES('nhomest1@twcny.rr.com', 'OfficeEmail', 'urn:freebxml:registry:predefinedusers:nikola');
-- We make the owner of the User object to be the user itself.
INSERT INTO AuditableEvent VALUES('urn:freebxml:registry:predefinedEvent:createNikola', null, 'urn:freebxml:registry:predefinedEvent:createNikola', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:AuditableEvent', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.0', null,  '//TODO', 'urn:oasis:names:tc:ebxml-regrep:EventType:Created', '2003-12-10 10:53:24', 'urn:freebxml:registry:predefinedusers:nikola');
INSERT INTO AffectedObject VALUES('urn:freebxml:registry:predefinedusers:nikola', null, 'urn:freebxml:registry:predefinedEvent:createNikola');

--Insert IHE defined clssifications

--XDSSubmissionSet
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd', NULL, 'urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', NULL, 'XDSSubmissionSet', NULL, NULL);
INSERT INTO CLASSSCHEME VALUES ('urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d', NULL, 'urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', 'XDSSubmissionSet.author', 'F', 'urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSSCHEME VALUES ('urn:uuid:aa543740-bdda-424e-8c96-df4873be8500', NULL, 'urn:uuid:aa543740-bdda-424e-8c96-df4873be8500', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', 'XDSSubmissionSet.contentTypeCode', 'F', 'urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSSCHEME VALUES ('urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446', NULL, 'urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', 'XDSSubmissionSet.patientId', 'F', 'urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSSCHEME VALUES ('urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832', NULL, 'urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', 'XDSSubmissionSet.sourceId', 'F', 'urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSSCHEME VALUES ('urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8', NULL, 'urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', 'XDSSubmissionSet.uniqueId', 'F', 'urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');

--XDSDocumentEntry
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1', NULL, 'urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', NULL, 'XDSDocumentEntry', NULL, NULL);
INSERT INTO CLASSSCHEME VALUES ('urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d', NULL, 'urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', 'XDSDocumentEntry.author', 'F', 'urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSSCHEME VALUES ('urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a', NULL, 'urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', 'XDSDocumentEntry.classCode', 'F', 'urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSSCHEME VALUES ('urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f', NULL, 'urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', 'XDSDocumentEntry.confidentialityCode', 'F', 'urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSSCHEME VALUES ('urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4', NULL, 'urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', 'XDSDocumentEntry.eventCodeList', 'F', 'urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSSCHEME VALUES ('urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d', NULL, 'urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', 'XDSDocumentEntry.formatCode', 'F', 'urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSSCHEME VALUES ('urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1', NULL, 'urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', 'XDSDocumentEntry.healthCareFacilityTypeCode', 'F', 'urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSSCHEME VALUES ('urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427', NULL, 'urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', 'XDSDocumentEntry.patientId', 'F', 'urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSSCHEME VALUES ('urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead', NULL, 'urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', 'XDSDocumentEntry.practiceSettingCode', 'F', 'urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSSCHEME VALUES ('urn:uuid:f0306f51-975f-434e-a61c-c59651d33983', NULL, 'urn:uuid:f0306f51-975f-434e-a61c-c59651d33983', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', 'XDSDocumentEntry.typeCode', 'F', 'urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSSCHEME VALUES ('urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab', NULL, 'urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', 'XDSDocumentEntry.uniqueId', 'F', 'urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');

--XDSFolder
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2', NULL, 'urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', NULL, 'XDSFolder', NULL, NULL);
INSERT INTO CLASSSCHEME VALUES ('urn:uuid:1ba97051-7806-41a8-a48b-8fce7af683c5', NULL, 'urn:uuid:1ba97051-7806-41a8-a48b-8fce7af683c5', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', 'XDSFolder.codeList', 'F', 'urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSSCHEME VALUES ('urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a', NULL, 'urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', 'XDSFolder.patientId', 'F', 'urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSSCHEME VALUES ('urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a', NULL, 'urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', 'XDSFolder.uniqueId', 'F', 'urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');

--Document Relationships
INSERT INTO CLASSSCHEME VALUES ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1','AssociationType','F','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember', NULL, 'urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', NULL, 'HasMember', 'urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType', '/urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType/HasMember');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:ihe:iti:2007:AssociationType:APND', NULL, 'urn:ihe:iti:2007:AssociationType:APND', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', NULL, 'APND', 'urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType', '/urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType/APND');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:ihe:iti:2007:AssociationType:RPLC', NULL, 'urn:ihe:iti:2007:AssociationType:RPLC', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', NULL, 'RPLC', 'urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType', '/urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType/RPLC');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:ihe:iti:2007:AssociationType:XFRM', NULL, 'urn:ihe:iti:2007:AssociationType:XFRM', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', NULL, 'XFRM', 'urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType', '/urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType/XFRM');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:ihe:iti:2007:AssociationType:XFRM_RPLC', NULL, 'urn:ihe:iti:2007:AssociationType:XFRM_RPLC', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', NULL, 'XFRM_RPLC', 'urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType', '/urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType/XFRM_RPLC');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:ihe:iti:2007:AssociationType:signs', NULL, 'urn:ihe:iti:2007:AssociationType:signs', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', NULL, 'signs', 'urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType', '/urn:oasis:names:tc:ebxml-regrep:classificationScheme:AssociationType/signs');

--Others
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:uuid:10aa1a4b-715a-4120-bfd0-9760414112c8', NULL, 'urn:uuid:10aa1a4b-715a-4120-bfd0-9760414112c8', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', NULL, 'XDSDocumentEntryStub', NULL, NULL);
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:uuid:abd807a3-4432-4053-87b4-fd82c643d1f3', NULL, 'urn:uuid:abd807a3-4432-4053-87b4-fd82c643d1f3', 'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted', '1.1', NULL, 'Association Documentation', NULL, NULL);

--Insert Factory defined classifications
--StatusTypes
INSERT INTO CLASSSCHEME VALUES ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:StatusType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:StatusType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1','StatusType','T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved',NULL,'urn:oasis:names:tc:ebxml-regrep:StatusType:Approved','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Approved','urn:oasis:names:tc:ebxml-regrep:classificationScheme:StatusType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:StatusType/Approved');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted',NULL,'urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Approved','urn:oasis:names:tc:ebxml-regrep:classificationScheme:StatusType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:StatusType/Submitted');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated',NULL,'urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Approved','urn:oasis:names:tc:ebxml-regrep:classificationScheme:StatusType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:StatusType/Deprecated');

--DataTypes
INSERT INTO CLASSSCHEME VALUES ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1','DataType','T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:DataType:Boolean',NULL,'urn:oasis:names:tc:ebxml-regrep:DataType:Boolean','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Boolean','urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType/Boolean');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:DataType:Date',NULL,'urn:oasis:names:tc:ebxml-regrep:DataType:Date','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Date','urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType/Date');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:DataType:DateTime',NULL,'urn:oasis:names:tc:ebxml-regrep:DataType:DateTime','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'DateTime','urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType/DateTime');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:DataType:Double',NULL,'urn:oasis:names:tc:ebxml-regrep:DataType:Double','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Double','urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType/Double');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:DataType:Duration',NULL,'urn:oasis:names:tc:ebxml-regrep:DataType:Duration','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Duration','urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType/Duration');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:DataType:Float',NULL,'urn:oasis:names:tc:ebxml-regrep:DataType:Float','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Float','urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType/Float');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:DataType:Integer',NULL,'urn:oasis:names:tc:ebxml-regrep:DataType:Integer','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Integer','urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType/Integer');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:DataType:ObjectRef',NULL,'urn:oasis:names:tc:ebxml-regrep:DataType:ObjectRef','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'ObjectRef','urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType/ObjectRef');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:DataType:String',NULL,'urn:oasis:names:tc:ebxml-regrep:DataType:String','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'String','urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType/String');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:DataType:Time',NULL,'urn:oasis:names:tc:ebxml-regrep:DataType:Time','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Time','urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType/Time');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:DataType:URI',NULL,'urn:oasis:names:tc:ebxml-regrep:DataType:URI','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'URI','urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:DataType/URI');

--EventType
INSERT INTO CLASSSCHEME VALUES ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:EventType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:EventType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1','EventType','T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:EventType:Approved',NULL,'urn:oasis:names:tc:ebxml-regrep:EventType:Approved','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Approved','urn:oasis:names:tc:ebxml-regrep:classificationScheme:EventType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:EventType/Approved');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:EventType:Created',NULL,'urn:oasis:names:tc:ebxml-regrep:EventType:Created','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Created','urn:oasis:names:tc:ebxml-regrep:classificationScheme:EventType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:EventType/Created');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:EventType:Deleted',NULL,'urn:oasis:names:tc:ebxml-regrep:EventType:Deleted','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Deleted','urn:oasis:names:tc:ebxml-regrep:classificationScheme:EventType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:EventType/Deleted');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:EventType:Deprecated',NULL,'urn:oasis:names:tc:ebxml-regrep:EventType:Deprecated','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Deprecated','urn:oasis:names:tc:ebxml-regrep:classificationScheme:EventType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:EventType/Deprecated');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:EventType:Downloaded',NULL,'urn:oasis:names:tc:ebxml-regrep:EventType:Downloaded','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Downloaded','urn:oasis:names:tc:ebxml-regrep:classificationScheme:EventType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:EventType/Downloaded');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:EventType:Relocated',NULL,'urn:oasis:names:tc:ebxml-regrep:EventType:Relocated','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Relocated','urn:oasis:names:tc:ebxml-regrep:classificationScheme:EventType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:EventType/Relocated');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:EventType:Undeprecated',NULL,'urn:oasis:names:tc:ebxml-regrep:EventType:Undeprecated','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Undeprecated','urn:oasis:names:tc:ebxml-regrep:classificationScheme:EventType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:EventType/Undeprecated');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:EventType:Updated',NULL,'urn:oasis:names:tc:ebxml-regrep:EventType:Updated','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Updated','urn:oasis:names:tc:ebxml-regrep:classificationScheme:EventType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:EventType/Updated');

--EmailTypes
INSERT INTO CLASSSCHEME VALUES ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:EmailType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:EmailType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1','EmailType','T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:EmailType:HomeEmail',NULL,'urn:oasis:names:tc:ebxml-regrep:EmailType:HomeEmail','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'HomeEmail','urn:oasis:names:tc:ebxml-regrep:classificationScheme:EmailType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:EmailType/HomeEmail');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:EmailType:OfficeEmail',NULL,'urn:oasis:names:tc:ebxml-regrep:EmailType:OfficeEmail','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'OfficeEmail','urn:oasis:names:tc:ebxml-regrep:classificationScheme:EmailType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:EmailType/OfficeEmail');

--NodeTypes
INSERT INTO CLASSSCHEME VALUES ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:NodeType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:NodeType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1','NodeType','T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath',NULL,'urn:oasis:names:tc:ebxml-regrep:NodeType:EmbeddedPath','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'EmbeddedPath','urn:oasis:names:tc:ebxml-regrep:classificationScheme:NodeType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:NodeType/EmbeddedPath');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:NodeType:NonUniqueCode',NULL,'urn:oasis:names:tc:ebxml-regrep:NodeType:NonUniqueCode','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'NonUniqueCode','urn:oasis:names:tc:ebxml-regrep:classificationScheme:NodeType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:NodeType/NonUniqueCode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode',NULL,'urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'UniqueCode','urn:oasis:names:tc:ebxml-regrep:classificationScheme:NodeType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:NodeType/UniqueCode');

--NotificationOptionType
INSERT INTO CLASSSCHEME VALUES ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:NotificationOptionType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:NotificationOptionType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1','NotificationOptionType','T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:NotificationOptionType:ObjectRefs',NULL,'urn:oasis:names:tc:ebxml-regrep:NotificationOptionType:ObjectRefs','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'ObjectRefs','urn:oasis:names:tc:ebxml-regrep:classificationScheme:NotificationOptionType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:NotificationOptionType/ObjectRefs');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:NotificationOptionType:Objects',NULL,'urn:oasis:names:tc:ebxml-regrep:NotificationOptionType:Objects','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Objects','urn:oasis:names:tc:ebxml-regrep:classificationScheme:NotificationOptionType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:NotificationOptionType/Objects');

--PhoneType
INSERT INTO CLASSSCHEME VALUES ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:PhoneType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:PhoneType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1','PhoneType','T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:PhoneType:Beeper',NULL,'urn:oasis:names:tc:ebxml-regrep:PhoneType:Beeper','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Beeper','urn:oasis:names:tc:ebxml-regrep:classificationScheme:PhoneType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:PhoneType/Beeper');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:PhoneType:FAX',NULL,'urn:oasis:names:tc:ebxml-regrep:PhoneType:FAX','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'FAX','urn:oasis:names:tc:ebxml-regrep:classificationScheme:PhoneType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:PhoneType/FAX');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:PhoneType:HomePhone',NULL,'urn:oasis:names:tc:ebxml-regrep:PhoneType:HomePhone','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'HomePhone','urn:oasis:names:tc:ebxml-regrep:classificationScheme:PhoneType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:PhoneType/HomePhone');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:PhoneType:MobilePhone',NULL,'urn:oasis:names:tc:ebxml-regrep:PhoneType:MobilePhone','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'MobilePhone','urn:oasis:names:tc:ebxml-regrep:classificationScheme:PhoneType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:PhoneType/MobilePhone');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:PhoneType:OfficePhone',NULL,'urn:oasis:names:tc:ebxml-regrep:PhoneType:OfficePhone','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'OfficePhone','urn:oasis:names:tc:ebxml-regrep:classificationScheme:PhoneType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:PhoneType/OfficePhone');

--QueryLanguage
INSERT INTO CLASSSCHEME VALUES ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:QueryLanguage',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:QueryLanguage','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1','QueryLanguage','T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:QueryLanguage:ebRSFilterQuery',NULL,'urn:oasis:names:tc:ebxml-regrep:QueryLanguage:ebRSFilterQuery','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'ebRSFilterQuery','urn:oasis:names:tc:ebxml-regrep:classificationScheme:QueryLanguage','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:QueryLanguage/ebRSFilterQuery');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:QueryLanguage:SQL-92',NULL,'urn:oasis:names:tc:ebxml-regrep:QueryLanguage:SQL-92','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'SQL-92','urn:oasis:names:tc:ebxml-regrep:classificationScheme:QueryLanguage','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:QueryLanguage/SQL-92');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:QueryLanguage:XPath',NULL,'urn:oasis:names:tc:ebxml-regrep:QueryLanguage:XPath','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'XPath','urn:oasis:names:tc:ebxml-regrep:classificationScheme:QueryLanguage','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:QueryLanguage/XPath');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:QueryLanguage:XQuery',NULL,'urn:oasis:names:tc:ebxml-regrep:QueryLanguage:XQuery','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'XQuery','urn:oasis:names:tc:ebxml-regrep:classificationScheme:QueryLanguage','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:QueryLanguage/XQuery');

--SubjectRole
INSERT INTO CLASSSCHEME VALUES ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:SubjectRole',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:SubjectRole','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1','SubjectRole','T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:SubjectRole:ContentOwner',NULL,'urn:oasis:names:tc:ebxml-regrep:SubjectRole:ContentOwner','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'ContentOwner','urn:oasis:names:tc:ebxml-regrep:classificationScheme:SubjectRole','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:SubjectRole/ContentOwner');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:SubjectRole:Intermediary',NULL,'urn:oasis:names:tc:ebxml-regrep:SubjectRole:Intermediary','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Intermediary','urn:oasis:names:tc:ebxml-regrep:classificationScheme:SubjectRole','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:SubjectRole/Intermediary');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:SubjectRole:RegistryAdministrator',NULL,'urn:oasis:names:tc:ebxml-regrep:SubjectRole:RegistryAdministrator','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'RegistryAdministrator','urn:oasis:names:tc:ebxml-regrep:classificationScheme:SubjectRole','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:SubjectRole/RegistryAdministrator');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:SubjectRole:RegistryGuest',NULL,'urn:oasis:names:tc:ebxml-regrep:SubjectRole:RegistryGuest','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'RegistryGuest','urn:oasis:names:tc:ebxml-regrep:classificationScheme:SubjectRole','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:SubjectRole/RegistryGuest');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:freebxml:registry:demoDB:SubjectRole:ProjectMember',NULL,'urn:freebxml:registry:demoDB:SubjectRole:ProjectMember','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'ProjectMember','urn:oasis:names:tc:ebxml-regrep:classificationScheme:SubjectRole','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:SubjectRole/ProjectMember');
--ProtocolType
INSERT INTO CLASSSCHEME VALUES ('urn:oasis:names:tc:ebxml-regrep:profile:ws:classificationScheme:ProtocolType',NULL,'urn:oasis:names:tc:ebxml-regrep:profile:ws:classificationScheme:ProtocolType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1','ProtocolType','T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:profile:ws:ProtocolType:AS2',NULL,'urn:oasis:names:tc:ebxml-regrep:profile:ws:ProtocolType:AS2','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'AS2','urn:oasis:names:tc:ebxml-regrep:profile:ws:classificationScheme:ProtocolType','/urn:oasis:names:tc:ebxml-regrep:profile:ws:classificationScheme:ProtocolType/AS2');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:profile:ws:ProtocolType:Atom',NULL,'urn:oasis:names:tc:ebxml-regrep:profile:ws:ProtocolType:Atom','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Atom','urn:oasis:names:tc:ebxml-regrep:profile:ws:classificationScheme:ProtocolType','/urn:oasis:names:tc:ebxml-regrep:profile:ws:classificationScheme:ProtocolType/Atom');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:profile:ws:ProtocolType:SOAP',NULL,'urn:oasis:names:tc:ebxml-regrep:profile:ws:ProtocolType:SOAP','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'SOAP','urn:oasis:names:tc:ebxml-regrep:profile:ws:classificationScheme:ProtocolType','/urn:oasis:names:tc:ebxml-regrep:profile:ws:classificationScheme:ProtocolType/SOAP');

--SOAPStyleType
INSERT INTO CLASSSCHEME VALUES ('urn:oasis:names:tc:ebxml-regrep:profile:ws:classificationScheme:SOAPStyleType',NULL,'urn:oasis:names:tc:ebxml-regrep:profile:ws:classificationScheme:SOAPStyleType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1','SOAPStyleType','T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:profile:ws:SOAPStyleType:Document',NULL,'urn:oasis:names:tc:ebxml-regrep:profile:ws:SOAPStyleType:Document','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Document','urn:oasis:names:tc:ebxml-regrep:profile:ws:classificationScheme:SOAPStyleType','/urn:oasis:names:tc:ebxml-regrep:profile:ws:classificationScheme:SOAPStyleType/Document');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:profile:ws:SOAPStyleType:RPC',NULL,'urn:oasis:names:tc:ebxml-regrep:profile:ws:SOAPStyleType:RPC','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'RPC','urn:oasis:names:tc:ebxml-regrep:profile:ws:classificationScheme:SOAPStyleType','/urn:oasis:names:tc:ebxml-regrep:profile:ws:classificationScheme:SOAPStyleType/RPC');

--StabilityType
INSERT INTO CLASSSCHEME VALUES ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:StabilityType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:StabilityType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1','StabilityType','T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:StabilityType:Dynamic',NULL,'urn:oasis:names:tc:ebxml-regrep:StabilityType:Dynamic','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Dynamic','urn:oasis:names:tc:ebxml-regrep:classificationScheme:StabilityType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:StabilityType/Dynamic');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:StabilityType:DynamicCompatible',NULL,'urn:oasis:names:tc:ebxml-regrep:StabilityType:DynamicCompatible','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'DynamicCompatible','urn:oasis:names:tc:ebxml-regrep:classificationScheme:StabilityType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:StabilityType/DynamicCompatible');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:StabilityType:Static',NULL,'urn:oasis:names:tc:ebxml-regrep:StabilityType:Static','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Static','urn:oasis:names:tc:ebxml-regrep:classificationScheme:StabilityType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:StabilityType/Static');

--DeletionScopeType
INSERT INTO CLASSSCHEME VALUES ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:DeletionScopeType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:DeletionScopeType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1','DeletionScopeType','T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:DeletionScopeType:DeleteAll',NULL,'urn:oasis:names:tc:ebxml-regrep:DeletionScopeType:DeleteAll','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'DeleteAll','urn:oasis:names:tc:ebxml-regrep:classificationScheme:DeletionScopeType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:DeletionScopeType/DeleteAll');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:DeletionScopeType:DeleteRepositoryItemOnly',NULL,'urn:oasis:names:tc:ebxml-regrep:DeletionScopeType:DeleteRepositoryItemOnly','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'DeleteRepositoryItemOnly','urn:oasis:names:tc:ebxml-regrep:classificationScheme:DeletionScopeType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:DeletionScopeType/DeleteRepositoryItemOnly');

--ErrorHandlingModel
INSERT INTO CLASSSCHEME VALUES ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:ErrorHandlingModel',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:ErrorHandlingModel','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1','ErrorHandlingModel','T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ErrorHandlingModel:FailOnError',NULL,'urn:oasis:names:tc:ebxml-regrep:ErrorHandlingModel:FailOnError','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'FailOnError','urn:oasis:names:tc:ebxml-regrep:classificationScheme:ErrorHandlingModel','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ErrorHandlingModel/FailOnError');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ErrorHandlingModel:LogErrorAndContinue',NULL,'urn:oasis:names:tc:ebxml-regrep:ErrorHandlingModel:LogErrorAndContinue','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'LogErrorAndContinue','urn:oasis:names:tc:ebxml-regrep:classificationScheme:ErrorHandlingModel','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ErrorHandlingModel/LogErrorAndContinue');

--ErrorSeverityType
INSERT INTO CLASSSCHEME VALUES ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:ErrorSeverityType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:ErrorSeverityType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1','ErrorSeverityType','T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error',NULL,'urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Error','urn:oasis:names:tc:ebxml-regrep:classificationScheme:ErrorSeverityType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ErrorSeverityType/Error');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Warning',NULL,'urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Warning','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Warning','urn:oasis:names:tc:ebxml-regrep:classificationScheme:ErrorSeverityType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ErrorSeverityType/Warning');

--ObjectType
INSERT INTO CLASSSCHEME VALUES ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1','ObjectType','T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'RegistryObject','urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject');

--ResponseStatusType
INSERT INTO CLASSSCHEME VALUES ('urn:oasis:names:tc:ebxml-regrep:classificationScheme:ResponseStatusType',NULL,'urn:oasis:names:tc:ebxml-regrep:classificationScheme:ResponseStatusType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1','ResponseStatusType','T','urn:oasis:names:tc:ebxml-regrep:NodeType:UniqueCode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure',NULL,'urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Failure','urn:oasis:names:tc:ebxml-regrep:classificationScheme:ResponseStatusType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ResponseStatusType/Failure');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success',NULL,'urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Success','urn:oasis:names:tc:ebxml-regrep:classificationScheme:ResponseStatusType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ResponseStatusType/Success');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Unavailable',NULL,'urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Unavailable','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Unavailable','urn:oasis:names:tc:ebxml-regrep:classificationScheme:ResponseStatusType','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ResponseStatusType/Unavailable');

--other
INSERT INTO CLASSIFICATION VALUES ('urn:oasis:names:tc:ebxml-regrep:classification:RegistryAdministrator',NULL,'urn:oasis:names:tc:ebxml-regrep:classification:RegistryAdministrator','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'urn:oasis:names:tc:ebxml-regrep:SubjectRole:RegistryAdministrator',NULL,'urn:freebxml:registry:predefinedusers:registryoperator',NULL);

--RegistryObject
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:AdhocQuery',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:AdhocQuery','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'AdhocQuery','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/AdhocQuery');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Association','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/Association');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:AuditableEvent',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:AuditableEvent','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'AuditableEvent','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/AuditableEvent');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Classification','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/Classification');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'ClassificationNode','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ClassificationNode');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'ClassificationScheme','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ClassificationScheme');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'ExternalIdentifier','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExternalIdentifier');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalLink',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalLink','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'ExternalLink','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExternalLink');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'ExtrinsicObject','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExtrinsicObject');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:image',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:image','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'image','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExtrinsicObject/image');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:image:gif',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:image:gif','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'gif','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:image','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExtrinsicObject/image/gif');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:image:jpeg',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:image:jpeg','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'jpeg','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:image','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExtrinsicObject/image/jpeg');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:WSDL',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:WSDL','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'WSDL','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExtrinsicObject/WSDL');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:WSDL:Binding',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:WSDL:Binding','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Binding','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:WSDL','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExtrinsicObject/WSDL/Binding');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:WSDL:Port',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:WSDL:Port','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Port','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:WSDL','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExtrinsicObject/WSDL/Port');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:WSDL:PortType',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:WSDL:PortType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'PortType','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:WSDL','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExtrinsicObject/WSDL/PortType');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:WSDL:Service',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:WSDL:Service','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Service','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:WSDL','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExtrinsicObject/WSDL/Service');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XACML',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XACML','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'XACML','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExtrinsicObject/XACML');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XACML:Policy',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XACML:Policy','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Policy','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XACML','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExtrinsicObject/XACML/Policy');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XACML:PolicySet',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XACML:PolicySet','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'PolicySet','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XACML','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExtrinsicObject/XACML/PolicySet');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XHTML',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XHTML','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'XHTML','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExtrinsicObject/XHTML');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XHTML:XForm',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XHTML:XForm','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'XForm','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XHTML','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExtrinsicObject/XHTML/XForm');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XML',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XML','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'XML','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExtrinsicObject/XML');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XML:Schematron',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XML:Schematron','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Schematron','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExtrinsicObject/Schematron');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XMLSchema',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XMLSchema','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'XMLSchema','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExtrinsicObject/XMLSchema');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XSLT',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject:XSLT','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'XSLT','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExtrinsicObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ExtrinsicObject/XSLT');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Federation',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Federation','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Federation','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/Federation');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Notification',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Notification','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Notification','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/Notification');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Organization',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Organization','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Organization','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/Organization');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Person',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Person','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Person','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/Person');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Person:User',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Person:User','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'User','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Person','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/Person/User');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Registry',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Registry','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Registry','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/Registry');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'RegistryPackage','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/RegistryPackage');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Service',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Service','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Service','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/Service');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ServiceBinding',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ServiceBinding','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'ServiceBinding','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/ServiceBinding');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:SpecificationLink',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:SpecificationLink','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'SpecificationLink','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/SpecificationLink');
INSERT INTO CLASSIFICATIONNODE VALUES ('urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Subscription',NULL,'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Subscription','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ClassificationNode','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'Subscription','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject','/urn:oasis:names:tc:ebxml-regrep:classificationScheme:ObjectType/RegistryObject/Subscription');

