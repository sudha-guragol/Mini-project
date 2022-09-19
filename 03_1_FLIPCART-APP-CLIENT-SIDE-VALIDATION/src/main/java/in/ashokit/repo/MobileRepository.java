package in.ashokit.repo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.ashokit.entity.MobileEntity;

public interface MobileRepository extends JpaRepository<MobileEntity, Serializable> {

	
	//fetch distinct brand mobiles(gives unique brand mobiles from db)
	@Query("select distinct(brand) from MobileEntity")
	public List<String>findBrandNames();
	
	//fetch distinct brand mobiles(gives unique brand mobiles from db)
	@Query("select distinct (ram) from MobileEntity")
	public List<Integer> findRams();
	
	
	//custom queries
	@Query("select distinct (rating) from MobileEntity")
	public List<Integer> findRatings();
}
