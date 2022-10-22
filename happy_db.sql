-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: localhost    Database: happy_db
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+08:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

create database happy_db;
use happy_db;

--
-- Table structure for table `t_judge_experiment`
--

DROP TABLE IF EXISTS `t_judge_experiment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_judge_experiment` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `num_value` int NOT NULL COMMENT '数字值',
  `str_value` varchar(45) NOT NULL COMMENT '字符串值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='dao中参数判断的实验';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_multithread_sync`
--

DROP TABLE IF EXISTS `t_multithread_sync`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_multithread_sync` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `value_gen` int DEFAULT NULL COMMENT '测试的不断加1的值',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='synchronized和transational并发安全测试表';
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `mp`;
CREATE TABLE `mp` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `num_value` INT COMMENT 'int类型的数值',
  `str_value` VARCHAR(45) COMMENT '数字',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
COMMENT = 'MybatisPlus Test Table';

--
-- Table structure for table `t_stat_class`
--

DROP TABLE IF EXISTS `t_stat_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_stat_class` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '班级名称',
  `cre_time` datetime NOT NULL COMMENT '创建时间',
  `upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='统计测试用的班级表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_stat_student`
--

DROP TABLE IF EXISTS `t_stat_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_stat_student` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '学生姓名，可能重复',
  `status` tinyint NOT NULL COMMENT '学生的状态',
  `class_id` int unsigned NOT NULL COMMENT '班级id',
  `cre_time` datetime NOT NULL COMMENT '创建时间',
  `upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=201 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='统计测试用学生表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_stat_subject_score`
--

DROP TABLE IF EXISTS `t_stat_subject_score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_stat_subject_score` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `score` double NOT NULL COMMENT '分数',
  `stu_id` int unsigned NOT NULL COMMENT '学生ID',
  `subject_type` tinyint unsigned NOT NULL COMMENT '学科类型',
  `cre_time` datetime NOT NULL COMMENT '创建时间',
  `upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=601 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='统计测试用的学科成绩表';

--
-- Dumping data for table `t_multithread_sync`
--

LOCK TABLES `t_multithread_sync` WRITE;
/*!40000 ALTER TABLE `t_multithread_sync` DISABLE KEYS */;
INSERT INTO `t_multithread_sync` VALUES (1,300,'Transactional + synchronized + sql'),(2,600,'synchronized + sql'),(3,351,'Transactional + synchronized + mybatisPlus'),(4,600,'Transactional+ synchronized + no select + sql');
/*!40000 ALTER TABLE `t_multithread_sync` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-01-29 22:32:13
