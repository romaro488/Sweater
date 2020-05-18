package ua.polosmak.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ua.polosmak.domain.Message;
import ua.polosmak.domain.User;
import ua.polosmak.repository.MessageRepository;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static ua.polosmak.controller.ControllerUtils.getErrors;

@Controller
public class MainController {

	private final MessageRepository messageRepo;
	@Value("${upload.path}")
	private String uploadPath;

	public MainController(MessageRepository messageRepo) {
		this.messageRepo = messageRepo;
	}

	@GetMapping("/")
	public String greeting(Map<String, Object> model) {
		return "greeting";
	}

	@GetMapping("/main")
	public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
		Iterable<Message> messages = messageRepo.findAll();

		if (filter != null && !filter.isEmpty()) {
			messages = messageRepo.findByTag(filter);
		} else {
			messages = messageRepo.findAll();
		}

		model.addAttribute("messages", messages);
		model.addAttribute("filter", filter);

		return "main";
	}

	@PostMapping("/main")
	public String add(
			@AuthenticationPrincipal User user,
			@Valid Message message,
			BindingResult bindingResult,
			Model model,
			@RequestParam("file") MultipartFile file
	) throws IOException {
		message.setAuthor(user);
		if (bindingResult.hasErrors()) {
			Map<String, String> errorMap = getErrors(bindingResult);
			model.mergeAttributes(errorMap);
			model.addAttribute("message", message);
		} else {
			if (file != null && !file.getOriginalFilename().isEmpty()) {
				File uploadDir = new File(uploadPath);
				if (!uploadDir.exists()) {
					uploadDir.mkdir();
				}

				String uuidFile = UUID.randomUUID().toString();
				String resultFileName = uuidFile + "." + file.getOriginalFilename();

				file.transferTo(new File(uploadPath + "/" + resultFileName));

				message.setFilename(resultFileName);
			}
			model.addAttribute("message",null);

			messageRepo.save(message);
		}
		Iterable<Message> messages = messageRepo.findAll();

		model.addAttribute("messages", messages);

		return "main";
	}


}
