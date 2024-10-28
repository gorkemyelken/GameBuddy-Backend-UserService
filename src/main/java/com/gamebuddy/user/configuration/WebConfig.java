package com.gamebuddy.user.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Tüm endpoint'ler için CORS'u etkinleştir
                .allowedOrigins("http://localhost:3000", "https://production-url.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // İzin verilen HTTP yöntemleri
                .allowedHeaders("*") // İzin verilen başlıklar
                .allowCredentials(true); // Kimlik bilgilerini kullanmaya izin ver
    }
}

