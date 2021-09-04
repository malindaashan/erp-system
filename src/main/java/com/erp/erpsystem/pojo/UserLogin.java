package com.erp.erpsystem.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_login" , schema = "erp")
public class UserLogin implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	@Column(name="employee_detail_id")
	Long employeeDetailId;
	
	String username;
	
	String password;
	
	String privilege_id;
	
	String status;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getEmployeeDetailId() {
		return employeeDetailId;
	}
	public void setEmployeeDetailId(Long employeeDetailId) {
		this.employeeDetailId = employeeDetailId;
	}
	public String getPrivilege_id() {
		return privilege_id;
	}
	public void setPrivilege_id(String privilege_id) {
		this.privilege_id = privilege_id;
	}

	

	
	
}
