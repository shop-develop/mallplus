package com.zscat.mallplus.sys.controller;


import com.zscat.mallplus.bo.AdminUserDetails;
import com.zscat.mallplus.sys.entity.SysUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {
	public SysUser getCurrentUser() {
		try {
			SecurityContext ctx = SecurityContextHolder.getContext();
			Authentication auth = ctx.getAuthentication();
			AdminUserDetails memberDetails = (AdminUserDetails) auth.getPrincipal();
			return memberDetails.getUmsAdmin();
		}catch (Exception e){
			return new SysUser();
		}
	}
}