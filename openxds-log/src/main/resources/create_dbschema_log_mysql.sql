/*Table structure for table `ip` */

DROP TABLE IF EXISTS `ip`;

CREATE TABLE `ip` (
  `ip` varchar(255) NOT NULL,
  `company_name` varchar(255) default NULL,
  `email` varchar(255) default NULL,
  PRIMARY KEY  (`ip`)
);

/*Table structure for table `main` */

DROP TABLE IF EXISTS `main`;

CREATE TABLE `main` (
  `messageID` varchar(255) NOT NULL,
  `pass` bit(1) default NULL,
  `is_secure` bit(1) default NULL,
  `test` mediumtext,
  `timereceived` datetime default NULL,
  `ip` varchar(255) default NULL,
  PRIMARY KEY  (`messageID`),
  CONSTRAINT `FK_MAIN_IP` FOREIGN KEY (`ip`) REFERENCES `ip` (`ip`)
);

/*Table structure for table `error` */

DROP TABLE IF EXISTS `error`;

CREATE TABLE `error` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) default NULL,
  `value` mediumtext,
  `messageID` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  CONSTRAINT `FK_ERR_messageID` FOREIGN KEY (`messageID`) REFERENCES `main` (`messageID`)
);

/*Table structure for table `http` */

DROP TABLE IF EXISTS `http`;

CREATE TABLE `http` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) default NULL,
  `value` mediumtext,
  `messageID` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  CONSTRAINT `FK_HTTP_messageID` FOREIGN KEY (`messageID`) REFERENCES `main` (`messageID`)
);

/*Table structure for table `other` */

DROP TABLE IF EXISTS `other`;

CREATE TABLE `other` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) default NULL,
  `value` mediumtext,
  `messageID` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  CONSTRAINT `FK_OTHER_messageID` FOREIGN KEY (`messageID`) REFERENCES `main` (`messageID`)
);

/*Table structure for table `soap` */

DROP TABLE IF EXISTS `soap`;

CREATE TABLE `soap` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) default NULL,
  `value` mediumtext,
  `messageID` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  CONSTRAINT `FK_SOAP_messageID` FOREIGN KEY (`messageID`) REFERENCES `main` (`messageID`)
);

--Grant Privilages
GRANT  DELETE, INSERT, SELECT, UPDATE ON  ip TO logs; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  main TO logs;
GRANT  DELETE, INSERT, SELECT, UPDATE ON  error TO logs; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  http TO logs; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  other TO logs; 
GRANT  DELETE, INSERT, SELECT, UPDATE ON  soap TO logs; 