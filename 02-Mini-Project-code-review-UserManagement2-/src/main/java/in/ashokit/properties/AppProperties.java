package in.ashokit.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix="app")
@EnableConfigurationProperties
public class AppProperties {
	//messages  variable read datab from yml file(meaages variable has same name as in yml file messages)
	private  Map<String,String> messages=new HashMap<>();

}
