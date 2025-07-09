--
-- Table structure for table `iam_pps_associations`
--

DROP TABLE IF EXISTS `iam_pps_associations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iam_pps_associations` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `iamId` bigint(20) NOT NULL,
  `deptCode` varchar(10) NOT NULL,
  `deptOfficialName` varchar(64) NOT NULL,
  `deptDisplayName` varchar(64) DEFAULT NULL,
  `deptAbbrev` varchar(200) NOT NULL DEFAULT '',
  `isUCDHS` tinyint(1) NOT NULL,
  `bouOrgOId` varchar(32) DEFAULT NULL,
  `assocRank` varchar(2) NOT NULL DEFAULT '',
  `assocStartDate` datetime NOT NULL,
  `assocEndDate` datetime DEFAULT NULL,
  `titleCode` varchar(8) NOT NULL,
  `titleOfficialName` varchar(160) DEFAULT NULL,
  `titleDisplayName` varchar(128) NOT NULL,
  `positionTypeCode` varchar(1) NOT NULL,
  `positionType` varchar(32) NOT NULL,
  `percentFullTime` varchar(6) NOT NULL,
  `createDate` datetime NOT NULL,
  `modifyDate` datetime DEFAULT NULL,
  `updatedAt` timestamp NULL DEFAULT NULL,
  `createdAt` timestamp NULL DEFAULT NULL,
  `adminDeptCode` varchar(10) NOT NULL,
  `apptDeptCode` varchar(10) NOT NULL,
  `adminDeptOfficialName` varchar(64) NOT NULL,
  `adminDeptDisplayName` varchar(64) DEFAULT NULL,
  `adminDeptAbbrev` varchar(24) NOT NULL,
  `apptDeptOfficialName` varchar(64) NOT NULL,
  `apptDeptDisplayName` varchar(64) DEFAULT NULL,
  `apptDeptAbbrev` varchar(24) NOT NULL,
  `lastSeen` timestamp NULL DEFAULT NULL,
  `adminBouOrgOId` varchar(32) DEFAULT NULL,
  `apptBouOrgOId` varchar(32) DEFAULT NULL,
  `emplClass` varchar(2) DEFAULT NULL,
  `emplClassDesc` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_iamid` (`iamId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
  `levelCode` varchar(2) DEFAULT NULL,
  `levelName` varchar(32) DEFAULT NULL,
  `classCode` varchar(2) DEFAULT NULL,
  `className` varchar(32) DEFAULT NULL,
  `collegeCode` varchar(2) DEFAULT NULL,
  `collegeName` varchar(32) DEFAULT NULL,
  `assocRank` varchar(2) DEFAULT NULL,
  `assocStartDate` datetime DEFAULT NULL,
  `assocEndDate` datetime DEFAULT NULL,
  `majorCode` varchar(4) DEFAULT NULL,
  `majorName` varchar(32) DEFAULT NULL,
  `fepraCode` varchar(1) DEFAULT NULL,
  `createDate` datetime NOT NULL,
  `modifyDate` datetime DEFAULT NULL,
  `updatedAt` timestamp NULL DEFAULT NULL,
  `createdAt` timestamp NULL DEFAULT NULL,
  `lastSeen` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_iamid` (`iamId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `iam_contactinfo`
--

