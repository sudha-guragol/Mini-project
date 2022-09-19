package in.ashokit.serviceImpl;

import java.util.List;
import java.util.Optional;

import in.ashokit.entity.Contact;
import in.ashokit.repository.ContactRepository;
import in.ashokit.service.ContactService;

public class ContactServiceImpl implements ContactService {
	
	
	private ContactRepository contactRepo;
	//constructor injection
	public ContactServiceImpl(ContactRepository contactRepo) {
		this.contactRepo=contactRepo;
	}

	@Override
	public Boolean saveContact(Contact contact) {
		
		Contact savedEntity= contactRepo.save(contact);
		if(savedEntity!=null && savedEntity.getContactId()!=null)
		{
			return true;
		}
		return false;
	}

	@Override
	public List<Contact> getAllContacts() {
		List<Contact> contacts = contactRepo.findAll();
		return contacts;
	}

	@Override
	public Contact getContactById(Integer contactId) {
		Optional<Contact> findById = contactRepo.findById(contactId);
		if(findById.isPresent())
		
		{
			return findById.get();
		}
		return null;
	}

	@Override
	public Boolean deleteContact(Integer contactId) {
		
		boolean existsById = contactRepo.existsById(contactId);
		if(existsById)
		{
			contactRepo.deleteById(contactId);
			return true;
		}
		
		
		return null;
	}

}
