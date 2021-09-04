package com.erp.erpsystem.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.erpsystem.pojo.FileUpload;

public interface FileUploadRepository extends JpaRepository<FileUpload, Long>{

}
