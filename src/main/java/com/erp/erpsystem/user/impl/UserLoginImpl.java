package com.erp.erpsystem.user.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.erpsystem.pojo.UserLogin;
import com.erp.erpsystem.user.dao.UserLoginRepository;


@Service
public class UserLoginImpl {
	
	@Autowired
	UserLoginRepository userloginrepository;
	
	public String CheckAuth(HttpServletRequest  request){
		HttpSession session = request.getSession();
		List<UserLogin> users =	userloginrepository.findByusername(request.getParameter("username"));
	
		if (users.size() == 1) {
			if (users.get(0).getPassword().equals(request.getParameter("password"))) {
				session.setAttribute("LOGIN_USER", request.getParameter("username"));
				return "Success";
			} else {
				session.removeAttribute("LOGIN_USER");
				return "Password Incorrect";
			}
		} else {
		  }
		session.removeAttribute("LOGIN_USER");
		return "No User";
		}
	
	public void saveUser(HttpServletRequest request){
		UserLogin user = new UserLogin();
		user.setId(null);
		user.setEmployeeDetailId(Long.parseLong(request.getParameter("employeeDetailId")));
		user.setUsername(request.getParameter("username"));
		user.setPassword(request.getParameter("password"));
		user.setStatus("Y");
		userloginrepository.save(user);
		return;
	}
	public List<UserLogin> getUserByUsername(HttpServletRequest request){
		return userloginrepository.findByusername(request.getParameter("username"));
	}
}
