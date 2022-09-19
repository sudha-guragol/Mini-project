package in.ashokit.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import in.ashokit.bindings.SearchForm;
import in.ashokit.entity.MobileEntity;
import in.ashokit.service.MobileService;

@Controller
public class MobilesController {
	
	@Autowired
private MobileService service;
	//below annotation indicate it will load everytime when any methods of this controller class is called
	@ModelAttribute
	public void loadFormData(Model model) {
		model.addAttribute("brands", service.getBrandNames());
		model.addAttribute("rams",service.getRams());
		model.addAttribute("ratings",service.getRatings());
	}
	@GetMapping("/loadPage")
	public String loadPage(Model model)
	{
		//sending form binding object
		SearchForm formObj=new SearchForm();
		model.addAttribute("formObj" ,formObj);
		return "index";
	}
	
	
	@PostMapping("/searchMobiles")
	public String searchMobiles(@ModelAttribute("formObj") SearchForm formObj,HttpServletRequest request,Model model )
	{
		//System.out.println(formObj);
		//System.out.println(request.getParameter("pn"));
		
		Integer pageNo=1;
		Integer pageSize=3;
		
		String pn = request.getParameter("pn");
		//checking pageno should not be null and not empty(works for pageno other than 1
		//page no will be replacing by page no given by ui ,page no clicked by user(button is mandatory,with hyperlink we cant send post request)
		if(null != pn && !"".equals(pn))
		{
			pageNo = Integer.parseInt(pn);
		}
		
		Page<MobileEntity> page = service.filterProducts(formObj,pageNo,pageSize);
		
		int totalPages = page.getTotalPages();
		//we can get page contents
		model.addAttribute("mobiles",page.getContent());
		model.addAttribute("tp",totalPages);
		return "index";
	}
}
