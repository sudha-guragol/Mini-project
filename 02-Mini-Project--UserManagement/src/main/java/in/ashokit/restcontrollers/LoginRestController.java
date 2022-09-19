package in.ashokit.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.ashokit.bindings.LoginForm;
import in.ashokit.service.UserService;

@RestController
public class LoginRestController {
	@Autowired
	private UserService service;
	
	//@RequestBody is used bcz v are sending sensitive data
	@PostMapping("/login")
	public String loginCheck(@RequestBody LoginForm loginForm)
	{
		//loginForm OBJ
		String loginCheck = service.LoginCheck(loginForm);
	     return loginCheck;
	}
	
	

}
