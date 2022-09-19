package in.ashokit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name="MOBILES")
public class MobileEntity {

	@Id
	@Column(name="MOBILE_ID")
	private Integer mobileId;
	
	@Column(name="BRAND_NAME")
	private String brand;
	

	@Column(name="MOBILE_PRICE")
	private Double price;
	

	@Column(name="MOBILE_RAM")
	private Integer ram;
	

	@Column(name="MOBILE_RATING")
	private Integer rating;
}
