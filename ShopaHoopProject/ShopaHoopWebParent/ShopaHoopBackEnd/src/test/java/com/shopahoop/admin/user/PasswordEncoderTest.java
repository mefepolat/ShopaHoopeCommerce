package com.shopahoop.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;


public class PasswordEncoderTest {

	
	@Test
	public void testEncodePassword() {
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		String rawPassword = "15231523";
		
		String encodedPass = passwordEncoder.encode(rawPassword);
		
		System.out.println(encodedPass.toString());
		
		boolean matches = passwordEncoder.matches(rawPassword, encodedPass);
		
		assertThat(matches).isTrue();
		
	}
	
}
