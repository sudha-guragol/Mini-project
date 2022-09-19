package in.ashokit.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ashokit.entity.StateMasterEntity;

public interface StatesMasterRepository extends JpaRepository<StateMasterEntity,Serializable>{

	
	public List<StateMasterEntity> findByCountryId(Integer countryId);
}
 