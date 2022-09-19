package in.ashokit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="COUNTRY_MASTER")
public class Country {
	@GeneratedValue
	@Column(name="COUNTRY_ID")
	private  Integer country_Id;
	
	@Column(name="COUNTRY_CODE")
	private String country_Code;
	
	@Column(name="COUNTRY_NAME")
	private String country_Name;

}
