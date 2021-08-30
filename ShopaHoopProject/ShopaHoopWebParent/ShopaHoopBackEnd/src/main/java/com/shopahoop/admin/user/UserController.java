package com.shopahoop.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopahoop.common.entity.Role;
import com.shopahoop.common.entity.User;

@Controller
public class UserController {

	@Autowired	
	private UserService userService;
	
	@GetMapping("/users")
	public String listAll(Model theModel) {
		
		List<User> listUsers = userService.listAll();
		theModel.addAttribute("listUsers", listUsers);
		
		return "users";
		
	}
	
	@GetMapping("/users/new")
	public String newUser(Model theModel) {
		List<Role> listRoles = userService.listRoles();
		User user = new User();
		user.setEnabled(true);
		theModel.addAttribute("user", user);
		theModel.addAttribute("listRoles",listRoles);
		theModel.addAttribute("pageTitle", "Create New User");
		return "user_form";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes) {
		
		userService.save(user);
		
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
		
		
		
		return "redirect:/users";
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable (name = "id") Integer id, RedirectAttributes redirectAttributes, Model theModel) {
		try {
		User user = userService.get(id);
		List<Role> listRoles = userService.listRoles();
		theModel.addAttribute("listRoles",listRoles);
		theModel.addAttribute("user", user);
		theModel.addAttribute("pageTitle", "Edit User ID:" + id);
		return "user_form";
		} catch (UserNotFoundException ex) {
			
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";


		}
		
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable (name = "id") Integer id, RedirectAttributes redirectAttributes, Model theModel) {
		
		try {
			 userService.delete(id);
			
			 redirectAttributes.addFlashAttribute("message", "User has been successfully deleted.");
	
			
		} catch (UserNotFoundException ex) {
			
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			
			
		}
		 
		
		return "redirect:/users";
	}
	
	
}
