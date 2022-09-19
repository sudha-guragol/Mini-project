package in.ashokit.restcontrollers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import in.ashokit.constants.AppConstants;
import in.ashokit.properties.AppProperties;

@RestController
public class WelcomeRestController {
	
	@Autowired
	private AppProperties appProps;
	
	@GetMapping("/welcome")
	public String getWelcomeMsg()
	{
		Map<String, String> msgs = appProps.getMessages();
		String msg = msgs.get(AppConstants.WELCOME_MSG);
		
		return msg;
	}
	
	
	@GetMapping("/greet")
	public String greetWelcomeMsg()
	{
		Map<String, String> msgs = appProps.getMessages();
		String greetmessage = msgs.get(AppConstants.GREET_MSG);
		return greetmessage;
	}
}
