package com.example.myhome.home.configuration;
import com.example.myhome.home.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.resource.PathResourceResolver;
import software.amazon.awssdk.services.s3.internal.resource.S3Resource;
import software.amazon.awssdk.services.s3.S3Client;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;
    @Value("${prefix}")
    private String prefix;

    @Value("${aws.bucket.name}")
    private String awsBucket;
    @Autowired
    OwnerService ownerService;
    //for saving on AWS S3

//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        System.out.println("CONFIG UPLOAD PATH " + uploadPath);
//        System.out.println("CONFIG PREFIX " + prefix);
//        registry
//
//                .addResourceHandler("/myhomecab/img/**", "/img/**", "img/**")
//                .addResourceLocations("https://" + awsBucket + ".s3.amazonaws.com/img/")
////                .setCachePeriod(3600)
////                .resourceChain(true)
////                .addResolver(new PathResourceResolver() {
////                    @Override
////                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
////                        // Вывод информации о перенаправлениях в консоль
////                        System.out.println("Resource Path: " + resourcePath);
////                        System.out.println("Location: " + location.getURL());
////                        System.out.println("full addr " + location.getURL() + resourcePath);
////                        System.out.println("orig addr https://myhome24.s3.eu-north-1.amazonaws.com/img/ownerId/6da09be3-a0f5-4915-8232-4759d736da3c-photo-1554080353-a576cf803bda.jpg");
////
////                        return super.getResource(resourcePath, location);
////                    }
////                })
//                ;
//                registry
//                .addResourceHandler( "/myhomecab/images/**", "/images/**")
//                .addResourceLocations("classpath:/static/", "classpath:/static/images/");
//        registry
//                .addResourceHandler("/**")
//                .addResourceLocations("classpath:/static/");
//           }


    //for local saving
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("CONFIG UPLOAD PATH " + uploadPath);
        System.out.println("CONFIG PREFIX " + prefix);
        registry
                .addResourceHandler("/img/**", "/images/**")
                .addResourceLocations(prefix + uploadPath + "/", prefix + uploadPath + "/img/");
        registry
                .addResourceHandler("/myhomecab/**", "/myhomecab/images/**", "/images/**")
                .addResourceLocations("classpath:/static/", "classpath:/static/images/");
        registry
                .addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry
                .addResourceHandler("/files/**")
                .addResourceLocations(prefix + uploadPath + "/", prefix + uploadPath + "/files/");
    }






//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(new StringToApartmentDTOConverter());
//        registry.addConverter(new StringToBuildingDTOConverter());
//        registry.addConverter(new StringToAccountDTOConverter());
//        registry.addConverter(new StringToOwnerDTOConverter());
////        registry.addConverter(new StringToUnitConverter());
//        registry.addConverter(new AccountDTOToStringConverter());
//        registry.addConverter(new OwnerDTOToStringConverter());
//        registry.addConverter(new AdminToStringConverter());
//        registry.addConverter(new RoleToStringConverter());
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        registry.addInterceptor(interceptor);
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(new Locale("uk", "UA"));
        return resolver;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}