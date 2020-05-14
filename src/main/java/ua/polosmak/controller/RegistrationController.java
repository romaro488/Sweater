package ua.polosmak.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ua.polosmak.domain.Role;
import ua.polosmak.domain.User;
import ua.polosmak.repository.UserRepository;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
	private final UserRepository userRepo;

	public RegistrationController(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@GetMapping("/registration")
	public String registration() {
		return "registration";
	}

	@PostMapping("/registration")
	public String addUser(User user, Map<String, Object> model) {
		User userFromDb = userRepo.findByUsername(user.getUsername());

		if (userFromDb != null) {
			model.put("message", "User exists!");
			return "registration";
		}

		user.setActive(true);
		user.setRoles(Collections.singleton(Role.USER));
		userRepo.save(user);

		return "redirect:/login";
	}
}
