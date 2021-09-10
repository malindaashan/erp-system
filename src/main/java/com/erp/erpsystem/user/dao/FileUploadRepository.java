package com.erp.erpsystem.user.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.erp.erpsystem.pojo.FileUpload;

public interface FileUploadRepository extends JpaRepository<FileUpload, Long>{

	List<FileUpload> findByStatus(String status);
	
	@Transactional
	@Modifying
	@Query("update FileUpload p set p.status=?2 where p.id =?1")
	void setById(Long id,String status);

}
