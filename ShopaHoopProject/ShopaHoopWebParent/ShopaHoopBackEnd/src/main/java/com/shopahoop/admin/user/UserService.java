package com.shopahoop.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopahoop.common.entity.Role;
import com.shopahoop.common.entity.User;

@Service
@Transactional
public class UserService {

	public static final int USERS_PER_PAGE = 5;
	
	private final UserRepository userRepo;
	
	
	private final RoleRepository roleRepo;
	
	
	private final PasswordEncoder passwordEncoder;
	
	
	@Autowired
	public UserService(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
		
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.passwordEncoder = passwordEncoder;
	}

	public List<User> listAll(){
		
	return (List<User>) userRepo.findAll();
	}
	
	public Page<User> listByPage(int pageNumber, String sortField, String sortDir) {
		
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable= PageRequest.of(pageNumber - 1, USERS_PER_PAGE, sort);
		
		return userRepo.findAll(pageable);
	}
	
	public List<Role> listRoles(){
		
		return (List<Role>) roleRepo.findAll();
	}

	public User save(User user) {
		boolean isUpdatingUser =(user.getId() != null);
		
		if (isUpdatingUser) {
			User existingUser = userRepo.findById(user.getId()).get();
			
			if (user.getPassword().isEmpty()){
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			
		
		encodePassword(user);
		}
		return userRepo.save(user);
		
	}
	
	private void encodePassword(User user) {
		
		String encodedPass = passwordEncoder.encode(user.getPassword());
		
		user.setPassword(encodedPass);
		
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		
		User userByEmail = userRepo.getUserByEmail(email);
		
		if(userByEmail == null) return true;
			
		boolean isCreatingNew = (id == null);
		
		if(isCreatingNew) {
			if (userByEmail != null) return false;
		} else {
			if (userByEmail.getId() != id) {
				return false;
			}
		}
		
		return true;
	}

	public User get(Integer id) throws UserNotFoundException {
		
		try {
		
		return userRepo.findById(id).get();
		}
		catch (NoSuchElementException ex) {
			
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
	}
		
	public void delete(Integer id) throws UserNotFoundException {
		Long countById = userRepo.countById(id);
		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
		
		userRepo.deleteById(id);
	}
	
	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		
		userRepo.updateEnabledStatus(id, enabled);
	}
	     
}
