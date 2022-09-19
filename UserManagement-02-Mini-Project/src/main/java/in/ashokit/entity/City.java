package in.ashokit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="CITY_MASTER")
public class City {

	@Column(name="CITY_ID")
	private Integer city_Id;
	
	@Column(name="STATE_ID")
	private Integer state_Id;
	
	@Column(name="CITY_NAME")
	private String city_Name;
}
