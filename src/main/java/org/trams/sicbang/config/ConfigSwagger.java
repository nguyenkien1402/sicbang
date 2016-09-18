package org.trams.sicbang.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by voncount on 5/9/16.
 */
@Configuration
@EnableSwagger2
public class ConfigSwagger {

//    @Profile("dev")
//    @Bean
//    public Docket docket_dev() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .paths(PathSelectors.ant("/api/**"))
//                .build().apiInfo(apiInfo()).host("14.63.219.203:5001");
//    }

    @Profile("prod")
    @Bean
    public Docket docket_prod() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.ant("/api/**"))
                .build().apiInfo(apiInfo());
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfo(
                "Sigbang API",
                "Test API for sigbang project",
                "1.0",
                "TOS",
                new Contact("Minh Dang Le", "https://tramskorea.atlassian.net/browse/SIC-10", "leminh9889@gmail.com"),
                "License",
                "http://www.bing.com");
    }

}
