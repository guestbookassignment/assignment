package com.guestbook.service;

import java.sql.Date;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.guestbook.dto.UserDto;
import com.guestbook.entity.UserDetails;
import com.guestbook.repo.UserDetailsRepository;


@Service
public class LoginService {

    @Autowired
    private UserDetailsRepository repository;
    
    public boolean validate(UserDto dto) throws IllegalAccessException {
        UserDetails user = repository.findByUsername(dto.getUsername());
        if (user != null) {
        	if(!user.isEnabled()) {
        		throw new IllegalAccessException("Kindly validate the email address and try login!");
        	}
            if (user.getUsername().equalsIgnoreCase(dto.getUsername()) && new BCryptPasswordEncoder().matches(dto.getPassword(), user.getPassword())) {
                Calendar cal = Calendar.getInstance();
                user.setLastLoginDate(new Date(cal.getTime().getTime()));
                repository.saveAndFlush(user);
                return true;
            }
        }
        return false;
    }
    
    public void updateLastLoginDate(String username) {
        UserDetails user = repository.findByUsername(username);
        if (user != null) {
            Calendar cal = Calendar.getInstance();
            user.setLastLoginDate(new Date(cal.getTime().getTime()));
        }
        repository.saveAndFlush(user);
    }
}
