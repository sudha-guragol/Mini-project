package in.ashokit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	
	//creating docket object,(purpose of it is to build the documentation for the API
	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("public-api")
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("in.ashokit.restcontrollers"))
				.paths(PathSelectors.any())
				.build();
	}

	
//company information
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("JavaInUse API")
				.description("Ashok IT API reference for developers")
				.termsOfServiceUrl("http://www.ashokit.com")
				.contact(new Contact("Ashok IT", "www.ashokit.in", "ashokitschool@gmail.com"))
				.license("Ashok IT License")
				.licenseUrl("ashokitschool@gmail.com")
				.version("1.0")
				.build();
	}

}
