package in.ashokit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="STATE_MASTER")
public class State {

	@Column(name="STATE_ID")
	private Integer state_Id;
	

	@Column(name="COUNTRY_ID")
	private Integer country_Id;
	

	@Column(name="STATE_NAME")
	private String state_Name;
}
