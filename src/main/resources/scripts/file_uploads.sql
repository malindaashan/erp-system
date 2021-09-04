create table file_uploads(
id INT NOT NULL AUTO_INCREMENT,
name varchar(255),
des varchar(255),
approve_status  VARCHAR(1) DEFAULT 'P',
PRIMARY KEY (id)
)