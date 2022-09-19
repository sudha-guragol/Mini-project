package in.ashokit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import in.ashokit.bindings.SearchForm;
import in.ashokit.entity.MobileEntity;
import in.ashokit.repo.MobileRepository;

@Service
public class MobileService {
	
	@Autowired
	private MobileRepository mobileRepo;
	
	public List<MobileEntity> fetchAllMobiles()
	{
		return mobileRepo.findAll();
	}

	//gives unique brand names
	public List<String> getBrandNames()
	{
		return mobileRepo.findBrandNames();
	}
	
	//gives unique rams
	
public List<Integer> getRams()
{
	return mobileRepo.findRams();
}

public List<Integer> getRatings()
{
	return mobileRepo.findRatings();
}

public Page<MobileEntity> filterProducts(SearchForm formObj,Integer pageNo,Integer pageSize)
{
	
	MobileEntity entity=new MobileEntity();
	//BeanUtils.copyProperties(formObj, entity);
	
	
	//if brand is empty dont set it(if brand is not null then only set the brand value)
	//if brand is not equal to null and not empty then only set it to formObj
	if(null!=formObj.getBrand() && ! "".equals(formObj.getBrand()))
	{
	entity.setBrand(formObj.getBrand());
	}
	entity.setRam(formObj.getRam());
	entity.setRating(formObj.getRating());
	
	//price is not selecting in the query bcz we have not set the data 
    Example<MobileEntity> example = Example.of(entity);
    
  //creating page request object
  	PageRequest page = PageRequest.of(pageNo, pageSize);
	Page<MobileEntity> findAll = mobileRepo.findAll(example,page);
	 /*
	 //if price not equal to null execute it else dispaly all records findAll
	 if(null !=formObj.getPrice())
	 {
	return filterMobileByPrice(findAll,formObj.getPrice());
	 }*/
	 return findAll;
	 
}

//filtering products whose price is less than price given in UIs
private List<MobileEntity> filterMobileByPrice(List<MobileEntity> mobiles,Double price) {
	
	
	List<MobileEntity> filteredList=new ArrayList<>();
	mobiles.forEach(mobile -> {
		if(mobile.getPrice()<price)
		{
			filteredList.add(mobile);
		}
	});
	return filteredList;
}

}
