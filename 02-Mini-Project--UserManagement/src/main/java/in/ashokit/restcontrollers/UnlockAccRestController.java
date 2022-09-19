package in.ashokit.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.UsesSunHttpServer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.ashokit.bindings.UnlockAccountForm;
import in.ashokit.service.UserService;

@RestController
public class UnlockAccRestController {
	
	@Autowired
	private UserService service;
	
	@PostMapping("/unlock")
	public String UnlockUserAcc(@RequestBody UnlockAccountForm unlockAccForm)
	{
		Boolean status = service.unlockAccount(unlockAccForm);
		
		if(status)
		{
			return "Account Unlocked successfully";
		}
		else
		{
			return "Incorrect Temporary Password";
		}
	}

}
