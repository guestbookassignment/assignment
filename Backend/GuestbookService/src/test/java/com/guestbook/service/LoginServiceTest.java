package com.guestbook.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.guestbook.dto.UserDto;
import com.guestbook.entity.UserDetails;
import com.guestbook.repo.UserDetailsRepository;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

	@Mock
	private UserDetailsRepository repository;
	
	@InjectMocks
	private LoginService service;
	
	@Test
	public void test_Validate_success() throws IllegalAccessException {
		UserDto dto = new UserDto();
		dto.setUsername("Test");
		dto.setPassword("test");
		UserDetails user = new UserDetails();
		user.setEnabled(true);
		user.setUsername("Test");
		user.setPassword("$2y$12$p0/Ck3if9MJ.PhQCdUzoQerXfXAUC0MPpnBeKETreGIepuO1l/J1.");
		when(repository.findByUsername(dto.getUsername())).thenReturn(user);
		boolean result = this.service.validate(dto);
		verify(repository, times(1)).saveAndFlush(user);
		assertEquals(result, true);
	}
	
	@Test
	public void test_validate_failure() throws IllegalAccessException {
		UserDto dto = new UserDto();
		dto.setUsername("Test");
		dto.setPassword("test123");
		UserDetails user = new UserDetails();
		user.setEnabled(true);
		user.setUsername("Test");
		user.setPassword("$2y$12$p0/Ck3if9MJ.PhQCdUzoQerXfXAUC0MPpnBeKETreGIepuO1l/J1.");
		when(repository.findByUsername(dto.getUsername())).thenReturn(user);
		boolean result = this.service.validate(dto);
		verify(repository, times(0)).saveAndFlush(user);
		assertEquals(result, false);
	}
	
	@Test(expected = IllegalAccessException.class)
	public void test_validate_exception() throws IllegalAccessException {
		UserDto dto = new UserDto();
		dto.setUsername("Test");
		UserDetails user = new UserDetails();
		user.setEnabled(false);
		when(repository.findByUsername(dto.getUsername())).thenReturn(user);
		this.service.validate(dto);
	}
	
	@Test
	public void test_updateLastLoginDate() {
		UserDetails user = mock(UserDetails.class);
		String username="test";
		when(repository.findByUsername(username)).thenReturn(user);
		this.service.updateLastLoginDate(username);
		verify(repository, times(1)).saveAndFlush(user);
	}
}
