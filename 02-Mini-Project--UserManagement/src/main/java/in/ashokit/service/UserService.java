package in.ashokit.service;

import java.util.List;
import java.util.Map;

import in.ashokit.bindings.LoginForm;
import in.ashokit.bindings.UserRegForm;
import in.ashokit.bindings.UnlockAccountForm;
import in.ashokit.entity.CitiesMasterEntity;
import in.ashokit.entity.CountryMasterEntity;
import in.ashokit.entity.StateMasterEntity;
import in.ashokit.entity.UserAccountEntity;

public interface UserService {

	
public String LoginCheck ( LoginForm loginForm);

public Map<Integer,String> getCountries();

public Map<Integer,String> getStates(Integer countryId);

public Map<Integer,String> getCities(Integer stateId);

public String isEmailUnique(String emailId);

public Boolean saveUser(UserRegForm userForm) throws Exception;

public Boolean unlockAccount(UnlockAccountForm unlockAccForm);

public Boolean ForgotPassword (String emailId) throws Exception;







}
