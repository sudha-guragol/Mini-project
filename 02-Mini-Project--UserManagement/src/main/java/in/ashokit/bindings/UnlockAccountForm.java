package in.ashokit.bindings;

import lombok.Data;

@Data
public class UnlockAccountForm {

	private String email;
	private String temporaryPassword;
	private String newPassword;
	private String confirmPassword;
	
}
