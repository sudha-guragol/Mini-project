package in.ashokit.util;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import in.ashokit.exception.UserAppException;

@Component
public class EmailUtils {
	
	private static Logger logger =LoggerFactory.getLogger(EmailUtils.class); 
	@Autowired
	private JavaMailSender mailSender;
	
	public Boolean sendEmail(String to,String subject,String body) throws UserAppException
	{
		Boolean isSent=false;
		
		try
		{
		
		  MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
 
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setTo(to);
            //BELOW LINE TRUE MEANS body is html text
            mimeMessageHelper.setText(body, true);
 
            
            mailSender.send(mimeMessageHelper.getMimeMessage());
			isSent=true;
	}
			catch(Exception e)
		{
				logger.error("exception occured :" +e.getMessage(),e);
				
		}
		return isSent;
	}
	

}
