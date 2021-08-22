package com.erp.erpsystem.user.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.erp.erpsystem.pojo.UserLogin;


public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {
	
	@Query("select u from UserLogin u where u.username = ?1")
	  List <UserLogin> findByusername(String username);

}
