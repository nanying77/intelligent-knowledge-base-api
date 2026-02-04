package com.bujian.aipersnonknowledge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 授权配置
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 允许所有请求
                )
                // 关闭 CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // 关闭 CORS（如果需要可以单独配置）
                .cors(AbstractHttpConfigurer::disable)
                // 禁用默认登录页和HTTP Basic认证
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // 禁用匿名认证（可选）
                .anonymous(AbstractHttpConfigurer::disable);

        return http.build();
    }
}