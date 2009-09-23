DROP TABLE IF EXISTS Association CASCADE;
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

DROP TABLE IF EXISTS AuditableEvent CASCADE;
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

DROP TABLE IF EXISTS AffectedObject CASCADE;
CREATE TABLE AffectedObject (

--Each row is a relationship between a RegistryObject and an AuditableEvent
--Enables many-to-many relationship between effected RegistryObjects and AuditableEvents
  id                            VARCHAR(256) NOT NULL,
  home                      VARCHAR(256),
  eventId                   VARCHAR(256) NOT NULL,

  PRIMARY KEY (id, eventId)
);

DROP TABLE IF EXISTS Classification CASCADE;
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

DROP TABLE IF EXISTS ClassificationNode CASCADE;
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

DROP TABLE IF EXISTS ClassScheme CASCADE;
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

DROP TABLE IF EXISTS ExternalIdentifier CASCADE;
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

DROP TABLE IF EXISTS ExternalLink CASCADE;
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

DROP TABLE IF EXISTS ExtrinsicObject CASCADE;
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

DROP TABLE IF EXISTS Federation CASCADE;
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

DROP TABLE IF EXISTS Name_ CASCADE;
CREATE TABLE Name_ (
--LocalizedString attributes flattened for Name
  charset			VARCHAR(32),
  lang				VARCHAR(32) NOT NULL,
  value				VARCHAR(1024) NOT NULL,
--The RegistryObject id for the parent RegistryObject for which this is a Name
  parent			VARCHAR(256) NOT NULL,
  PRIMARY KEY (parent, lang)
);

DROP TABLE IF EXISTS Description CASCADE;
CREATE TABLE Description (
--LocalizedString attributes flattened for Description
  charset			VARCHAR(32),
  lang				VARCHAR(32) NOT NULL,
  value				VARCHAR(1024) NOT NULL,
--The RegistryObject id for the parent RegistryObject for which this is a Name
  parent			VARCHAR(256) NOT NULL,
  PRIMARY KEY (parent, lang)
);

DROP TABLE IF EXISTS UsageDescription CASCADE;
CREATE TABLE UsageDescription (
--LocalizedString attributes flattened for UsageDescription
  charset			VARCHAR(32),
  lang				VARCHAR(32) NOT NULL,
  value				VARCHAR(1024) NOT NULL,
--The RegistryObject id for the parent RegistryObject for which this is a Name
  parent			VARCHAR(256) NOT NULL,
  PRIMARY KEY (parent, lang)
);

DROP TABLE IF EXISTS ObjectRef CASCADE;
CREATE TABLE ObjectRef (
--Stores remote ObjectRefs only
--Identifiable Attributes
  id				VARCHAR(256) NOT NULL PRIMARY KEY,
  home                          VARCHAR(256)
);

DROP TABLE IF EXISTS Organization CASCADE;
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

DROP TABLE IF EXISTS RegistryPackage CASCADE;
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

DROP TABLE IF EXISTS PostalAddress CASCADE;
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

DROP TABLE IF EXISTS EmailAddress CASCADE;
CREATE TABLE EmailAddress (
  address			VARCHAR(64) NOT NULL,
  type				VARCHAR(256),
--The parent object that this is an email address for
  parent			VARCHAR(256) NOT NULL
);

DROP TABLE IF EXISTS Registry CASCADE;
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

DROP TABLE IF EXISTS Service CASCADE;
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

DROP TABLE IF EXISTS ServiceBinding CASCADE;
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

DROP TABLE IF EXISTS Slot CASCADE;
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

DROP TABLE IF EXISTS SpecificationLink CASCADE;
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

DROP TABLE IF EXISTS Subscription CASCADE;
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

DROP TABLE IF EXISTS NotifyAction CASCADE;
CREATE TABLE NotifyAction (
  notificationOption       VARCHAR(256) NOT NULL,

--Either a ref to a Service, a String representing an email address in form: mailto:user@server,
--or a String representing an http URLin form: http://url
  endPoint                    VARCHAR(256) NOT NULL,

--Parent Subscription reference
  parent			VARCHAR(256) NOT NULL
);

DROP TABLE IF EXISTS Notification CASCADE;
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

DROP TABLE IF EXISTS NotificationObject CASCADE;
CREATE TABLE NotificationObject (

--Each row is a relationship between a RegistryObject and a Notification
--Enables a Notification to have multiple RegistryObjects
  notificationId             VARCHAR(256) NOT NULL,
  registryObjectId           VARCHAR(256) NOT NULL,

  PRIMARY KEY (notificationId, registryObjectId)
);

