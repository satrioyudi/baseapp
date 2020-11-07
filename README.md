# baseapp
Baseapp test

How to start service :
1. run "baseapp/mbs-common/buildAll.bat"
2. run "baseapp/mbs-java/buildAll.bat"
3. open cmd to folder "/baseapp/mbs-java/master-rest/" 
   and then run "java -jar target/master-rest-1.0.0.jar --spring.config.additional-location=../mbs-conf/local.properties --server.port={port}"
   
Postman
https://documenter.getpostman.com/view/13002820/TVeiCqcT

swagger
http://localhost:{port}/swagger-ui.html

DB person
CREATE TABLE `person` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) DEFAULT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `nik` varchar(50) NOT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `checkin_date` datetime DEFAULT NULL,
  `checkout_date` datetime DEFAULT NULL,
  `estimated_day` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1

DB book
CREATE TABLE `book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(30) DEFAULT NULL,
  `author` varchar(30) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1

DB person_book
CREATE TABLE `person_book` (
  `person_id` int(11) DEFAULT NULL,
  `book_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1