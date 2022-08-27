package ARApi.Scaffold;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebMvc
@EnableSwagger2

@SpringBootApplication
// comment this in if you want to use it locally
//@ImportResource({"classpath:config.xml"})

@ComponentScan(basePackages = {"ARApi"})
public class DbFetcherApplication {
	public static void main(String[] args) {
		SpringApplication.run(DbFetcherApplication.class);
	}
}