DROP TABLE IF EXISTS AdhocQuery CASCADE;
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

DROP TABLE IF EXISTS UsageParameter CASCADE;
CREATE TABLE UsageParameter (
  value				VARCHAR(1024) NOT NULL,
--The parent SpecificationLink that this is a usage parameter for
  parent			VARCHAR(256) NOT NULL
);

DROP TABLE IF EXISTS TelephoneNumber CASCADE;
CREATE TABLE TelephoneNumber (
  areaCode			VARCHAR(8),
  countryCode		VARCHAR(8),
  extension			VARCHAR(8),
  -- we use "number_" instead of number, which is reserved in Oracle
  number_			VARCHAR(16),
  phoneType			VARCHAR(256),
  parent			VARCHAR(256) NOT NULL
);

DROP TABLE IF EXISTS User_ CASCADE;
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

DROP TABLE IF EXISTS Person CASCADE;
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

DROP TABLE IF EXISTS RepositoryItem CASCADE;

DROP VIEW IF EXISTS Identifiable CASCADE;
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

DROP VIEW IF EXISTS RegistryObject CASCADE;
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
DROP INDEX IF EXISTS lid_AdhQuery_idx;
DROP INDEX IF EXISTS lid_Assoc_idx;
DROP INDEX IF EXISTS lid_AUEVENT_idx;
DROP INDEX IF EXISTS lid_Class_idx;
DROP INDEX IF EXISTS lid_Node_idx;
DROP INDEX IF EXISTS lid_SCHEME_idx;
DROP INDEX IF EXISTS lid_EID_idx;
DROP INDEX IF EXISTS lid_ExLink_idx;
DROP INDEX IF EXISTS lid_EXTOBJ_idx;
DROP INDEX IF EXISTS lid_FED_idx;
DROP INDEX IF EXISTS lid_ORG_idx;
DROP INDEX IF EXISTS lid_Registry_idx;
DROP INDEX IF EXISTS lid_PKG_idx;
DROP INDEX IF EXISTS lid_Service_idx;
DROP INDEX IF EXISTS lid_BIND_idx;
DROP INDEX IF EXISTS lid_SLnk_idx;
DROP INDEX IF EXISTS lid_SUBS_idx;
DROP INDEX IF EXISTS lid_User_idx;
DROP INDEX IF EXISTS lid_Person_idx;

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
DROP INDEX IF EXISTS id_AdhQuery_idx;
DROP INDEX IF EXISTS id_Assoc_idx;
DROP INDEX IF EXISTS id_AUEVENT_idx;
DROP INDEX IF EXISTS id_Class_idx;
DROP INDEX IF EXISTS id_Node_idx;
DROP INDEX IF EXISTS id_SCHEME_idx;
DROP INDEX IF EXISTS id_EID_idx;
DROP INDEX IF EXISTS id_ExLink_idx;
DROP INDEX IF EXISTS id_EXTOBJ_idx;
DROP INDEX IF EXISTS id_FED_idx;
DROP INDEX IF EXISTS id_ObjectRef_idx;
DROP INDEX IF EXISTS id_ORG_idx;
DROP INDEX IF EXISTS id_Registry_idx;
DROP INDEX IF EXISTS id_PKG_idx;
DROP INDEX IF EXISTS id_Service_idx;
DROP INDEX IF EXISTS id_BIND_idx;
DROP INDEX IF EXISTS id_SLnk_idx;
DROP INDEX IF EXISTS id_SUBS_idx;
DROP INDEX IF EXISTS id_User_idx;
DROP INDEX IF EXISTS id_Person_idx;

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
DROP INDEX IF EXISTS home_AdhQuery_idx;
DROP INDEX IF EXISTS home_Assoc_idx;
DROP INDEX IF EXISTS home_AUEVENT_idx;
DROP INDEX IF EXISTS home_Class_idx;
DROP INDEX IF EXISTS home_Node_idx;
DROP INDEX IF EXISTS home_SCHEME_idx;
DROP INDEX IF EXISTS home_EID_idx;
DROP INDEX IF EXISTS home_ExLink_idx;
DROP INDEX IF EXISTS home_EXTOBJ_idx;
DROP INDEX IF EXISTS home_FED_idx;
DROP INDEX IF EXISTS home_ORG_idx;
DROP INDEX IF EXISTS home_Registry_idx;
DROP INDEX IF EXISTS home_PKG_idx;
DROP INDEX IF EXISTS home_Service_idx;
DROP INDEX IF EXISTS home_BIND_idx;
DROP INDEX IF EXISTS home_SLnk_idx;
DROP INDEX IF EXISTS home_SUBS_idx;
DROP INDEX IF EXISTS home_User_idx;
DROP INDEX IF EXISTS home_Person_idx;

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

