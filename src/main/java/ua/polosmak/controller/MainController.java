package ua.polosmak.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.polosmak.domain.Message;
import ua.polosmak.domain.User;
import ua.polosmak.repository.MessageRepository;

import java.util.Map;

@Controller
public class MainController {

	private final MessageRepository messagesRepo;

	public MainController(MessageRepository messagesRepo) {
		this.messagesRepo = messagesRepo;
	}

	@GetMapping("/")
	public String greeting(Map<String, Object> model) {
		return "greeting";
	}

	@GetMapping("/main")
	public String main(Map<String, Object> model) {
		Iterable<Message> messages = messagesRepo.findAll();
		model.put("messages", messages);

		return "main";
	}

	@PostMapping("/main")
	public String add(@AuthenticationPrincipal User user,
					  @RequestParam String text,
					  @RequestParam String tag,
					  Map<String, Object> model) {
		Message message = new Message(text, tag, user);
		messagesRepo.save(message);
		Iterable<Message> messages = messagesRepo.findAll();

		model.put("messages", messages);

		return "main";
	}

	@PostMapping("filter")
	public String filter(@RequestParam String filter, Map<String, Object> model) {
		Iterable<Message> messages;
		if (filter != null && !filter.isEmpty()) {
			messages = messagesRepo.findByTag(filter);
		} else {
			messages = messagesRepo.findAll();
		}
		model.put("messages", messages);
		return "main";

	}
}
