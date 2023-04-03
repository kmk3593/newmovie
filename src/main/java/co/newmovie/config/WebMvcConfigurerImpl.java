package co.newmovie.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfigurerImpl implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 요청에 대해서
                .allowedOrigins("*") // 허용할 오리진들 전체로 설정함
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.HEAD.name(), HttpMethod.POST.name(),
                        HttpMethod.PUT.name(), HttpMethod.DELETE.name());
    }
}
