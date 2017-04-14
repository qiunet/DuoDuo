-- MySQL dump 10.13  Distrib 5.7.16, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: test_0
-- ------------------------------------------------------
-- Server version	5.7.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `test_0`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `test_0` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `test_0`;

--
-- Table structure for table `equip_a_0`
--

DROP TABLE IF EXISTS `equip_a_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `equip_a_0` (
  `id` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `level` int(11) NOT NULL DEFAULT '0',
  `exp` int(11) NOT NULL DEFAULT '0',
  UNIQUE KEY `index1` (`id`,`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equip_a_0`
--

LOCK TABLES `equip_a_0` WRITE;
/*!40000 ALTER TABLE `equip_a_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `equip_a_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equip_i_0`
--

DROP TABLE IF EXISTS `equip_i_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `equip_i_0` (
  `id` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `level` int(11) NOT NULL DEFAULT '0',
  `exp` int(11) NOT NULL DEFAULT '0',
  UNIQUE KEY `index1` (`id`,`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equip_i_0`
--

LOCK TABLES `equip_i_0` WRITE;
/*!40000 ALTER TABLE `equip_i_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `equip_i_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend`
--

DROP TABLE IF EXISTS `friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `friend` (
  `uid` int(11) NOT NULL,
  `friend_descs` varchar(2000) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend`
--

LOCK TABLES `friend` WRITE;
/*!40000 ALTER TABLE `friend` DISABLE KEYS */;
/*!40000 ALTER TABLE `friend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `login`
--

DROP TABLE IF EXISTS `login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `login` (
  `openid` varchar(255) NOT NULL,
  `uid` int(11) NOT NULL,
  `token` varchar(45) NOT NULL,
  PRIMARY KEY (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login`
--

LOCK TABLES `login` WRITE;
/*!40000 ALTER TABLE `login` DISABLE KEYS */;
/*!40000 ALTER TABLE `login` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player_a`
--

DROP TABLE IF EXISTS `player_a`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player_a` (
  `uid` int(11) NOT NULL,
  `exp` int(11) NOT NULL DEFAULT '0',
  `level` int(11) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_a`
--

LOCK TABLES `player_a` WRITE;
/*!40000 ALTER TABLE `player_a` DISABLE KEYS */;
/*!40000 ALTER TABLE `player_a` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player_i`
--

DROP TABLE IF EXISTS `player_i`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player_i` (
  `uid` int(11) NOT NULL,
  `exp` int(11) NOT NULL DEFAULT '0',
  `level` int(11) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_i`
--

LOCK TABLES `player_i` WRITE;
/*!40000 ALTER TABLE `player_i` DISABLE KEYS */;
/*!40000 ALTER TABLE `player_i` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qunxiu`
--

DROP TABLE IF EXISTS `qunxiu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qunxiu` (
  `id` int(11) NOT NULL,
  `master` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `level` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qunxiu`
--

LOCK TABLES `qunxiu` WRITE;
/*!40000 ALTER TABLE `qunxiu` DISABLE KEYS */;
/*!40000 ALTER TABLE `qunxiu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sysmsg_0`
--

DROP TABLE IF EXISTS `sysmsg_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sysmsg_0` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `msg` varchar(45) NOT NULL,
  `param` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`,`msg`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sysmsg_0`
--

LOCK TABLES `sysmsg_0` WRITE;
/*!40000 ALTER TABLE `sysmsg_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `sysmsg_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `test_1`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `test_1` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `test_1`;

--
-- Table structure for table `equip_a_0`
--

DROP TABLE IF EXISTS `equip_a_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `equip_a_0` (
  `id` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `level` int(11) NOT NULL DEFAULT '0',
  `exp` int(11) NOT NULL DEFAULT '0',
  UNIQUE KEY `index1` (`id`,`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equip_a_0`
--

LOCK TABLES `equip_a_0` WRITE;
/*!40000 ALTER TABLE `equip_a_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `equip_a_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equip_i_0`
--

DROP TABLE IF EXISTS `equip_i_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `equip_i_0` (
  `id` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `level` int(11) NOT NULL DEFAULT '0',
  `exp` int(11) NOT NULL DEFAULT '0',
  UNIQUE KEY `index1` (`id`,`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equip_i_0`
--

LOCK TABLES `equip_i_0` WRITE;
/*!40000 ALTER TABLE `equip_i_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `equip_i_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend`
--

DROP TABLE IF EXISTS `friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `friend` (
  `uid` int(11) NOT NULL,
  `friend_descs` varchar(2000) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend`
--

LOCK TABLES `friend` WRITE;
/*!40000 ALTER TABLE `friend` DISABLE KEYS */;
/*!40000 ALTER TABLE `friend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `login`
--

DROP TABLE IF EXISTS `login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `login` (
  `openid` varchar(255) NOT NULL,
  `uid` int(11) NOT NULL,
  `token` varchar(45) NOT NULL,
  PRIMARY KEY (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login`
--

LOCK TABLES `login` WRITE;
/*!40000 ALTER TABLE `login` DISABLE KEYS */;
/*!40000 ALTER TABLE `login` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player_a`
--

DROP TABLE IF EXISTS `player_a`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player_a` (
  `uid` int(11) NOT NULL,
  `exp` int(11) NOT NULL DEFAULT '0',
  `level` int(11) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_a`
--

LOCK TABLES `player_a` WRITE;
/*!40000 ALTER TABLE `player_a` DISABLE KEYS */;
/*!40000 ALTER TABLE `player_a` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player_i`
--

DROP TABLE IF EXISTS `player_i`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player_i` (
  `uid` int(11) NOT NULL,
  `exp` int(11) NOT NULL DEFAULT '0',
  `level` int(11) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_i`
--

LOCK TABLES `player_i` WRITE;
/*!40000 ALTER TABLE `player_i` DISABLE KEYS */;
/*!40000 ALTER TABLE `player_i` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qunxiu`
--

DROP TABLE IF EXISTS `qunxiu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qunxiu` (
  `id` int(11) NOT NULL,
  `master` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `level` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qunxiu`
--

LOCK TABLES `qunxiu` WRITE;
/*!40000 ALTER TABLE `qunxiu` DISABLE KEYS */;
/*!40000 ALTER TABLE `qunxiu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sysmsg_0`
--

DROP TABLE IF EXISTS `sysmsg_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sysmsg_0` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `msg` varchar(45) NOT NULL,
  `param` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`,`msg`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sysmsg_0`
--

LOCK TABLES `sysmsg_0` WRITE;
/*!40000 ALTER TABLE `sysmsg_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `sysmsg_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `test_global`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `test_global` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `test_global`;

--
-- Table structure for table `global_table`
--

DROP TABLE IF EXISTS `global_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `global_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `global_table`
--

LOCK TABLES `global_table` WRITE;
/*!40000 ALTER TABLE `global_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `global_table` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-02-28  3:05:59
