package com.taskmanager.app.configuration;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@EnableWebSecurity
public class AppConfiguration implements WebSocketMessageBrokerConfigurer {

    @Bean
    public ModelMapper getModelMapper() {
        return  new ModelMapper();
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topics");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers("/","/assets/**","*.css","*.js","*.html","/user")
                            .permitAll()
                            .anyRequest().authenticated();
                })
                .sessionManagement(session ->
                        session.
                        sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                .maximumSessions(1)
                                .maxSessionsPreventsLogin(true)
                )
                .sessionManagement(session ->
                        session.sessionFixation().migrateSession());

        httpSecurity.formLogin(login -> {
            login.loginPage("/user/login")
                    .permitAll();
        });

        httpSecurity.oauth2Login(oauthLogin -> {
            oauthLogin.loginPage("/user/login")
                    .defaultSuccessUrl("/app/dashboard")
                    .successHandler((request,response,authentication) -> {
                        log.info("authenticated via oauth2");
                        if(request.getUserPrincipal() != null)
                            System.out.println(request.getUserPrincipal().getName());
                        response.sendRedirect("/app/dashboard");
                    })
                    .failureUrl("/user/login?error=true")
                    .permitAll();
        });

        return httpSecurity.build();
    }
}
