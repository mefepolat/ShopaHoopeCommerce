package com.shopahoop.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopahoop.common.entity.Role;
import com.shopahoop.common.entity.User;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
	
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	
	@Test
	public void testCreateNewUser() {
		
		Role roleAdmin = entityManager.find(Role.class, 1); 
	
		
		User userNamHM = new User("mefepolat@hotmail.com", "15231523", "Efe", "Polat");
		
		
		userNamHM.addRole(roleAdmin);
		
		User savedUser = repo.save(userNamHM);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserMultipleRoles() {
		
		User userBegum = new User("begumbysl@gmail.com", "Begumefe0604", "Begum", "Baysal");
		
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);

		
		
		userBegum.addRole(roleEditor);
		userBegum.addRole(roleAssistant);
		
		User savedUser = repo.save(userBegum);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testListAllUsers() {
		
		Iterable<User> listUsers = repo.findAll();
		
		listUsers.forEach(user -> System.out.println(user));
		
	}
	
	@Test
	public void testGetUserById() {
		
		User user = repo.findById(1).get();
		System.out.print(user);
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		
		User user = repo.findById(1).get();
		user.setEnabled(true);
		user.setEmail("mefepolat34@icloud.com");
		repo.save(user);

		
	}
	
	@Test
	public void testUpdateUserRoles() {
		
		User user = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		user.getRoles().remove(roleEditor);
		user.addRole(roleSalesperson);
		
		repo.save(user);
	
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
		
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "mefepolat34@icloud.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		
		Integer id = 1;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}

}
