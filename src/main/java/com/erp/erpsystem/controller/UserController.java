package com.erp.erpsystem.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.erp.erpsystem.user.impl.UserLoginImpl;



@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserLoginImpl repository;

    @RequestMapping(value = "/checkAuth", method = RequestMethod.GET)
 	public String CheckAuth(HttpServletRequest request){
 		return repository.CheckAuth(request);
 	}
    
    @RequestMapping(value = "/checkHomePage", method = RequestMethod.GET)
 	public String ValidateLogin(HttpServletRequest request){
    	HttpSession session = request.getSession();  
    	if (session.getAttribute("LOGIN_USER") != null){
    	   	return "home";
    	}else{
    	   	return "login";
    	}

 	}
    
//    @RequestMapping(value = "/save/userLogin", method = RequestMethod.POST)
// 	public void saveUser(HttpServletRequest request){
//    	 repository.saveUser(request);
//    	 return;
// 	}
//    
//    @RequestMapping(value = "/getUserByUsername", method = RequestMethod.GET)
//  	public List<UserLogin> getUserByUsername(HttpServletRequest request){
//     	return repository.getUserByUsername(request);
//  	}
    
}
