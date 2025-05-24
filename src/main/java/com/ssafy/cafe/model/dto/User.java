package com.ssafy.cafe.model.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor

public class User {
    private String id;
    private String name;
    private String pass;
    private String email;
    private String role;
    private Integer stamps;
    private String provider;

    private List<Stamp> stampList = new ArrayList<>();
    public List<String> getRoleList(){
        if(this.role.length()>0){
            return Arrays.asList(this.role.split(","));
        }
        return new ArrayList<>();
    }
    public User(String id, String name, String pass, Integer stamps) {
        this.id = id;
        this.name = name;
        this.pass = pass;
        this.stamps = stamps;
    }
    public User(){

    }
//    

    
//
//
//	public String getEmail() {
//		return email;
//	}
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public String getRole() {
//		return role;
//	}
//	public void setRole(String role) {
//		this.role = role;
//	}
//	public String getProvider() {
//		return provider;
//	}
//	public void setProvider(String provider) {
//		this.provider = provider;
//	}
//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public String getPass() {
//		return pass;
//	}
//
//	public void setPass(String pass) {
//		this.pass = pass;
//	}
//
//	public Integer getStamps() {
//		return stamps;
//	}
//
//	public void setStamps(Integer stamps) {
//		this.stamps = stamps;
//	}
//
//	public List<Stamp> getStampList() {
//		return stampList;
//	}
//
//	public void setStampList(List<Stamp> stampList) {
//		this.stampList = stampList;
//	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", pass=" + pass + ", stamps=" + stamps + ", stampList="
				+ stampList + "]";
	}

    
    
}