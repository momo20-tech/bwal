package com.example.backdorm.cofig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:8081"); // 允许的前端地址
        // 允许的请求方法（*表示所有：GET/POST/PUT/DELETE等）
        corsConfiguration.addAllowedHeader("*");

        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        //配置URL匹配规则（/**表示所有接口都允许跨域）
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}