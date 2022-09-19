package in.ashokit.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import in.ashokit.exception.UserAppException;
import in.ashokit.service.UserService;

@RestController
public class ForgotPwdRestController  {
	
	@Autowired
	private UserService service;
	//emailId is path parameter that is read as pathvariable
	@GetMapping("/forgotPwd/{emailId}")
	public String forgotPwd(@PathVariable String emailId) throws UserAppException
	{
		Boolean status = service.forgotPassword(emailId);
	if(status)
	{
		return "we have sent password to your email";
	}
	else
	{
		return "please enter valid email Id";
	}
		
	}

}
