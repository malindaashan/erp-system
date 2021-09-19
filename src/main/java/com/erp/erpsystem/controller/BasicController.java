package com.erp.erpsystem.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {
	static final Logger logger = LoggerFactory.getLogger(BasicController.class);
	
	@RequestMapping(value = "/logOut", method = RequestMethod.GET)
	public void logOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("logOut Started...");
		HttpSession session = request.getSession();
		session.removeAttribute("LOGIN_USER");
		response.sendRedirect("");
	}
}