DROP TABLE IF EXISTS `iam_contactinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iam_contactinfo` (
  `iamId` bigint(20) NOT NULL,
  `email` varchar(48) DEFAULT NULL,
  `hsEmail` varchar(32) DEFAULT NULL,
  `campusEmail` varchar(32) DEFAULT NULL,
  `addrStreet` varchar(128) DEFAULT NULL,
  `addrCity` varchar(32) DEFAULT NULL,
  `addrState` varchar(2) DEFAULT NULL,
  `addrZip` varchar(10) DEFAULT NULL,
  `postalAddress` varchar(128) DEFAULT NULL,
  `workPhone` varchar(32) DEFAULT NULL,
  `workCell` varchar(16) DEFAULT NULL,
  `workPager` varchar(16) DEFAULT NULL,
  `workFax` varchar(16) DEFAULT NULL,
  `updatedAt` timestamp NULL DEFAULT NULL,
  `createdAt` timestamp NULL DEFAULT NULL,
  `lastSeen` timestamp NULL DEFAULT NULL,
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `idx_iamid` (`iamId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `iam_people`
--

DROP TABLE IF EXISTS `iam_people`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iam_people` (
  `iamId` bigint(20) NOT NULL,
  `mothraId` varchar(8) DEFAULT NULL,
  `ppsId` varchar(9) DEFAULT NULL,
  `studentId` varchar(32) DEFAULT NULL,
  `bannerPIdM` varchar(32) DEFAULT NULL,
  `externalId` varchar(32) DEFAULT NULL,
  `oFirstName` varchar(64) DEFAULT NULL,
  `oMiddleName` varchar(32) DEFAULT NULL,
  `oLastName` varchar(64) NOT NULL,
  `oFullName` varchar(128) NOT NULL,
  `oSuffix` varchar(20) DEFAULT NULL,
  `dFirstName` varchar(64) DEFAULT NULL,
  `dMiddleName` varchar(32) DEFAULT NULL,
  `dLastName` varchar(64) DEFAULT NULL,
  `dSuffix` varchar(20) DEFAULT NULL,
  `dFullName` varchar(128) DEFAULT NULL,
  `isEmployee` tinyint(1) NOT NULL,
  `isHSEmployee` tinyint(1) NOT NULL,
  `isFaculty` tinyint(1) NOT NULL,
  `isStudent` tinyint(1) NOT NULL,
  `isStaff` tinyint(1) NOT NULL,
  `isExternal` tinyint(1) NOT NULL,
  `privacyCode` varchar(16) DEFAULT NULL,
  `modifyDate` datetime NOT NULL,
  `UpdatedAt` timestamp NULL DEFAULT NULL,
  `CreatedAt` timestamp NULL DEFAULT NULL,
  `LastSeen` timestamp NULL DEFAULT NULL,
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `idx_iamid` (`iamId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `iam_pps_depts`
--

DROP TABLE IF EXISTS `iam_pps_depts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iam_pps_depts` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `orgOId` varchar(32) NOT NULL,
  `deptCode` varchar(10) NOT NULL,
  `deptOfficialName` varchar(64) NOT NULL,
  `deptDisplayName` varchar(64) DEFAULT NULL,
  `deptAbbrev` varchar(200) NOT NULL DEFAULT '',
  `isUCDHS` tinyint(1) NOT NULL,
  `bouOrgOId` varchar(32) DEFAULT NULL,
  `createDate` datetime NOT NULL,
  `modifyDate` datetime NOT NULL,
  `UpdatedAt` timestamp NULL DEFAULT NULL,
  `CreatedAt` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `iam_prikerbacct`
--

DROP TABLE IF EXISTS `iam_prikerbacct`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `iam_prikerbacct` (
  `iamId` bigint(20) NOT NULL,
  `userId` varchar(32) NOT NULL,
  `uuId` varchar(32) DEFAULT NULL,
  `createDate` datetime NOT NULL,
  `claimDate` datetime DEFAULT NULL,
  `expireDate` datetime DEFAULT NULL,
  `UpdatedAt` timestamp NULL DEFAULT NULL,
  `CreatedAt` timestamp NULL DEFAULT NULL,
  `LastSeen` timestamp NULL DEFAULT NULL,
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `idx_userid` (`userId`),
  KEY `idx_iamid` (`iamId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
  `deptDisplayName` varchar(32) DEFAULT '',
  `deptAbbrev` varchar(32) DEFAULT NULL,
  `isUCDHS` tinyint(1) NOT NULL,
  `createDate` datetime NOT NULL,
  `modifyDate` datetime DEFAULT NULL,
  `UpdatedAt` timestamp NULL DEFAULT NULL,
  `CreatedAt` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`orgOId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
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