DROP INDEX IF EXISTS id_evId_AFOBJ_idx;
CREATE INDEX id_evId_AFOBJ_idx ON AffectedObject(id, eventId);

--
-- Following indexes should be OK to use in all databases
--

--index on AffectedObject
DROP INDEX IF EXISTS id_AFOBJ_idx;
DROP INDEX IF EXISTS evid_AFOBJ_idx;
CREATE INDEX id_AFOBJ_idx ON AffectedObject(id);
CREATE INDEX evid_AFOBJ_idx ON AffectedObject(eventid);


--index on AuditableEvent
CREATE INDEX lid_AUEVENT_evtTyp ON AuditableEvent(eventType);

--name index
DROP INDEX IF EXISTS value_Name_idx;
DROP INDEX IF EXISTS lngval_Name_idx;
CREATE INDEX value_Name_idx ON Name_(value);
CREATE INDEX lngval_Name_idx ON Name_(lang, value);

--description index
DROP INDEX IF EXISTS value_Desc_idx;
DROP INDEX IF EXISTS lngval_Desc_idx;
CREATE INDEX value_Desc_idx ON Description(value);
CREATE INDEX lngval_Desc_idx ON Description(lang, value);

--UsageDesc index
DROP INDEX IF EXISTS value_UsgDes_idx;
DROP INDEX IF EXISTS lngval_UsgDes_idx;
CREATE INDEX value_UsgDes_idx ON UsageDescription(value);
CREATE INDEX lngval_UsgDes_idx ON UsageDescription(lang, value);


--Indexes on Assoc
DROP INDEX IF EXISTS src_Ass_idx;
DROP INDEX IF EXISTS tgt_Ass_idx;
DROP INDEX IF EXISTS type_Ass_idx;
CREATE INDEX src_Ass_idx ON Association(sourceObject);
CREATE INDEX tgt_Ass_idx ON Association(targetObject);
CREATE INDEX type_Ass_idx ON Association(associationType);


--Indexes on Class
DROP INDEX IF EXISTS clsObj_Class_idx;
DROP INDEX IF EXISTS node_Class_idx;
CREATE INDEX clsObj_Class_idx ON Classification(classifiedObject);
CREATE INDEX node_Class_idx ON Classification(classificationNode);


--Indexes on ClassNode
DROP INDEX IF EXISTS parent_Node_idx;
DROP INDEX IF EXISTS code_Node_idx;
DROP INDEX IF EXISTS path_Node_idx;
CREATE INDEX parent_Node_idx ON ClassificationNode(parent);
CREATE INDEX code_Node_idx ON ClassificationNode(code);
CREATE INDEX path_Node_idx ON ClassificationNode(path);

--Indexes on ExIdentifier
DROP INDEX IF EXISTS ro_EID_idx;
CREATE INDEX ro_EID_idx ON ExternalIdentifier(registryObject);


--Indexes on ExLink
DROP INDEX IF EXISTS uri_ExLink_idx;
CREATE INDEX uri_ExLink_idx ON ExternalLink(externalURI);


--Indexes on ExtrinsicObject

--Indexes on Organization
DROP INDEX IF EXISTS parent_ORG_idx;
CREATE INDEX parent_ORG_idx ON Organization(parent);


--Indexes on PostalAddress
DROP INDEX IF EXISTS parent_PstlAdr_idx;
DROP INDEX IF EXISTS city_PstlAdr_idx;
DROP INDEX IF EXISTS cntry_PstlAdr_idx;
DROP INDEX IF EXISTS pCode_PstlAdr_idx;
CREATE INDEX parent_PstlAdr_idx ON PostalAddress(parent);
CREATE INDEX city_PstlAdr_idx ON PostalAddress(city);
CREATE INDEX cntry_PstlAdr_idx ON PostalAddress(country);
CREATE INDEX pCode_PstlAdr_idx ON PostalAddress(postalCode);

--Indexes on EmailAddress
DROP INDEX IF EXISTS parent_EmlAdr_idx;
CREATE INDEX parent_EmlAdr_idx ON EmailAddress(parent);


--Indexes on ServiceBinding
DROP INDEX IF EXISTS service_BIND_idx;
CREATE INDEX service_BIND_idx ON ServiceBinding(service);


