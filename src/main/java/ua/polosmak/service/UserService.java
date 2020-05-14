package ua.polosmak.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ua.polosmak.domain.Role;
import ua.polosmak.domain.User;
import ua.polosmak.repository.UserRepository;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
	private final UserRepository userRepo;
	private final MailService mailService;
	;

	public UserService(UserRepository userRepo, MailService mailService) {
		this.userRepo = userRepo;
		this.mailService = mailService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepo.findByUsername(username);
	}

	public boolean addUser(User user) {
		User userFromDb = userRepo.findByUsername(user.getUsername());
		if (userFromDb != null) {
			return false;
		}
		user.setActive(true);
		user.setRoles(Collections.singleton(Role.USER));
		user.setActivationCode(UUID.randomUUID().toString());
		userRepo.save(user);

		if (!StringUtils.isEmpty(user.getEmail())) {
			String message = String.format(
					"Hello, %s \n" +
							"Welcome to Sweater. Please, visit next link: http://localhost:8080/activate/%s ",
					user.getUsername(),
					user.getActivationCode()
			);
			mailService.sendEmail(user.getEmail(), "Activation code", message);
		}

		return true;
	}

	public boolean activateUser(String code) {
		User user = userRepo.findByActivationCode(code);
		if (user == null) {
			return false;
		}
		user.setActivationCode(null);
		userRepo.save(user);

		return true;
	}
}
