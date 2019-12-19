--
-- Table structure for table `iam_pps_associations`
--

DROP TABLE IF EXISTS `iam_pps_associations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iam_pps_associations` (
  `deptCode` varchar(10) NOT NULL,
  `deptOfficialName` varchar(64) NOT NULL,
  `deptDisplayName` varchar(64) NULL,
  `deptAbbrev` varchar(24) NOT NULL,
  `isUCDHS` tinyint(1) NOT NULL,
  `bouOrgOId` varchar(32) NULL,
  `assocRank` varchar(1) NOT NULL,
  `assocStartDate` datetime NOT NULL,
  `assocEndDate` datetime NULL,
  `titleCode` varchar(8) NOT NULL,
  `positionTypeCode` varchar(1) NOT NULL,
  `createDate` datetime NOT NULL,
  `modifyDate` datetime NULL,
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `iamId` bigint(20) NOT NULL,
  `percentFullTime` varchar(6) NOT NULL,
  `titleDisplayName` varchar(128) NOT NULL,
  `positionType` varchar(32) NOT NULL,
  `titleOfficialName` varchar(160) NULL,
  `adminBouOrgOId` varchar(32) NULL,
  `adminDeptAbbrev` varchar(24) NOT NULL,
  `adminDeptCode` varchar(10) NOT NULL,
  `adminDeptDisplayName` varchar(64) NULL,
  `adminDeptOfficialName` varchar(64) NOT NULL,
  `apptBouOrgOId` varchar(32) NULL,
  `apptDeptAbbrev` varchar(24) NOT NULL,
  `apptDeptCode` varchar(10) NOT NULL,
  `apptDeptDisplayName` varchar(64) NULL,
  `apptDeptOfficialName` varchar(64) NOT NULL,
  `createdAt` timestamp NULL,
  `updatedAt` timestamp NULL,
  `lastSeen` timestamp NULL,
  `emplClass` varchar(2) NULL,
  -- emplClassDesc longest seen was 29 as of 12/19/19, setting higher for safety
  `emplClassDesc` varchar(64) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `iam_sis_associations`
--

DROP TABLE IF EXISTS `iam_sis_associations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iam_sis_associations` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `iamId` bigint(20) NOT NULL,
  `levelCode` varchar(2) NULL,
  `levelName` varchar(32) NULL,
  `classCode` varchar(2) NULL,
  `className` varchar(32) NULL,
  `collegeCode` varchar(2) NULL,
  `collegeName` varchar(32) NULL,
  `assocRank` varchar(1) NULL,
  `assocStartDate` datetime NULL,
  `assocEndDate` datetime NULL,
  `majorCode` varchar(4) NULL,
  `majorName` varchar(32) NULL,
  `fepraCode` varchar(1) NULL,
  `createDate` datetime NOT NULL,
  `modifyDate` datetime NULL,
  `createdAt` timestamp NULL,
  `updatedAt` timestamp NULL,
  `lastSeen` timestamp NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `iam_contactinfo`
--

DROP TABLE IF EXISTS `iam_contactinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iam_contactinfo` (
  `hsEmail` varchar(32) DEFAULT NULL,
  `campusEmail` varchar(32) DEFAULT NULL,
  `workCell` varchar(16) DEFAULT NULL,
  `workPager` varchar(16) DEFAULT NULL,
  `workFax` varchar(16) DEFAULT NULL,
  `iamId` bigint(20) NOT NULL,
  `workPhone` varchar(32) DEFAULT NULL,
  `email` varchar(48) DEFAULT NULL,
  `addrCity` varchar(32) DEFAULT NULL,
  `addrState` varchar(2) DEFAULT NULL,
  `addrZip` varchar(10) DEFAULT NULL,
  `postalAddress` varchar(96) DEFAULT NULL,
  `addrStreet` varchar(128) DEFAULT NULL,
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `createdAt` timestamp NULL,
  `updatedAt` timestamp NULL,
  `lastSeen` timestamp NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `iam_people`
--

DROP TABLE IF EXISTS `iam_people`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iam_people` (
  `studentId` varchar(32) DEFAULT NULL,
  `bannerPIdM` varchar(32) DEFAULT NULL,
  `externalId` varchar(32) DEFAULT NULL,
  `oFirstName` varchar(64) NULL,
  `oLastName` varchar(64) NOT NULL,
  `oFullName` varchar(128) NOT NULL,
  `oSuffix` varchar(9) DEFAULT NULL,
  `isEmployee` tinyint(1) NOT NULL,
  `isHSEmployee` tinyint(1) NOT NULL,
  `isFaculty` tinyint(1) NOT NULL,
  `isStudent` tinyint(1) NOT NULL,
  `isStaff` tinyint(1) NOT NULL,
  `isExternal` tinyint(1) NOT NULL,
  `privacyCode` varchar(16) DEFAULT NULL,
  `modifyDate` datetime NOT NULL,
  `iamId` bigint(20) NOT NULL,
  `oMiddleName` varchar(32) DEFAULT NULL,
  `dFirstName` varchar(32) DEFAULT NULL,
  `dMiddleName` varchar(32) DEFAULT NULL,
  `dLastName` varchar(32) DEFAULT NULL,
  `dSuffix` varchar(9) DEFAULT NULL,
  `dFullName` varchar(64) DEFAULT NULL,
  `ppsId` varchar(9) DEFAULT NULL,
  `mothraId` varchar(8) DEFAULT NULL,
  `createdAt` timestamp NULL,
  `updatedAt` timestamp NULL,
  `lastSeen` timestamp NULL,
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  -- FULLTEXT KEY `search_key` (`oFirstName`, `oMiddleName`, `oLastName`, `oFullName`, `dFirstName`, `dMiddleName`, `dLastName`, `dFullName`)
  FULLTEXT KEY `search_key` (`oFullName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `iam_pps_depts`
--

DROP TABLE IF EXISTS `iam_pps_depts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iam_pps_depts` (
  `orgOId` varchar(32) NOT NULL,
  `deptCode` varchar(10) NOT NULL,
  `deptOfficialName` varchar(64) NOT NULL,
  `deptDisplayName` varchar(64) NULL,
  `deptAbbrev` varchar(24) NOT NULL,
  `isUCDHS` tinyint(1) NOT NULL,
  `bouOrgOId` varchar(32) NULL,
  `createDate` datetime NOT NULL,
  `modifyDate` datetime NOT NULL,
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `createdAt` timestamp NULL,
  `updatedAt` timestamp NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `iam_prikerbacct`
--

DROP TABLE IF EXISTS `iam_prikerbacct`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iam_prikerbacct` (
  `userId` varchar(32) NOT NULL,
  `uuId` varchar(32) NOT NULL,
  `createDate` datetime NOT NULL,
  `claimDate` datetime DEFAULT NULL,
  `expireDate` datetime DEFAULT NULL,
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `iamId` bigint(20) NOT NULL,
  `createdAt` timestamp NULL,
  `updatedAt` timestamp NULL,
  `lastSeen` timestamp NULL,
  PRIMARY KEY (`id`),
  KEY `idx_iamid` (`iamId`),
  INDEX `iam_p_iam` (`iamId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `iam_bous`
--

DROP TABLE IF EXISTS `iam_bous`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iam_bous` (
  `orgOId` varchar(32) NOT NULL,
  `deptCode` varchar(6) NOT NULL,
  `deptOfficialName` varchar(64) NOT NULL,
  `deptDisplayName` varchar(32) NOT NULL,
  `deptAbbrev` varchar(32) NOT NULL,
  `isUCDHS` boolean NOT NULL,
  `createDate` datetime NOT NULL,
  `modifyDate` datetime NULL,
  `createdAt` timestamp NULL,
  `updatedAt` timestamp NULL,
  PRIMARY KEY (`orgOId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `statuses`
--

DROP TABLE IF EXISTS `statuses`;
CREATE TABLE `statuses` (
  `upstream_db` varchar(32) NOT NULL,
  `last_attempt` datetime DEFAULT NULL,
  `last_success` datetime DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  PRIMARY KEY (`upstream_db`),
  UNIQUE KEY `upstream_db_UNIQUE` (`upstream_db`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
