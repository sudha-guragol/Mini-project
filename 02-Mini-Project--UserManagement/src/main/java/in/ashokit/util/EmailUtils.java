package in.ashokit.util;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import in.ashokit.exception.SmtpException;

@Component
public class EmailUtils {
	@Autowired
	private JavaMailSender mailSender;
	
	public Boolean sendEmail(String to,String subject,String body) throws Exception
	{
		Boolean isSent =false;
		  MimeMessage mimeMessage = mailSender.createMimeMessage();
		try
		{
			

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
 
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setTo(to);
            //BELOW LINE TRUE MEANS body is html text
            mimeMessageHelper.setText(body, true);;
 
            
            mailSender.send(mimeMessageHelper.getMimeMessage());
			isSent=true;
		}
		catch(Exception e)
		{
			throw new SmtpException(e.getMessage());
		}
		return isSent;
	}
	

}
