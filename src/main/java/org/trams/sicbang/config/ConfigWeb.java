package org.trams.sicbang.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.trams.sicbang.filter.AuthenticationFilter;
import org.trams.sicbang.interceptor.DebugInterceptor;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Locale;

/**
 * Created by voncount on 4/6/16.
 */
@Configuration
@EnableSwagger2
@EnableCaching
public class ConfigWeb extends WebMvcConfigurerAdapter {

    @Autowired
    Environment env;
    @Autowired
    DebugInterceptor debugInterceptor;

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Bean
    public SessionLocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        Locale defaultLocale = Locale.KOREA;
        localeResolver.setDefaultLocale(defaultLocale);
        return localeResolver;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setCacheSeconds(-1);
        return messageSource;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(Integer.parseInt(env.getProperty("cache.time")));
        registry
                .addResourceHandler("/public/**")

                .addResourceLocations("file:/data/sicbang/public/")
                .setCachePeriod(Integer.parseInt(env.getProperty("cache.time")));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(debugInterceptor);
    }

    @Bean
    FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AuthenticationFilter());
        registration.addUrlPatterns("/api/auth/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("authenticationFilter");
        return registration;
    }

}
