package in.ashokit.service;


import java.util.List;

import in.ashokit.entity.Contact;

public interface ContactService {
	public Boolean saveContact(Contact contact);
	public List<Contact> getAllContacts();
	public Contact  getContactById(Integer contactId);
	public Boolean deleteContact(Integer contactId);
	

}
 