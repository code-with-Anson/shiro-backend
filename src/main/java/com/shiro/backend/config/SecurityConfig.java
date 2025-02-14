package com.shiro.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // 使用这种方式更明确
                .authorizeRequests()
                .anyRequest().permitAll();
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 改为true，允许携带凭证
        configuration.setAllowCredentials(true);

        // 明确指定允许的域名，而不是使用通配符
        configuration.addAllowedOrigin("https://shiro.mikudesi.top");
        configuration.addAllowedOrigin("http://localhost:5173");  // 添加前端开发服务器地址
        configuration.addAllowedOrigin("http://192.168.31.58:5173");  // 添加前端开发服务器地址

        configuration.addAllowedOrigin("http://106.14.59.65:15200");  // 添加前端开发服务器地址

        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("Authorization");

        // 如果需要允许多个域名，可以这样添加
        // configuration.addAllowedOrigin("https://其他域名.com");

        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
