package com.hhplus.reservation_concert.global.config;

import com.hhplus.reservation_concert.application.TokenFacade;
import com.hhplus.reservation_concert.global.interceptor.LogInterceptor;
import com.hhplus.reservation_concert.global.interceptor.TokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TokenFacade tokenFacade;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**");

        registry.addInterceptor(new TokenInterceptor(tokenFacade))
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/tokens/**",
                        "/swagger-resources/**", "/webjars/**", "/v3/api-docs/**", "/swagger-ui/**");
    }
}
