package vn.be.platform_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.be.platform_service.constant.ApiConstant;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtFilter() {
        FilterRegistrationBean<JwtAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtAuthFilter);
        registrationBean.addUrlPatterns(ApiConstant.API_PREFIX + "/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
