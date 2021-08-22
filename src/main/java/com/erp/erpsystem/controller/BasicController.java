package com.erp.erpsystem.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {
	
	@RequestMapping(value = "/logOut", method = RequestMethod.GET)
	public void logOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		session.removeAttribute("LOGIN_USER");
		response.sendRedirect("");
	}
/*	@RequestMapping(value = "/send", method = RequestMethod.POST)
   	public void sendMail(HttpServletRequest request) {
		emailserviceimpl.sendSimpleMessage("malinda.ashan@gmail.com", "HI", "This is the text");
    	return;
   	}*/
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() throws Exception {
		return "Malinda";
	}
}
