package in.ashokit.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ashokit.entity.UserAccountEntity;

public interface UserAccountRepository extends JpaRepository<UserAccountEntity, Serializable>{
//data jpa will construct query for this method
	public UserAccountEntity findByEmailAndPazzword(String email,String pazzword);
}
