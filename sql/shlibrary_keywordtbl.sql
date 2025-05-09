-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: shlibrary
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `keywordtbl`
--

DROP TABLE IF EXISTS `keywordtbl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `keywordtbl` (
  `isbn` int NOT NULL,
  `keyword1` varchar(20) DEFAULT NULL,
  `keyword2` varchar(20) DEFAULT NULL,
  `keyword3` varchar(20) DEFAULT NULL,
  `keyword4` varchar(20) DEFAULT NULL,
  `keyword5` varchar(20) DEFAULT NULL,
  `keyword6` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`isbn`),
  CONSTRAINT `keywordtbl_ibfk_1` FOREIGN KEY (`isbn`) REFERENCES `booktbl` (`isbn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `keywordtbl`
--

LOCK TABLES `keywordtbl` WRITE;
/*!40000 ALTER TABLE `keywordtbl` DISABLE KEYS */;
INSERT INTO `keywordtbl` VALUES (1001,'건강 관리','다이어트',NULL,NULL,NULL,NULL),(1002,'중독','쾌락','고통','결핍',NULL,NULL),(1003,'근육 운동','기능 해부학',NULL,NULL,NULL,NULL),(1004,'베이킹',NULL,NULL,NULL,NULL,NULL),(1005,'임플란트','치과보철',NULL,NULL,NULL,NULL),(1006,'노화','가속노화','노쇠생리학',NULL,NULL,NULL),(1007,'정서신경과학','뇌과학입문','복잡계네트워크','신경과학자',NULL,NULL),(1008,'전기','전기 자기학',NULL,NULL,NULL,NULL),(1009,'건축,',NULL,NULL,NULL,NULL,NULL),(1010,'뇌과학',NULL,NULL,NULL,NULL,NULL),(1011,'게임','학습만화',NULL,NULL,NULL,NULL),(1012,'명화',NULL,NULL,NULL,NULL,NULL),(1013,'미술','미술 감상',NULL,NULL,NULL,NULL),(1014,'예술','글모음',NULL,NULL,NULL,NULL),(1015,'수기',NULL,NULL,NULL,NULL,NULL),(1016,'영국 화가','회화',NULL,NULL,NULL,NULL),(1017,'목각',NULL,NULL,NULL,NULL,NULL),(1018,'영화 평론','영화 비평',NULL,NULL,NULL,NULL),(1019,'미술',NULL,NULL,NULL,NULL,NULL),(1020,'클래식','고전 음악','해설서',NULL,NULL,NULL),(1021,'영어 학습',NULL,NULL,NULL,NULL,NULL),(1022,'영어 회화',NULL,NULL,NULL,NULL,NULL),(1023,'한글 맞춤법',NULL,NULL,NULL,NULL,NULL),(1024,'한글 맞춤법',NULL,NULL,NULL,NULL,NULL),(1025,'초등 국어','한국어 학습','만화',NULL,NULL,NULL),(1026,'영어','영어회화','패턴','반복',NULL,NULL),(1027,'일본어 회화',NULL,NULL,NULL,NULL,NULL),(1028,'한자',NULL,NULL,NULL,NULL,NULL),(1029,'한자',NULL,NULL,NULL,NULL,NULL),(1030,'감정','감정 표현',NULL,NULL,NULL,NULL),(1031,'한국현대소설','한국장편소설','제주 4.3 사건',NULL,NULL,NULL),(1032,'한국 현대 소설',NULL,NULL,NULL,NULL,NULL),(1033,'산문집','에세이','기행 문학','한국 현대 문학',NULL,NULL),(1034,'한국 현대 소설','장편소설',NULL,NULL,NULL,NULL),(1035,'영미소설','도서관','자정','우울증','후회','영국 현대 소설'),(1036,'한국 현대 소설','가족 소설',NULL,NULL,NULL,NULL),(1037,'한국 현대 소설',NULL,NULL,NULL,NULL,NULL),(1038,'한국 현대 소설',NULL,NULL,NULL,NULL,NULL),(1039,'한국 현대 소설','과학 소설',NULL,NULL,NULL,NULL),(1040,'창작 동화',NULL,NULL,NULL,NULL,NULL),(1041,'기행 문학','한국 현대 문학',NULL,NULL,NULL,NULL),(1042,'한국사',NULL,NULL,NULL,NULL,NULL),(1043,'일본 여행','해외 여행 안내',NULL,NULL,NULL,NULL),(1044,'튀르키예','터키','여행안내','Turkey',NULL,NULL),(1045,'세계여행','해외여행','세계사','박물관','학습만화',NULL),(1046,'세계여행','해외여행','세계사','박물관','학습만화',NULL),(1047,'세계사','아동만화','학습만화',NULL,NULL,NULL),(1048,'세계사',NULL,NULL,NULL,NULL,NULL),(1049,'해외 여행 안내','호텔','도쿄',NULL,NULL,NULL),(1050,'Musk Elon','전기','우주 공학',NULL,NULL,NULL),(2001,'독서법','자기계발',NULL,NULL,NULL,NULL),(2002,'지식','인문 과학',NULL,NULL,NULL,NULL),(2003,'어플리케이션','소프트웨어',NULL,NULL,NULL,NULL),(2004,'랭체인','LLM','AI','RAG',NULL,NULL),(2005,'편집','편집자','편집 작업',NULL,NULL,NULL),(2006,'챗GPT','프롬트',NULL,NULL,NULL,NULL),(2007,'젠킨스','소프트웨어 개발','시스템 관리',NULL,NULL,NULL),(2008,'챗GPT','데이터 리터러시','AI 데이터 분석',NULL,NULL,NULL),(2009,'인공지능','AI','ChatGPT','챗GPT',NULL,NULL),(2010,'AI','프로그래밍','LLM',NULL,NULL,NULL),(2021,'철학','서양철학자',NULL,NULL,NULL,NULL),(2022,'명리학','사주 명리학',NULL,NULL,NULL,NULL),(2023,'감정','느낌',NULL,NULL,NULL,NULL),(2024,'인생훈','자기관리',NULL,NULL,NULL,NULL),(2025,'철학','사유',NULL,NULL,NULL,NULL),(2026,'완벽주의','심리학',NULL,NULL,NULL,NULL),(2027,'내면','소통',NULL,NULL,NULL,NULL),(2028,'우주론','존재론',NULL,NULL,NULL,NULL),(2029,'집중력',NULL,NULL,NULL,NULL,NULL),(2030,'성공','처세','자산가',NULL,NULL,NULL),(2031,'승려','수기',NULL,NULL,NULL,NULL),(2032,'인문학','종교','불교','명언',NULL,NULL),(2033,'그리스 로마 신화',NULL,NULL,NULL,NULL,NULL),(2034,'그리스 로마 신화',NULL,NULL,NULL,NULL,NULL),(2035,'종교','불교',NULL,NULL,NULL,NULL),(2036,'성서',NULL,NULL,NULL,NULL,NULL),(2037,'성서 연구','신약 성서',NULL,NULL,NULL,NULL),(2038,'그리스 로마 신화',NULL,NULL,NULL,NULL,NULL),(2039,'성서 주석',NULL,NULL,NULL,NULL,NULL),(2040,'복음','성서 연구','Jesus Christ',NULL,NULL,NULL),(2043,'공직사회','공무원',NULL,NULL,NULL,NULL),(2044,'재테크','자산 관리',NULL,NULL,NULL,NULL),(2045,'국제 정치','정치 지리학',NULL,NULL,NULL,NULL),(2046,'학습','교육','대담',NULL,NULL,NULL),(2047,'사재정','재테크','투자 가이드',NULL,NULL,NULL),(2048,'국제 경제','국제 정치 경제',NULL,NULL,NULL,NULL),(2049,'사회적 불평등','공정','능력주의',NULL,NULL,NULL),(2050,'투자','재테크','돈',NULL,NULL,NULL),(2051,'자산 관리','부자',NULL,NULL,NULL,NULL),(2052,'경제경영','투자','유튜버',NULL,NULL,NULL),(2053,'유전자','동물 행동학',NULL,NULL,NULL,NULL),(2054,'코스모스','우주',NULL,NULL,NULL,NULL),(2055,'과학자','자기성찰','논픽션',NULL,NULL,NULL),(2056,'과학',NULL,NULL,NULL,NULL,NULL),(2057,'과학','만화',NULL,NULL,NULL,NULL),(2058,'양자역학',NULL,NULL,NULL,NULL,NULL),(2059,'과학','만화',NULL,NULL,NULL,NULL),(2060,'진화인류학','공감',NULL,NULL,NULL,NULL),(2061,'로봇세계','살아남기','서바이벌','만화','과학양식',NULL),(2062,'과학',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `keywordtbl` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-09 11:13:38
