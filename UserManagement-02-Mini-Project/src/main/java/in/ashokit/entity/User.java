package in.ashokit.entity;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import lombok.Data;

@Entity
@Data
@Table(name="USER_ACCOUNTS")
public class User {
	
	@Column(name="USER_ID")
	private Integer user_Id;
	
	@Column(name="FIRST_NAME")
	private String first_Name;
	
	@Column(name="LAST_NAME")
	private String last_Name;
	
	@Column(name="USER_EMAIL")
	private String email;
	
	
	@Column(name="USER_MOBILE")
	private Long phone_No;
	
	@Column(name="DOB")
	private Date dob;
	
	@Column(name="GENDER")
	private String gender;
	
	@Column(name="COUNTRY_ID")
	private String country;
	
	@Column(name="STATE_ID")
	private String state;
	
	@Column(name="CITY_ID")
	private String city;
	
	@Column(name="ACC_STATUS")
	private String acc_Status;
	
	@Column(name="USER_PWD")
	private String password;
	
	@CreationTimestamp
	@Column(name="CREATED_DATE" ,updatable=false)
	private LocalDate created_Date;
	
	@UpdateTimestamp
	@Column(name="UPDATED_DATE" ,insertable=false)
	private LocalDate updated_Date;
	
	
	
}
