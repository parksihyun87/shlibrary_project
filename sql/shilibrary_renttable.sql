
desc rent;
drop table if exists rent;
create table rent(
number int auto_increment primary key,
personid varchar(10) not null,
bookid int not null,
rentdate date not null,
duedate date not null,
prolong boolean not null,
overduedate int not null,
turnin boolean not null);

-- 대출 가능 권수 신입:2/일반:3/우수:5/모범:7/(장기연체 1권)
-- 현재 cccc 1일 연체중 (두권)

insert into rent values
(null, 'aaaa', 1000, '2025-05-03','2025-05-16', false, 0, false),
(null, 'aaaa', 1001, '2025-05-03','2025-05-16', false, 0, false);
-- id: 'aaaa'의 2건의 대출이력/신입

insert into rent values
(null, 'bbbb', 1010, '2025-05-03','2025-05-16', false, 0, false),
(null, 'bbbb', 1011, '2025-05-03','2025-05-16', false, 0, false),
(null, 'bbbb', 1012, '2025-05-03','2025-05-16', false, 0, false),
(null, 'bbbb', 1013, '2025-05-03','2025-05-16', false, 0, false),
(null, 'bbbb', 1014, '2025-05-03','2025-05-16', false, 0, false);

-- id: 'bbbb'의 5건의 대출이력/우수

insert into rent values
(null, 'cccc', 1020, '2025-04-18','2025-05-01', false, 1, false),
(null, 'cccc', 1021, '2025-04-18','2025-05-01', false, 1, false);

-- id: 연체자 'cccc'의 2건의 대출이력/신입(현재 연체중)

insert into rent values
(null, 'dddd', 1030, '2025-05-03','2025-05-16', false, 0, false),
(null, 'dddd', 1031, '2025-05-03','2025-05-16', false, 0, false),
(null, 'dddd', 1032, '2025-05-03','2025-05-16', false, 0, false),
(null, 'dddd', 1033, '2025-05-03','2025-05-16', false, 0, false),
(null, 'dddd', 1034, '2025-05-03','2025-05-16', false, 0, false);
-- id: 'dddd'의 5건의 대출이력/우수

insert into rent values
(null, 'eeee', 1040, '2025-05-03','2025-05-16', false, 0, false),
(null, 'eeee', 1041, '2025-05-03','2025-05-16', false, 0, false),
(null, 'eeee', 1042, '2025-05-03','2025-05-16', false, 0, false),
(null, 'eeee', 1043, '2025-05-03','2025-05-16', false, 0, false),
(null, 'eeee', 1044, '2025-05-03','2025-05-16', false, 0, false),
(null, 'eeee', 1045, '2025-05-03','2025-05-16', false, 0, false),
(null, 'eeee', 1046, '2025-05-03','2025-05-16', false, 0, false);
-- id: 'eeee'의 7건의 대출이력/모범

insert into rent values
(null, 'ffff', 1050, '2025-05-03','2025-05-16', false, 0, false),
(null, 'ffff', 1051, '2025-05-03','2025-05-16', false, 0, false),
(null, 'ffff', 1052, '2025-05-03','2025-05-16', false, 0, false);
-- id: 'ffff'의 3건의 대출이력/일반

insert into rent values
(null, 'gggg', 1060, '2025-05-03','2025-05-16', false, 0, false);
-- id: 'gggg'의 1건의 대출이력/일반

insert into rent values
(null, 'hhhh', 1070, '2025-05-03','2025-05-16', false, 0, false);
-- id: 'hhhh'의 1건의 대출이력/장기연체자

insert into rent values
(null, 'iiii', 1080, '2025-05-03','2025-05-16', false, 0, false);
-- id: 'iiii'의 1건의 대출이력/장기연체자

insert into rent values
(null, 'jjjj', 1090, '2025-05-03','2025-05-16', false, 0, false);
-- id: 'jjjj'의 1건의 대출이력/신입

select * from rent;