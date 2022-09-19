package in.ashokit.bindings;

import lombok.Data;

//for search v need only 4 field (no id field required)
@Data
public class SearchForm {

	private String brand;
	
	private Double price;

	private Integer ram;

	private Integer rating;
}
