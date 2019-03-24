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
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table login
# ------------------------------------------------------------

DROP TABLE IF EXISTS `login`;

CREATE TABLE `login` (
  `openid` varchar(255) NOT NULL,
  `uid` int(11) NOT NULL,
  `token` varchar(45) NOT NULL,
  PRIMARY KEY (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table player
# ------------------------------------------------------------

DROP TABLE IF EXISTS `player`;

CREATE TABLE `player` (
  `uid` int(11) NOT NULL,
  `exp` int(11) NOT NULL DEFAULT 0,
  `level` int(11) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table qunxiu
# ------------------------------------------------------------

DROP TABLE IF EXISTS `qunxiu`;

CREATE TABLE `qunxiu` (
  `id` int(11) NOT NULL,
  `master` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `level` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sysmsg_0
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sysmsg_0`;

CREATE TABLE `sysmsg_0` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `msg` varchar(45) NOT NULL,
  `param` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`,`msg`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- Current Database: `test_1`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `test_1` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `test_1`;

--
-- Table structure for table `equip_a_0`
--
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table login
# ------------------------------------------------------------

DROP TABLE IF EXISTS `login`;

CREATE TABLE `login` (
  `openid` varchar(255) NOT NULL,
  `uid` int(11) NOT NULL,
  `token` varchar(45) NOT NULL,
  PRIMARY KEY (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table player
# ------------------------------------------------------------

DROP TABLE IF EXISTS `player`;

CREATE TABLE `player` (
  `uid` int(11) NOT NULL,
  `exp` int(11) NOT NULL DEFAULT 0,
  `level` int(11) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table qunxiu
# ------------------------------------------------------------

DROP TABLE IF EXISTS `qunxiu`;

CREATE TABLE `qunxiu` (
  `id` int(11) NOT NULL,
  `master` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `level` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table sysmsg_0
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sysmsg_0`;

CREATE TABLE `sysmsg_0` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `msg` varchar(45) NOT NULL,
  `param` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`,`msg`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



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
