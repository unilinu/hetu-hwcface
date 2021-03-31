-- MySQL

-- DATABASE: pml
-- TABLE: user

-- FIELD: id, name, facepath

-- ENTRY1: 1001, zhangsan, 
-- ENTRY2: 1002, lisi, 


DROP DATABASE IF EXISTS pml;

create database pml charset utf8;
use pml;
create table user(`Id` int not null AUTO_INCREMENT primary key, `Name` varchar(20) NOT NULL, `FacePath` varchar(4096) NOT NULL);
insert into user (Id,Name,FacePath) values (1001,'张三','/home/openlkadmin/team-1269929257/data/luoxiang.jpg');
insert into user (Id,Name,FacePath) values (1002,'李四','/home/openlkadmin/team-1269929257/data/luoyonghao.jpg');
select * from user;