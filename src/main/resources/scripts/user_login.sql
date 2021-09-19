create table user_login(
id INT NOT NULL AUTO_INCREMENT,
employee_detail_id INT,
username varchar(100),
password varchar(100),
status varchar(1),
privilege_id INT,
PRIMARY KEY (id)
);