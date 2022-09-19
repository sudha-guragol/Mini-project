package in.ashokit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactController {
	@GetMapping("/hello")
		public String welcome(Model model)
	{
		model.addAttribute("msg", "Welcome to ashokit Training Institute");
		System.out.println("hiiiiiii");
		return "index";
	}

}
