package in.ashokit.service;

import java.util.Map;

import in.ashokit.bindings.LoginForm;
import in.ashokit.bindings.UnlockAccountForm;
import in.ashokit.bindings.UserRegForm;
import in.ashokit.exception.UserAppException;

public interface UserService {

	
public String loginCheck ( LoginForm loginForm) throws UserAppException;

public Map<Integer,String> getCountries();

public Map<Integer,String> getStates(Integer countryId);

public Map<Integer,String> getCities(Integer stateId);

public String isEmailUnique(String emailId);

public Boolean saveUser(UserRegForm userForm) throws UserAppException;

public Boolean unlockAccount(UnlockAccountForm unlockAccForm) throws UserAppException;

public Boolean forgotPassword (String emailId) throws UserAppException;

}