--Indexes on Slot
DROP INDEX IF EXISTS parent_Slot_idx;
DROP INDEX IF EXISTS name_Slot_idx;
CREATE INDEX parent_Slot_idx ON Slot(parent);
CREATE INDEX name_Slot_idx ON Slot(name_);

--Indexes on SpecLink
DROP INDEX IF EXISTS binding_SLnk_idx;
DROP INDEX IF EXISTS spec_SLnk_idx;
CREATE INDEX binding_SLnk_idx ON SpecificationLink(serviceBinding);
CREATE INDEX spec_SLnk_idx ON SpecificationLink(specificationObject);


--Indexes on TelephoneNumber

DROP INDEX IF EXISTS parent_Phone_idx;
CREATE INDEX parent_Phone_idx ON TelephoneNumber(parent);


--Indexes on User
DROP INDEX IF EXISTS lastNm_User_idx;
CREATE INDEX lastNm_User_idx ON User_(personName_lastName);


--Indexes on Person
DROP INDEX IF EXISTS lastNm_Person_idx;
CREATE INDEX lastNm_Person_idx ON Person(personName_lastName);

--Grant Privilages
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Association TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  AuditableEvent TO public;
GRANT  DELETE, INSERT, SELECT, UPDATE ON  AffectedObject TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Classification TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  ClassificationNode TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  ClassScheme TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  ExternalIdentifier TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  ExternalLink TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  ExtrinsicObject TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Federation TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Name_ TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  UsageDescription TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  ObjectRef TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Organization TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  RegistryPackage TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  PostalAddress TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  EmailAddress TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Registry TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Service TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  ServiceBinding TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Slot TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  SpecificationLink TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Subscription TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  NotifyAction TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Notification TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  NotificationObject TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  AdhocQuery TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  UsageParameter TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  TelephoneNumber TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  User_ TO public; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  Person TO public; 



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

--Insert StoredQuery 

--Find Documents

