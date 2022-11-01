package com.cosmost.project.cosmost.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.Arrays;

@Configuration
public class CorsConfiguration {

    // 프론트와 Axios 연결 시, CrossOrigin 뜰 때, filter에 추가하여 사용함.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
        config.setExposedHeaders(Arrays.asList("Authorization")); // 리액트에서 헤더받을 수 있게 설정
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // AllowCredentials이 true일 때 AllowedOrigin 사용못하므로 AllowedOriginPattern으로 대체
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}