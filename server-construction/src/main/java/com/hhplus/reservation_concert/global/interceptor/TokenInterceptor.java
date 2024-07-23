package com.hhplus.reservation_concert.global.interceptor;

import com.hhplus.reservation_concert.application.TokenFacade;
import com.hhplus.reservation_concert.domain.token.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {

    private final TokenFacade tokenFacade;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String xToken = request.getHeader("X-TOKEN");

        log.info("토큰 체크 인터셉터 실행 {}", requestURI);
        Token token = tokenFacade.validate(xToken);
        request.setAttribute("token", token);

        return true;
    }
}
