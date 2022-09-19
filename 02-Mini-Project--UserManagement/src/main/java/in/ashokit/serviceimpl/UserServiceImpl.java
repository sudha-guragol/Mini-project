package in.ashokit.serviceimpl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import in.ashokit.bindings.LoginForm;
import in.ashokit.bindings.UnlockAccountForm;
import in.ashokit.bindings.UserRegForm;
import in.ashokit.constants.AppConstants;
import in.ashokit.entity.CitiesMasterEntity;
import in.ashokit.entity.CountryMasterEntity;
import in.ashokit.entity.StateMasterEntity;
import in.ashokit.entity.UserAccountEntity;
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
	public String LoginCheck(LoginForm loginForm) {

		// step-1 check given credentials are valid or not

		
		// String msg=AppConstants.EMPTY_STR; OR
		String msg = "";// (local variable we need to declare the variable first before v use &
						// initilise based on condition)
		String encryptedPwd=null;
		try
		{
			 encryptedPwd = PwdUtils.encryptMsg(loginForm.getPazzword());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		UserAccountEntity user = userRepo.findByEmailAndPazzword(loginForm.getEmial(), encryptedPwd);
		if (user != null) {
			// credentilas are valid
			// lock the account
			String acc_Status = user.getAcc_Status();

			/*
			 * line 59 may through null poinetr exception bcz acc status may have null
			 * values,so it is preferable to write (when we are comparing the strings
			 * ,always the string that you want to compare should be on left side
			 * 
			 * if(acc_Status.equals(AppConstants.LOCKED))
			 */

			if (AppConstants.LOCKED.equals(acc_Status))// will not through null pointer exception (constant having
														// locked has some value initially)

			{
				msg = appProps.getMessages().get(AppConstants.ACCOUNT_LOCKED);
			} else {
				msg = AppConstants.SUCCESS;
			}
		} else {
			msg = "Invalid Credentials";// not recommended to hard code value in java class(not recommended to use
										// string literal)(if v use this in future need to change the code v need to
										// compile and deploy the project
			msg = appProps.getMessages().get(AppConstants.INVALID_CREDENTIALS);

		}

		// step-2 if credentials are valid ,check account status (locked or unlocked)

		// step 3 unlocked account user should be able to login to application
		return msg;
	}

	@Override
	public Map<Integer, String> getCountries() {
		List<CountryMasterEntity> countries = countryRepo.findAll();
		Map<Integer, String> countryMap = new HashMap<>();
		countries.forEach(country -> {
			countryMap.put(country.getCountryId(), country.getCountryName());
		});
		return countryMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer countryId) {

		List<StateMasterEntity> states = stateRepo.findByCountryId(countryId);
		Map<Integer, String> stateMap = new HashMap<>();
		states.forEach(state -> {
			stateMap.put(state.getStateId(), state.getStateName());
		});
		return stateMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer stateId) {
		List<CitiesMasterEntity> cities = cityRepo.findBystateId(stateId);
		Map<Integer, String> cityMap = new HashMap<>();
		cities.forEach(city -> {
			cityMap.put(city.getCityId(), city.getCityName());
		});
		return cityMap;
	}

	@Override
	public String isEmailUnique(String emailId) {
		UserAccountEntity entity = new UserAccountEntity();
		entity.setEmail(emailId.trim());
		// example is used to filetr the data(it use the data that is in entity to
		// retrieve) constrruct the query based on value passed
		// we can use query by example or findby method to check emailid
		Example<UserAccountEntity> example = Example.of(entity);
		// Optional used to avoid null pointer exception
		// findOne
		Optional<UserAccountEntity> findOne = userRepo.findOne(example);
		if (findOne.isPresent()) {
			return AppConstants.DUPLICATE;

		} else {
			return AppConstants.UNIQUE;

		}
	}

	@Override
	public Boolean saveUser(UserRegForm userForm) throws Exception {
		// data is in userForm obj ..we need to copy the this data to entity obj to save
		// to db
		UserAccountEntity entity = new UserAccountEntity();
		// we can copy data for that variables and data types in target class and source
		// class should match(for matching proprty it will copy the data from source to
		// target for unmatched that will be ignored
		BeanUtils.copyProperties(userForm, entity);
		// default acc is LOCKED
		entity.setAcc_Status(AppConstants.LOCKED);
		
		
		String randomPwd=generateRandomPazzword(6);
		System.out.println("temp[ pwd ::" +randomPwd);
		// password Encryption
		String encryptedPwd;
		try {
			encryptedPwd = PwdUtils.encryptMsg(randomPwd);
			entity.setPazzword(encryptedPwd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		

	
		// for save method we supposed to save entity not form binding obj
		entity = userRepo.save(entity);
		String emailBody = readUnlockAccEmailBody(entity);
		String subject = appProps.getMessages().get(AppConstants.UNLOCK_ACC_EMAIL_SUB);

		// TODO :email functionality

		Boolean status = emailUtils.sendEmail(userForm.getEmail(), subject, emailBody);

		return entity.getUserId() != null ? true : false;
	}

	@Override
	public Boolean unlockAccount(UnlockAccountForm unlockAccForm) {
		String email = unlockAccForm.getEmail();
		String tempPasswrd = unlockAccForm.getTemporaryPassword();
		String encryptedPwd=null;
	try {
		encryptedPwd=PwdUtils.encryptMsg(tempPasswrd);
	} catch (Exception e) {
		e.printStackTrace();
	}
		UserAccountEntity user = userRepo.findByEmailAndPazzword(email, encryptedPwd);
		if (user != null) {
			
			String newPwd = unlockAccForm.getNewPassword();
			String encryptedNewPwd =null;
			try {
				encryptedNewPwd = PwdUtils.encryptMsg(newPwd);
				user.setPazzword(encryptedNewPwd);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			user.setAcc_Status(AppConstants.UNLOCKED);
			// save method will update the record (primary key is available in entity
			userRepo.save(user);
			return true;
		} else {
			return false;
		}

	}

	@Override
	public Boolean ForgotPassword(String emailId) throws Exception {
		/*
		 * replacement for below 4 lines of boiler plate code
		 * Optional<UserAccountEntity> findOne = getUserByEmail(emailId);
		 * 
		 * select below 4 lines right click -refactor-Extract Method --create method
		 * name:getEmailById
		 */
		UserAccountEntity entity = new UserAccountEntity();
		entity.setEmail(emailId);
		Example<UserAccountEntity> example = Example.of(entity);
		Optional<UserAccountEntity> findOne = userRepo.findOne(example);
		if (findOne.isPresent()) {
			UserAccountEntity userEntity = findOne.get();

			// :send email to user(reading email body to send to user as email

			String body = readForgotPwdEmailBody(userEntity);

			String subject = appProps.getMessages().get(AppConstants.RECOVER_PWD_EMAIL_SUB);

			emailUtils.sendEmail(userEntity.getEmail(), subject, body);

			return true;
		} else {
			return false;
		}

	}

	// creating email body to send to user for forgot password

	private String readForgotPwdEmailBody(UserAccountEntity entity) {

		StringBuffer sb = new StringBuffer(AppConstants.EMPTY_STR);
		String mailBody = AppConstants.EMPTY_STR;
		try {

			String fileName = appProps.getMessages().get(AppConstants.RECOVER_PWD_EMAIL_BODY_FILE);
			FileReader fileReader = new FileReader(fileName);

			BufferedReader br = new BufferedReader(fileReader);
			// read first line data
			String line = br.readLine();
			while (line != null) {// checks line!=null then append that line to string Buffer
				sb.append(line);
				// after first line is read replacing the line variable value with sec line,if
				// sec line not null that would be added to buffer so on
				// if first line is not null read next line and append to sb again till null
				// line comes
				line = br.readLine();// to read next line every time
			}
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
			mailBody = mailBody.replace(AppConstants.PWD, decryptedPwd);
			//smailBody = mailBody.replace(AppConstants.EMAIL, entity.getEmail());
		} catch (Exception e) {
			e.printStackTrace();
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

	private String readUnlockAccEmailBody(UserAccountEntity entity) {
		StringBuffer sb = new StringBuffer(AppConstants.EMPTY_STR);
		String mailBody = AppConstants.EMPTY_STR;
		try {

			String fileName = appProps.getMessages().get(AppConstants.UNLOCK_ACC_EMAIL_BODY_FILE);
			FileReader fileReader = new FileReader(fileName);

			BufferedReader br = new BufferedReader(fileReader);
			// read first line data
			String line = br.readLine();
			while (line != null) {// checks line!=null then append that line to string Buffer
				sb.append(line);
				// after first line is read replacing the line variable value with sec line,if
				// sec line not null that would be added to buffer so on
				// if first line is not null read next line and append to sb again till null
				// line comes
				line = br.readLine();// to read next line every time
			}
			br.close();
			//decrypting the password
			String decryptedPwd=PwdUtils.decryptMsg(entity.getPazzword());
			// converting buffer object to string
			mailBody = sb.toString();
			// we are replacing firstName,lastName,TEMP-PWD,EMAIL with
			// getFirstName,getLastName,getPazzword,getEmail
			// we are using string in below all lines bcz string is immutable
			mailBody = mailBody.replace(AppConstants.FNAME, entity.getFirstName());
			mailBody = mailBody.replace(AppConstants.LNAME, entity.getLastName());
			mailBody = mailBody.replace(AppConstants.TEMP_PWD,decryptedPwd );
			mailBody = mailBody.replace(AppConstants.EMAIL, entity.getEmail());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mailBody;
	}
}