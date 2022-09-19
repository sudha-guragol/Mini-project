package in.ashokit.serviceimpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import in.ashokit.bindings.LoginForm;
import in.ashokit.bindings.UnlockAccountForm;
import in.ashokit.bindings.UserRegForm;
import in.ashokit.constants.AppConstants;
import in.ashokit.entity.CityMasterEntity;
import in.ashokit.entity.CountryMasterEntity;
import in.ashokit.entity.StateMasterEntity;
import in.ashokit.entity.UserAccountEntity;
import in.ashokit.exception.UserAppException;
import in.ashokit.properties.AppProperties;
import in.ashokit.repository.CitiesMasterRepository;
import in.ashokit.repository.CountryMasterRepository;
import in.ashokit.repository.StatesMasterRepository;
import in.ashokit.repository.UserAccountRepository;
import in.ashokit.service.UserService;
import in.ashokit.util.EmailUtils;
import in.ashokit.util.PwdUtils;

@Service // representour class as spring bean class
public class UserServiceImpl implements UserService {

	private static Logger logger =LoggerFactory.getLogger(UserServiceImpl.class); 
	
	@Autowired
	// to call repository method v use repo interfaces
	private CountryMasterRepository countryRepo;

	@Autowired
	private StatesMasterRepository stateRepo;

	@Autowired
	private CitiesMasterRepository cityRepo;

	@Autowired
	private UserAccountRepository userRepo;

	@Autowired
	private AppProperties appProps;

	@Autowired
	private EmailUtils emailUtils;

	/**
	 * (java doc comments//tell purpose of method)this method is used to check
	 * whether user (logged with email n password)Login
	 */
	@Override
	public String loginCheck(LoginForm loginForm) throws UserAppException{
		
		String msg ;
						
		String encryptedPwd = PwdUtils.encryptMsg(loginForm.getPazzword());
	
		UserAccountEntity user = userRepo.findByEmailAndPazzword(loginForm.getEmial(), encryptedPwd);
		if (user != null) {
			
			String accStatus = user.getAccStatus();
			if (AppConstants.LOCKED.equals(accStatus))// will not through null pointer exception (constant having

			{
				msg = appProps.getMessages().get(AppConstants.ACCOUNT_LOCKED);
			} 
			else {
				msg = AppConstants.SUCCESS;
			}
		} else {
		//	msg = "Invalid Credentials";
			msg = appProps.getMessages().get(AppConstants.INVALID_CREDENTIALS);

		}

		
		return msg;
	}

	@Override
	public Map<Integer, String> getCountries() {
		List<CountryMasterEntity> countries = countryRepo.findAll();
		Map<Integer, String> countryMap = new HashMap<>();
		countries.forEach(country -> countryMap.put(country.getCountryId(), country.getCountryName()));
		return countryMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer countryId) {

		List<StateMasterEntity> states = stateRepo.findByCountryId(countryId);
		Map<Integer, String> stateMap = new HashMap<>();
		states.forEach(state -> 
			stateMap.put(state.getStateId(), state.getStateName())
		);
		return stateMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer stateId) {
		List<CityMasterEntity> cities = cityRepo.findBystateId(stateId);
		Map<Integer, String> cityMap = new HashMap<>();
		cities.forEach(city -> 
			cityMap.put(city.getCityId(), city.getCityName())
		);
		return cityMap;
	}

	@Override
	public String isEmailUnique(String emailId) {
		UserAccountEntity entity = new UserAccountEntity();
		entity.setEmail(emailId.trim());
		
		Example<UserAccountEntity> example = Example.of(entity);
		
		Optional<UserAccountEntity> findOne = userRepo.findOne(example);
		if (findOne.isPresent()) {
			return AppConstants.DUPLICATE;

		} else {
			return AppConstants.UNIQUE;

		}
	}

	@Override
	public Boolean saveUser(UserRegForm userForm) throws UserAppException {
		
		UserAccountEntity entity = new UserAccountEntity();
		
		BeanUtils.copyProperties(userForm, entity);

		entity.setAccStatus(AppConstants.LOCKED);
		
		
		String randomPwd=generateRandomPazzword(6);
		
		String encryptedPwd = PwdUtils.encryptMsg(randomPwd);
			entity.setPazzword(encryptedPwd);
			
		entity = userRepo.save(entity);
		
		String emailBody = readUnlockAccEmailBody(entity);
		String subject = appProps.getMessages().get(AppConstants.UNLOCK_ACC_EMAIL_SUB);


		try {
			emailUtils.sendEmail(userForm.getEmail(), subject, emailBody);
		} catch ( Exception e) {
			logger.error(AppConstants.EXCEPTION_OCCURED +e.getMessage(),e);
			throw new UserAppException(e.getMessage());
		
		}

		return entity.getUserId() != null ? true : false;
	}

