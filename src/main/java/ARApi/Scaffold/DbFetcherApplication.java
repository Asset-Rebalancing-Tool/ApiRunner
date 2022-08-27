package ARApi.Scaffold;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebMvc
@EnableSwagger2

@SpringBootApplication
@ImportResource({"classpath:config.xml"})

@ComponentScan(basePackages = {"ARApi"})
public class DbFetcherApplication {
	public static void main(String[] args) {
		SpringApplication.run(DbFetcherApplication.class);
	}
}
