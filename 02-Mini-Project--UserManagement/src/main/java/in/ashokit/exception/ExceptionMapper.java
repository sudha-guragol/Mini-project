package in.ashokit.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionMapper {

	
	@ExceptionHandler(value=SmtpException.class)
	public ResponseEntity<ErrorResponse> handleSmtpException(SmtpException se)
	{
		ErrorResponse response = new ErrorResponse();
		response.setErrorCode("SMTP101");
		response.setErrorMsg(se.getMessage());
		response.setDateTime(LocalDateTime.now());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
}
