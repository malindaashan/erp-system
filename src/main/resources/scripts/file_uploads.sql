create table erp.file_uploads(
id INT NOT NULL AUTO_INCREMENT,
name varchar(255),
des varchar(255),
approve_status  VARCHAR(1) DEFAULT 'P',
uploaded_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
approved_date varchar(255),
PRIMARY KEY (id)
)