package com.devsuperior.dsmovie.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.projections.UserDetailsProjection;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.tests.UserDetailsFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import com.devsuperior.dsmovie.utils.CustomUserUtil;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class UserServiceTests {

	@InjectMocks
	private UserService service;
	
	@Mock
	private UserRepository repository;
	
	@Mock
	private CustomUserUtil userUtil;
	
	private UserEntity userEntity;
	private String validUsername, invalidUsername;
	List<UserDetailsProjection> list;
	
	@BeforeEach
	void set() {
		userEntity=UserFactory.createUserEntity();
		validUsername=userEntity.getUsername();
		invalidUsername="gabriel@gmail.com";
		list=UserDetailsFactory.createCustomAdminClientUser(validUsername);
		
		Mockito.when(repository.findByUsername(validUsername)).thenReturn(Optional.of(userEntity));
		Mockito.when(repository.findByUsername(invalidUsername)).thenReturn(Optional.empty());
		
		Mockito.when(repository.searchUserAndRolesByUsername(validUsername)).thenReturn(list);
		Mockito.when(repository.searchUserAndRolesByUsername(invalidUsername)).thenReturn(new ArrayList<>());

	}
	
	@Test
	public void authenticatedShouldReturnUserEntityWhenUserExists() {
		Mockito.when(userUtil.getLoggedUsername()).thenReturn(validUsername);		
		UserEntity result=service.authenticated();
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getUsername(), validUsername);
		
	}

	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {		
		Mockito.doThrow(ClassCastException.class).when(userUtil).getLoggedUsername();
		
		Assertions.assertThrows(UsernameNotFoundException.class, ()->{
			UserEntity result=service.authenticated();
		});
	}

	@Test
	public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
		
		UserDetails result=service.loadUserByUsername(validUsername);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getUsername(), validUsername);
		
	}

	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
		
		Assertions.assertThrows(UsernameNotFoundException.class, ()->{
			service.loadUserByUsername(invalidUsername);
		});
		
	}
}
