package com.example.spring_security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.spring_security.entities.User;
import com.example.spring_security.services.JwtService;

@SpringBootTest
class SpringSecurityApplicationTests {

	@Autowired
	private JwtService jwtService;

	@Test
	void contextLoads() {

		User user = new User();
		user.setId(1L);
		user.setName("sonali");
		user.setEmail("sonali123.com");
		user.setPassword("sonali123");

		String token = jwtService.generateToken(user);
		System.out.println("token   : " + token);

		Long userId = jwtService.getUserIdFromToken(token);
		System.out.println("userId" + userId);

	}

}
