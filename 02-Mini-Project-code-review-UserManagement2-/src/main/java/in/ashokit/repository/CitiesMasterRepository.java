package in.ashokit.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ashokit.entity.CityMasterEntity;

public interface CitiesMasterRepository extends JpaRepository<CityMasterEntity, Serializable>{

	public List<CityMasterEntity> findBystateId(Integer stateId);
}
