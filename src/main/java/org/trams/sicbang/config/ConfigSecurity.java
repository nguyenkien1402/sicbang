package org.trams.sicbang.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.trams.sicbang.service.security.*;

/**
 * Created by voncount on 4/5/16.
 */
@Configuration
public class ConfigSecurity extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private RestAuthenticationEntryPoint entryPoint;
    @Autowired
    private RestAuthenticationSuccessHandler successHandler;
    @Autowired
    private RestAuthenticationFailureHandler failureHandler;
    @Autowired
    private RestUserDetailService userDetailService;
    @Autowired
    private RestAuthenticationLogoutSuccessHandler logoutSuccessHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**");
        web.ignoring().antMatchers("/public/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("call login config");
        http
            .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(entryPoint)
                .and()
            .authorizeRequests()
                .antMatchers("/admin").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/member/**").authenticated()
                .anyRequest().permitAll()
                .and()
            .rememberMe().rememberMeParameter("rememberme").key("SIGBANG").tokenValiditySeconds(604800)
                .and()
            .formLogin()
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
            .logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
    }

}