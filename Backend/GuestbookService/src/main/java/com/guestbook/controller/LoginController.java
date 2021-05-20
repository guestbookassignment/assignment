package com.guestbook.controller;

import java.security.Principal;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.guestbook.dto.UserDto;
import com.guestbook.service.LoginService;
import static com.guestbook.util.Constants.LOGIN;
import static com.guestbook.util.Constants.USER;

@RestController
public class LoginController {

	@Autowired
	private LoginService loginService;
	
	
	@PostMapping(LOGIN)
	public boolean login(@RequestBody UserDto dto) throws IllegalAccessException {
		return loginService.validate(dto);
	}

	@PostMapping(USER)
	public Principal user(HttpServletRequest request) {
		String authToken = request.getHeader("Authorization").substring("Basic".length()).trim();
		String username = new String(Base64.getDecoder().decode(authToken)).split(":")[0];
		loginService.updateLastLoginDate(username);
		return () -> username;
	}
}
