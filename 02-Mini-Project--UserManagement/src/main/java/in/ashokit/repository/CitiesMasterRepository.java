package in.ashokit.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ashokit.entity.CitiesMasterEntity;

public interface CitiesMasterRepository extends JpaRepository<CitiesMasterEntity, Serializable>{

	public List<CitiesMasterEntity> findBystateId(Integer state_id);
}
