package com.shopahoop.admin.user;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopahoop.admin.FileUploadUtil;
import com.shopahoop.common.entity.Role;
import com.shopahoop.common.entity.User;

@Controller
@Scope("session")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public String listFirstPage(Model theModel) {

		return listByPage(1, theModel, "firstName", "asc");
	}
	
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model theModel,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir) {
		
		Page<User> page = userService.listByPage(pageNum, sortField, sortDir);
		List<User> listUsers = page.getContent();
		
		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE +1;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
		
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		theModel.addAttribute("totalPages", page.getTotalPages());
		theModel.addAttribute("currentPage", pageNum);
		theModel.addAttribute("startCount", startCount);
		theModel.addAttribute("endCount", endCount);
		theModel.addAttribute("totalElements", page.getTotalElements());
		theModel.addAttribute("listUsers", listUsers);
		theModel.addAttribute("sortField", sortField);
		theModel.addAttribute("sortDir", sortDir);
		theModel.addAttribute("reverseSortDir", reverseSortDir);
	
		return "users";
	}

	@GetMapping("/users/new")
	public String newUser(Model theModel) {
		List<Role> listRoles = userService.listRoles();
		User user = new User();
		user.setEnabled(true);
		theModel.addAttribute("user", user);
		theModel.addAttribute("listRoles", listRoles);
		theModel.addAttribute("pageTitle", "Create New User");
		return "user_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {

		if (!multipartFile.isEmpty()) {

			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = userService.save(user);
			String uploadDir = "user-photos/" + savedUser.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		} else {

			if (user.getPhotos().isEmpty())
				user.setPhotos(null);

			userService.save(user);
		}
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");

		return "redirect:/users";
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes,
			Model theModel) {
		try {
			User user = userService.get(id);
			List<Role> listRoles = userService.listRoles();
			theModel.addAttribute("listRoles", listRoles);
			theModel.addAttribute("user", user);
			theModel.addAttribute("pageTitle", "Edit User ID:" + id);
			return "user_form";
		} catch (UserNotFoundException ex) {

			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";

		}

	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {

		try {
			userService.delete(id);

			redirectAttributes.addFlashAttribute("message", "User has been successfully deleted.");

		} catch (UserNotFoundException ex) {

			redirectAttributes.addFlashAttribute("message", ex.getMessage());

		}

		return "redirect:/users";
	}

	@GetMapping("/users/{id}/enabled/{status}")
	public String updateEnabledUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes,
			@PathVariable(name = "status") boolean enabled) {

		userService.updateUserEnabledStatus(id, enabled);

		redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been successfully enabled.");

		return "redirect:/users";
	}

	@GetMapping("/users/{id}/disable/{status}")
	public String updateDisabledUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes,
			@PathVariable(name = "status") boolean enabled) {

		userService.updateUserEnabledStatus(id, enabled);

		redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been successfully disabled.");

		return "redirect:/users";
	}
}
