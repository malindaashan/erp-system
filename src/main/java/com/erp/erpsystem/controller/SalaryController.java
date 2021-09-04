package com.erp.erpsystem.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.erp.erpsystem.user.impl.SalaryImpl;



@RestController
@RequestMapping("/salary")
public class SalaryController {
	@Autowired
	SalaryImpl salary;
	
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
 	public String saveEmployee(HttpServletRequest request,@RequestParam("file") MultipartFile multipartFile){
 		return salary.saveEmployee(request,multipartFile);
 	}
    
}
