package in.ashokit.entity;

import java.time.LocalDate;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;


@Entity
@Table(name="CONTACT_DTLS")
@Data
public class Contact {

	@Id
	@GeneratedValue
	@Column(name="CONTACT_ID")
	private Integer contactId;
	
	@Column(name="CONTACT_NAME")
	private String contactName;
	
	@Column(name="CONTACT_NUMBER")
	private Integer contactNumber;
	
	@Column(name="CONTACT_EMAIL")
	private String contactEmail;
	
	@CreationTimestamp
	@Column(name="CREATED_DATE",updatable=false)
	private LocalDate createdDate;
	
	@UpdateTimestamp
	@Column(name="UPDATE_DATE",insertable=false)
	private LocalDate updatedDate;
	
}
 