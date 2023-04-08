package com.example.myhome.home.configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;
    @Value("${prefix}")
    private String prefix;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/img/**")
                .addResourceLocations(prefix + uploadPath + "/", prefix + uploadPath + "/img/");
        registry
                .addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry
                .addResourceHandler("/files/**")
                .addResourceLocations("classpath:/static/files/");
    }
}
