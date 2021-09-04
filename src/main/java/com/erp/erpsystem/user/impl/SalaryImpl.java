package com.erp.erpsystem.user.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.erp.erpsystem.pojo.FileUpload;
import com.erp.erpsystem.user.dao.FileUploadRepository;


@Service
public class SalaryImpl {
	
	@Value("${erp.file.save.location}")
	private String filePath;
	
	@Autowired
	FileUploadRepository fileRepo;
	
	public String saveEmployee(HttpServletRequest request, MultipartFile file){
		FileUpload fileUpload = new FileUpload();
		//fileUpload.setId(null);
		fileUpload.setDes(request.getParameter("description"));
		fileUpload.setName(request.getParameter("filename"));
		fileRepo.save(fileUpload);
		  try {
			writeBytesToFile(filePath+request.getParameter("filename")+".xlsx", file.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}

		return "Success";
	}
	  private static void writeBytesToFile(String fileOutput, byte[] bytes)
		        throws IOException {

		        try (FileOutputStream fos = new FileOutputStream(fileOutput)) {
		            fos.write(bytes);
		        }

		    }
}
