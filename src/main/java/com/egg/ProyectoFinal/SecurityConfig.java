
package com.egg.ProyectoFinal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
//@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    
    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception{
        
        http
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/css/**","/img/**","/js/**","/landing", "/usuario/**", "/login/**",
                            "/registro/**", "/registration_success/**", "/verify/**", 
                            "/successful_verification/**", "/verification_failed/**").permitAll()
                   // .antMatchers("/**").permitAll()
                   .antMatchers("/**").authenticated()
                .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/gastos",true)
                .and()
                .logout()
                     .logoutSuccessUrl("/login?logout")
                .and()
                    .csrf().disable();
    }
}