	@Override
	public Boolean unlockAccount(UnlockAccountForm unlockAccForm) throws UserAppException {
		String email = unlockAccForm.getEmail();
		String tempPasswrd = unlockAccForm.getTemporaryPassword();
		String encryptedPwd=PwdUtils.encryptMsg(tempPasswrd);
		UserAccountEntity user = userRepo.findByEmailAndPazzword(email, encryptedPwd);
		if (user != null) {
			
			String newPwd = unlockAccForm.getNewPassword();
			String encryptedNewPwd =null;
			try {
				encryptedNewPwd = PwdUtils.encryptMsg(newPwd);
				user.setPazzword(encryptedNewPwd);
			} catch (Exception e) {
				logger.error(AppConstants.EXCEPTION_OCCURED +e.getMessage(),e);
				throw new UserAppException(e.getMessage());
			}
	
			user.setAccStatus(AppConstants.UNLOCKED);
			// save method will update the record (primary key is available in entity
			userRepo.save(user);
			return true;
		} else {
			return false;
		}

	}

	@Override
	public Boolean forgotPassword(String emailId) throws UserAppException {
	
		UserAccountEntity entity = new UserAccountEntity();
		entity.setEmail(emailId);
		Example<UserAccountEntity> example = Example.of(entity);
		Optional<UserAccountEntity> findOne = userRepo.findOne(example);
		if (findOne.isPresent()) {
			UserAccountEntity userEntity = findOne.get();

			// :send email to user(reading email body to send to user as email

			String body = readForgotPwdEmailBody(userEntity);

			String subject = appProps.getMessages().get(AppConstants.RECOVER_PZZWD_EMAIL_SUB);

			try {
				emailUtils.sendEmail(userEntity.getEmail(), subject, body);
			} catch (Exception e) {
				logger.error(AppConstants.EXCEPTION_OCCURED +e.getMessage(),e);
				throw new UserAppException(e.getMessage());
			}

			return true;
		} else {
			return false;
		}

	}

	// creating email body to send to user for forgot password

	private String readForgotPwdEmailBody(UserAccountEntity entity) throws UserAppException{

		StringBuilder sb = new StringBuilder(AppConstants.EMPTY_STR);
		String mailBody = AppConstants.EMPTY_STR;
		String fileName = appProps.getMessages().get(AppConstants.RECOVER_PZZWD_EMAIL_BODY_FILE);
		
		try (FileReader fr = new FileReader(fileName)){
BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				
				line = br.readLine();
			}
			fr.close();
			br.close();
			
			String pazzword = entity.getPazzword();
			String decryptedPwd=PwdUtils.decryptMsg(pazzword);
			
		
			// converting buffer object to string
			mailBody = sb.toString();
			// we are replacing firstName,lastName,TEMP-PWD,EMAIL with
			// getFirstName,getLastName,getPazzword,getEmail
			// we are using string in below all lines bcz string is immutable
			mailBody = mailBody.replace(AppConstants.FNAME, entity.getFirstName());
			mailBody = mailBody.replace(AppConstants.LNAME, entity.getLastName());
			mailBody = mailBody.replace(AppConstants.PZZWD, decryptedPwd);
		} catch (Exception e) {
			logger.error(AppConstants.EXCEPTION_OCCURED +e.getMessage(),e);
			throw new UserAppException(e.getMessage());
		}
		
		return mailBody;
	}

	private static String generateRandomPazzword(int length) {

		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(AppConstants.CANDIDATE_CHARS.charAt(random.nextInt(AppConstants.CANDIDATE_CHARS.length())));
		}

		return sb.toString();

	}

	private String readUnlockAccEmailBody(UserAccountEntity entity) throws UserAppException {
		StringBuilder sb = new StringBuilder(AppConstants.EMPTY_STR);
		String mailBody = AppConstants.EMPTY_STR;
		String fileName = appProps.getMessages().get(AppConstants.UNLOCK_ACC_EMAIL_BODY_FILE);
		
		try  (FileReader fr = new FileReader(fileName)){
 BufferedReader br = new BufferedReader(fr);
			// read first line data
			String line = br.readLine();
			while (line != null) {// checks line!=null then append that line to string Buffer
				sb.append(line);
			
				line = br.readLine();// to read next line every time
			}
			fr.close();
			br.close();
			//decrypting the password
			String decryptedPwd=PwdUtils.decryptMsg(entity.getPazzword());
			mailBody = sb.toString();
			
			mailBody = mailBody.replace(AppConstants.FNAME, entity.getFirstName());
			mailBody = mailBody.replace(AppConstants.LNAME, entity.getLastName());
			mailBody = mailBody.replace(AppConstants.TEMP_PZZWD,decryptedPwd );
			mailBody = mailBody.replace(AppConstants.EMAIL, entity.getEmail());
		} catch (Exception e) {
			logger.error(AppConstants.EXCEPTION_OCCURED +e.getMessage(),e);
			throw new UserAppException(e.getMessage());
		}
		
		
		return mailBody;
	}


	}
