package com.br.norteck.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                //.httpBasic(Customizer.withDefaults())
                //.formLogin(Customizer.withDefaults())
                //define que a pagina configurada na rota /login do WebConfiguration seja a pagina de login
                .formLogin(configurer ->{
                    configurer.loginPage("/login").permitAll();
                })
                .authorizeHttpRequests(authorize ->{
                    authorize.anyRequest().authenticated();
                })
                .build();
    }
}