INSERT INTO ADHOCQUERY VALUES ('urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d',NULL,'urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:AdhocQuery','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'urn:oasis:names:tc:ebxml-regrep:QueryLanguage:SQL-92',
'SELECT DISTINCT doc.*
FROM ExtrinsicObject doc, ExternalIdentifier patId
, Classification clCode  
, Classification psc  
, Classification hftc  
, Classification ecl  
, Slot clCodeScheme  
, Slot psCodeScheme  
, Slot crTimef  
, Slot crTimet  
, Slot serStartTimef  
, Slot serStartTimet  
, Slot serStopTimef  
, Slot serStopTimet  
, Slot hftcScheme  
, Slot eclScheme  
, Classification conf  
, Classfication fmtCode  
WHERE
doc.objecttype = ''urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1''
 AND (doc.id = patId.registryobject AND
patId.identificationScheme=''urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427'' AND
patId.value = $XDSDocumentEntryPatientId )
AND (clCode.classifiedobject = doc.id AND 
 clCode.classificationScheme = ''urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a'' AND 
clCode.nodeRepresentation IN ($XDSDocumentEntryClassCode) )
AND (clCodeScheme.parent = clCode.id AND  
 clCodeScheme.name = ''codingScheme'' AND 
clCodeScheme.value IN ($XDSDocumentEntryClassCodeScheme))  
AND (psc.classifiedObject = doc.id AND  
psc.classificationScheme=''urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead'' AND  
psc.nodeRepresentation IN ($XDSDocumentEntryPracticeSettingCode))
AND (psCodeScheme.parent = psc.id AND  
 psCodeScheme.name = ''codingScheme'' AND  
psCodeScheme.value IN ($XDSDocumentEntryPracticeSettingCodeScheme))  
AND (crTimef.parent = doc.id AND  
crTimef.name = ''creationTime'' AND  
 $XDSDocumentEntryCreationTimeFrom <= crTimef.value )  
AND (crTimet.parent = doc.id AND 
crTimet.name = ''creationTime'' AND 
crTimet.value < $XDSDocumentEntryCreationTimeTo) 
AND (serStartTimef.parent = doc.id AND  
serStartTimef.name = ''serviceStartTime'' AND  
 $XDSDocumentEntryServiceStartTimeFrom <= serStartTimef.value )  
AND (serStartTimet.parent = doc.id AND  
serStartTimet.name = ''serviceStartTime'' AND  
serStartTimet.value < $XDSDocumentEntryServiceStartTimeTo)  
AND (serStopTimef.parent = doc.id AND  
serStopTimef.name = ''serviceStopTime'' AND  
$XDSDocumentEntryServiceStopTimeFrom <= serStopTimef.value )  
AND (serStopTimet.parent = doc.id AND  
serStopTimet.name = ''serviceStopTime'' AND  
serStopTimet.value < $XDSDocumentEntryServiceStopTimeTo) 
AND (hftc.classifiedObject = doc.id AND  
hftc.classificationScheme = ''urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1'' AND  
hftc.nodeRepresentation IN ($XDSDocumentEntryhealthcareFacilityTypeCode ))
AND (hftcScheme.parent = hftc.id AND  
hftcScheme.name = ''codingScheme'' AND  
hftcScheme.value IN ($XDSDocumentEntryHealthcareFacilityTypeCodeScheme))  
AND (ecl.classifiedObject = doc.id AND  
ecl.classificationScheme = ''urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4'' AND
ecl.nodeRepresentation IN ($XDSDocumentEntryEventCodeList))
AND (eclScheme.parent = ecl.id AND 
 eclScheme.name = ''codingScheme'' AND
 eclScheme.value IN ($XDSDocumentEntryEventCodeListScheme))
AND (conf.classifiedObject = doc.id AND 
 conf.classificationScheme = ''urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f'' AND 
 conf.nodeRepresentation IN ($XDSDocumentEntryConfidentialityCode ))
AND (fmtCode.classifiedObject = doc.id AND 
     fmtCode.classificationScheme = ''urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d'' AND
     fmtCode.nodeRepresentation IN ($XDSDocumentEntryFormatCode))
AND doc.status = ($XDSDocumentEntryStatus)');

--FindSubmisstionSets

INSERT INTO ADHOCQUERY VALUES ('urn:uuid:f26abbcb-ac74-4422-8a30-edb644bbc1a9',NULL,'urn:uuid:f26abbcb-ac74-4422-8a30-edb644bbc1a9','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:AdhocQuery','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'urn:oasis:names:tc:ebxml-regrep:QueryLanguage:SQL-92',
'SELECT ss.* FROM RegistryPackage ss, ExternalIdentifier patId 
, Slot subTimeFrom 
, Slot subTimeTo 
, Slot ap 
, ExternalIdentifier sid 
, Classification ctc 
WHERE ( ss.id = patId.registryobject AND 
	  patId.identificationScheme= ''urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446'' AND 
	  patId.value = ($XDSSubmissionSetPatientId))
AND ( sid.registryobject = ss.id AND 
	sid.identificationScheme = ''urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832'' AND 
	sid.value IN ($XDSSubmissionSetSourceId))
AND ( subTimeFrom.parent = ss.id AND
	subTimeFrom.name = ''submissionTime'' AND
	subTimeFrom.value >= ($XDSSubmissionSetSubmissionTimeFrom))
AND (subTimeTo.parent = ss.id AND
	subTimeTo.name = ''submissionTime'' AND
	subTimeTo.value < ($XDSSubmissionSetSubmissionTimeTo))
AND ( ap.parent = ss.id AND 
	ap.name = ''authorPerson'' AND 
	ap.value LIKE $XDSSubmissionSetAuthorPerson)
AND ( ctc.classifiedObject = ss.id AND 
	ctc.classificationScheme = ''urn:uuid:aa543740-bdda-424e-8c96-df4873be8500'' AND
	ctc.nodeRepresentation IN ($XDSSubmissionSetContentTypeCode))
AND ss.status = ($XDSFolderStatus)');	

--GetDocuments

INSERT INTO ADHOCQUERY VALUES('urn:uuid:5c4f972b-d56b-40ac-a5fc-c8ca9b40b9d4',NULL,'urn:uuid:5c4f972b-d56b-40ac-a5fc-c8ca9b40b9d4','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:AdhocQuery','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'urn:oasis:names:tc:ebxml-regrep:QueryLanguage:SQL-92',
'SELECT eo.* from ExtrinsicObject eo, ExternalIdentifier ei
WHERE
(ei.registryObject = eo.id AND
ei.identificationScheme = ''urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab'' AND
(ei.value IN ($XDSDocumentEntryUniqueId)))
AND
(eo.id IN ($XDSDocumentEntryEntryUUID))');

--GetAssociations

INSERT INTO ADHOCQUERY VALUES ('urn:uuid:a7ae438b-4bc2-4642-93e9-be891f7bb155',NULL,'urn:uuid:a7ae438b-4bc2-4642-93e9-be891f7bb155','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:AdhocQuery','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'urn:oasis:names:tc:ebxml-regrep:QueryLanguage:SQL-92',
'SELECT DISTINCT ass.* FROM Association ass 
WHERE ass.sourceObject IN ($uuid)OR
      ass.targetObject IN ($uuid)');

--GetSubmissionSets

INSERT INTO ADHOCQUERY VALUES ('urn:uuid:51224314-5390-4169-9b91-b1980040715a',NULL,'urn:uuid:51224314-5390-4169-9b91-b1980040715a','urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:AdhocQuery','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'urn:oasis:names:tc:ebxml-regrep:QueryLanguage:SQL-92',
'SELECT ss.* FROM RegistryPackage ss,
Classification c,
Association a
WHERE 
c.classifiedObject = ss.id AND 
c.classificationNode = ''urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd'' AND 
a.sourceObject = ss.id AND 
a.associationType = ''urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember'' AND 
a.targetObject IN ($uuid)');

--FindFolders

INSERT INTO ADHOCQUERY VALUES ('urn:uuid:958f3006-baad-4929-a4de-ff1114824431',NULL,'urn:uuid:958f3006-baad-4929-a4de-ff1114824431',
'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:AdhocQuery','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'urn:oasis:names:tc:ebxml-regrep:QueryLanguage:SQL-92',
'SELECT fol.* FROM RegistryPackage fol, ExternalIdentifier patId
, Slot lupdateTimef 
, Slot lupdateTimet 
, Classification cl 
, Slot clScheme 
WHERE ( patId.registryobject = fol.id AND 
	patId.identificationScheme = ''urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a'' AND 
	patId.value = ($XDSFolderPatientId))
AND ( lupdateTimef.parent = fol.id AND 
	lupdateTimef.name = ''lastUpdateTime'' AND 
	lupdateTimef.value >= ($XDSFolderLastUpdateTimeFrom))
AND ( lupdateTimet.parent = fol.id AND
	lupdateTimet.name = ''lastUpdateTime'' AND
	lupdateTimef.value < ($XDSFolderLastUpdateTimeTo))
AND (cl.classifiedObject = fol.id AND
	cl.classificationScheme = ''urn:uuid:1ba97051-7806-41a8-a48b-8fce7af683c5'' AND
	cl.nodeRepresentation IN ($XDSFolderCodeList))
AND (clScheme.parent = cl.id AND 
	clScheme.name = ''codingScheme'' AND	
	clScheme.value = ($XDSFolderCodeListScheme))
AND fol.status = ($XDSFolderStatus)');

--GetFolder

INSERT INTO ADHOCQUERY VALUES('urn:uuid:5737b14c-8a1a-4539-b659-e03a34a5e1e4',NULL,'urn:uuid:5737b14c-8a1a-4539-b659-e03a34a5e1e4',
'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:AdhocQuery','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'urn:oasis:names:tc:ebxml-regrep:QueryLanguage:SQL-92',
'SELECT fol.* from RegistryPackage fol, 
ExternalIdentifier uniq 
WHERE 
(uniq.registryObject = fol.id AND 
uniq.identificationScheme = ''urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a'' AND
uniq.value IN ($XDSFolderUniqueId))
AND 
(fol.id IN ($XDSFolderEntryUUID))');

--GetFoldersForDocuments

INSERT INTO ADHOCQUERY VALUES ('urn:uuid:10cae35a-c7f9-4cf5-b61e-fc3278ffb578',NULL,'urn:uuid:10cae35a-c7f9-4cf5-b61e-fc3278ffb578',
'urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:AdhocQuery','urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted','1.1',NULL,'urn:oasis:names:tc:ebxml-regrep:QueryLanguage:SQL-92',
'SELECT fol.* FROM RegistryPackage fol,
Association a, 
ExtrinsicObject doc, 
Classification c
WHERE 
doc.id IN (SELECT doc.id FROM ExtrinsicObject doc, ExternalIdentifier uniqId 
	WHERE
	(uniqId.registryobject = doc.id AND 
	uniqId.identificationScheme = ''urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab'' AND
	uniqId.value = $XDSDocumentEntryUniqueId)
	AND (doc.id = $XDSDocumentEntryEntryUUID) )
AND 
a.targetObject = doc.id AND 
a.associationType = ''urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember'' AND
a.sourceObject = fol.id AND 
c.classifiedObject = fol.id AND 
c.classificationNode = ''urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2''');