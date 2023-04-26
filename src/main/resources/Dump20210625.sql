-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: localhost    Database: dynamic_formsv2
-- ------------------------------------------------------
-- Server version	8.0.23

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `organisation_vertical_location_page`
--

DROP TABLE IF EXISTS `dynamic_formsv2`.`organisation_vertical_location_page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dynamic_formsv2`.`organisation_vertical_location_page` (
  `id` int NOT NULL AUTO_INCREMENT,
  `UUID` varchar(50) DEFAULT NULL,
  `page_id` int DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `no_of_columns` int DEFAULT NULL,
  `pojo` varchar(100) DEFAULT NULL,
  `page_json` json DEFAULT NULL,
  `end_point` varchar(100) DEFAULT NULL,
  `created_at` date DEFAULT NULL,
  `updated_at` date DEFAULT NULL,
  `position` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organisation_vertical_location_page`
--

LOCK TABLES `dynamic_formsv2`.`organisation_vertical_location_page` WRITE;
/*!40000 ALTER TABLE `organisation_vertical_location_page` DISABLE KEYS */;
INSERT INTO `dynamic_formsv2`.`organisation_vertical_location_page` VALUES (1,'UUID-1',1,'Enquirey Form',3,'',NULL,NULL,'2021-06-23','2021-06-23',NULL),(2,'UUID-2',2,'Organization',2,NULL,NULL,NULL,'2021-06-23','2021-06-23',NULL);
/*!40000 ALTER TABLE `organisation_vertical_location_page` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organisation_vertical_location_page_group`
--

DROP TABLE IF EXISTS `dynamic_formsv2`.`organisation_vertical_location_page_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dynamic_formsv2`.`organisation_vertical_location_page_group` (
  `id` int NOT NULL AUTO_INCREMENT,
  `org_ver_loc_page_id` int DEFAULT NULL,
  `group` int DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `min_items` int DEFAULT NULL,
  `max_items` int DEFAULT NULL,
  `icon_cls` varchar(100) DEFAULT NULL,
  `no_of_columns` int DEFAULT NULL,
  `created_at` date DEFAULT NULL,
  `updated_at` date DEFAULT NULL,
  `position` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `org_ver_loc_page_id` (`org_ver_loc_page_id`),
  CONSTRAINT `org_ver_loc_page_groupfk_1` FOREIGN KEY (`org_ver_loc_page_id`) REFERENCES `dynamic_formsv2`.`organisation_vertical_location_page` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organisation_vertical_location_page_group`
--

LOCK TABLES `dynamic_formsv2`.`organisation_vertical_location_page_group` WRITE;
/*!40000 ALTER TABLE `organisation_vertical_location_page_group` DISABLE KEYS */;
INSERT INTO `dynamic_formsv2`.`organisation_vertical_location_page_group` VALUES (1,1,1,'Customer Details',1,200,'icon class',2,'2021-06-23','2021-06-23',NULL),(2,1,2,'Vechile Details',1,200,'icon class1',2,'2021-06-23','2021-06-23',NULL);
/*!40000 ALTER TABLE `organisation_vertical_location_page_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organisation_vertical_location_page_group_field`
--

DROP TABLE IF EXISTS `dynamic_formsv2`.`organisation_vertical_location_page_group_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dynamic_formsv2`.`organisation_vertical_location_page_group_field` (
  `id` int NOT NULL AUTO_INCREMENT,
  `org_acc_loc_page_group_id` int DEFAULT NULL,
  `field_id` int DEFAULT NULL,
  `identifier` varchar(100) DEFAULT NULL,
  `depends_on` varchar(100) DEFAULT NULL,
  `dom_type` varchar(100) DEFAULT NULL,
  `dom_input_type` varchar(100) DEFAULT NULL,
  `dt_static` varchar(1) DEFAULT NULL,
  `dt_choices` varchar(200) DEFAULT NULL,
  `dt_url` varchar(200) DEFAULT NULL,
  `dt_attributes` varchar(300) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `label` varchar(100) DEFAULT NULL,
  `css` varchar(100) DEFAULT NULL,
  `tool_tip` varchar(100) DEFAULT NULL,
  `default_text` varchar(100) DEFAULT NULL,
  `field_def_attributes` varchar(100) DEFAULT NULL,
  `required` tinyint(1) DEFAULT NULL,
  `required_validation_msg` varchar(200) DEFAULT NULL,
  `min_length` int DEFAULT NULL,
  `max_length` int DEFAULT NULL,
  `length_validation_msg` varchar(200) DEFAULT NULL,
  `reg_expression` varchar(200) DEFAULT NULL,
  `valid_js_func` varchar(100) DEFAULT NULL,
  `valid_js_func_msg_key` varchar(100) DEFAULT NULL,
  `valid_js_func_msg` varchar(100) DEFAULT NULL,
  `valid_func_on` varchar(100) DEFAULT NULL,
  `created_at` date DEFAULT NULL,
  `updated_at` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `org_acc_loc_page_group_id` (`org_acc_loc_page_group_id`),
  CONSTRAINT `org_acc_loc_page_group_field_fk_1` FOREIGN KEY (`org_acc_loc_page_group_id`) REFERENCES `dynamic_formsv2`.`organisation_vertical_location_page_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organisation_vertical_location_page_group_field`
--

LOCK TABLES `dynamic_formsv2`.`organisation_vertical_location_page_group_field` WRITE;
/*!40000 ALTER TABLE `organisation_vertical_location_page_group_field` DISABLE KEYS */;
INSERT INTO `dynamic_formsv2`.`organisation_vertical_location_page_group_field` VALUES (1,1,1,'dlChallanNo',NULL,'dropdown',NULL,'y','[{\"id\": \"1\", \"label\": \"1234\"}, {}, {}, {}]',NULL,'{ \"type\" : \"number\", \"edit\" : \"true\" }','dlChallanNo','dlChallanNo',NULL,'dlChallanNo','dlChallanNo',NULL,1,'Plese enter text  in between 5 to 10 characters',1,10,NULL,NULL,NULL,NULL,NULL,'on_key_up','2021-06-23','2021-06-23'),(2,1,1,'dlChallanDate',NULL,'radiobutton',NULL,'y','[{\"id\": \"1\", \"label\": \"1234\"}, {}, {}, {}]',NULL,'{ \"type\" : \"number\", \"edit\" : \"true\" }','dlChallanNo','dlChallanNo',NULL,'dlChallanNo','dlChallanNo',NULL,1,'Plese enter text  in between 5 to 10 characters',1,10,NULL,NULL,NULL,NULL,NULL,'on_key_up','2021-06-23','2021-06-23'),(3,1,1,'mobile_number',NULL,'input_text_field',NULL,'y','[{\"id\": \"1\", \"label\": \"1234\"}, {}, {}, {}]',NULL,'{ \"type\" : \"number\", \"edit\" : \"true\" }','dlChallanNo','dlChallanNo',NULL,'dlChallanNo','dlChallanNo',NULL,1,'Plese enter text  in between 5 to 10 characters',1,10,NULL,NULL,NULL,NULL,NULL,'on_key_up','2021-06-23','2021-06-23');
/*!40000 ALTER TABLE `organisation_vertical_location_page_group_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organisation_vertical_location_role`
--

DROP TABLE IF EXISTS `dynamic_formsv2`.`organisation_vertical_location_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dynamic_formsv2`.`organisation_vertical_location_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `UUID` varchar(50) DEFAULT NULL,
  `role_id` int DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organisation_vertical_location_role`
--

LOCK TABLES `dynamic_formsv2`.`organisation_vertical_location_role` WRITE;
/*!40000 ALTER TABLE `organisation_vertical_location_role` DISABLE KEYS */;
INSERT INTO `dynamic_formsv2`.`organisation_vertical_location_role` VALUES (1,'UUID - 1',1,'Manager','2021-06-23 00:00:00','2021-06-23 00:00:00'),(2,'UUID - 1',2,'Sales Executive','2021-06-23 00:00:00','2021-06-23 00:00:00');
/*!40000 ALTER TABLE `organisation_vertical_location_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organisation_vertical_location_role_menu`
--

DROP TABLE IF EXISTS `dynamic_formsv2`.`organisation_vertical_location_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dynamic_formsv2`.`organisation_vertical_location_role_menu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `org_ver_loc_role_id` int DEFAULT NULL,
  `menu_id` int DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `org_ver_loc_role_id` (`org_ver_loc_role_id`),
  CONSTRAINT `org_ver_loc_role_menufk_1` FOREIGN KEY (`org_ver_loc_role_id`) REFERENCES `dynamic_formsv2`.`organisation_vertical_location_role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organisation_vertical_location_role_menu`
--

LOCK TABLES `dynamic_formsv2`.`organisation_vertical_location_role_menu` WRITE;
/*!40000 ALTER TABLE `organisation_vertical_location_role_menu` DISABLE KEYS */;
INSERT INTO `dynamic_formsv2`.`organisation_vertical_location_role_menu` VALUES (1,1,1,'2021-06-23 00:00:00','2021-06-23 00:00:00'),(2,1,2,'2021-06-23 00:00:00','2021-06-23 00:00:00'),(3,2,2,'2021-06-23 00:00:00','2021-06-23 00:00:00'),(4,2,3,'2021-06-23 00:00:00','2021-06-23 00:00:00');
/*!40000 ALTER TABLE `organisation_vertical_location_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `page_group_field_role_access`
--

DROP TABLE IF EXISTS `dynamic_formsv2`.`page_group_field_role_access`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dynamic_formsv2`.`page_group_field_role_access` (
  `id` int NOT NULL AUTO_INCREMENT,
  `org_ver_loc_page_id` int DEFAULT NULL,
  `org_ver_loc_page_group_id` int DEFAULT NULL,
  `org_ver_loc_page_group_field_id` int DEFAULT NULL,
  `org_ver_loc_role_id` int DEFAULT NULL,
  `permission_type` varchar(10) DEFAULT NULL,
  `menu_id` int DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `org_ver_loc_page_id` (`org_ver_loc_page_id`),
  KEY `org_ver_loc_page_group_id` (`org_ver_loc_page_group_id`),
  KEY `org_ver_loc_page_group_field_id` (`org_ver_loc_page_group_field_id`),
  KEY `org_ver_loc_role_id` (`org_ver_loc_role_id`),
  CONSTRAINT `page_group_field_role_access_fk_1` FOREIGN KEY (`org_ver_loc_page_id`) REFERENCES `dynamic_formsv2`.`organisation_vertical_location_page` (`id`),
  CONSTRAINT `page_group_field_role_access_fk_2` FOREIGN KEY (`org_ver_loc_page_group_id`) REFERENCES `dynamic_formsv2`.`organisation_vertical_location_page_group` (`id`),
  CONSTRAINT `page_group_field_role_access_fk_3` FOREIGN KEY (`org_ver_loc_page_group_field_id`) REFERENCES `dynamic_formsv2`.`organisation_vertical_location_page_group_field` (`id`),
  CONSTRAINT `page_group_field_role_access_fk_4` FOREIGN KEY (`org_ver_loc_role_id`) REFERENCES `dynamic_formsv2`.`organisation_vertical_location_role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `page_group_field_role_access`
--

LOCK TABLES `dynamic_formsv2`.`page_group_field_role_access` WRITE;
/*!40000 ALTER TABLE `page_group_field_role_access` DISABLE KEYS */;
INSERT INTO `dynamic_formsv2`.`page_group_field_role_access` VALUES (1,1,1,1,1,'Edit',1,NULL,NULL),(2,1,1,2,2,'View',1,NULL,NULL),(3,1,1,3,1,'Edit',1,NULL,NULL);
/*!40000 ALTER TABLE `page_group_field_role_access` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-25 11:58:04




CREATE TABLE INTER_STATE_TAX(
id int NOT NULL AUTO_INCREMENT,
state varchar2(100),
model varchar2(100),
variant varchar2(100),
engine_cc varchar2(10),
fuel varchar2(50),
unit varchar2(50),
igst int,
cess int,
total int,
PRIMARY KEY (id)
);


CREATE TABLE UNION_TERRITORY_TAX(
id int NOT NULL AUTO_INCREMENT,
state varchar2(100),
model varchar2(100),
variant varchar2(100),
engine_cc varchar2(10),
fuel varchar2(50),
unit varchar2(50),
utgst int,
cess int,
total int,
PRIMARY KEY (id)
);


CREATE TABLE `dynamic_formsv3`.`organization` (
  `id` int NOT NULL AUTO_INCREMENT,
  `orgname` varchar(50) DEFAULT NULL,
  `orgdesc` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `dynamic_formsv3`.`address` (
  `id` int NOT NULL AUTO_INCREMENT,
  `org_id` int DEFAULT NULL,
  `orgname` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `org_id` (`org_id`),
  CONSTRAINT `address_ibfk_1` FOREIGN KEY (`org_id`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `dynamic_formsv3`.`contacts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `address_id` int DEFAULT NULL,
  `contact_no` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `contacts_ibfk_1` (`address_id`),
  CONSTRAINT `contacts_ibfk_1` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `target_setting` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_role_id` varchar(45) DEFAULT NULL,
  `branch` varchar(45) DEFAULT NULL,
  `location` varchar(45) DEFAULT NULL,
  `department` varchar(45) DEFAULT NULL,
  `designation` varchar(45) DEFAULT NULL,
  `experience` varchar(45) DEFAULT NULL,
  `targets` longtext,
  `salary_range` varchar(45) DEFAULT NULL,
  `retail_target` int DEFAULT NULL,
  `enquiry` int DEFAULT NULL,
  `start_date` varchar(45) DEFAULT NULL,
  `end_date` varchar(45) DEFAULT NULL,
  `emp_name` varchar(45) DEFAULT NULL,
  `emp_id` varchar(45) DEFAULT NULL,
  `team_lead` varchar(45) DEFAULT NULL,
  `manager` varchar(45) DEFAULT NULL,
  `branch_manager` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
