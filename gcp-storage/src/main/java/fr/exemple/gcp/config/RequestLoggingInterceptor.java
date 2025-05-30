package fr.exemple.gcp.config;

import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {

            logger.info("Request to CommandeController - Method: {} - URI: {} - HTTP Method: {}",
                    handlerMethod.getMethod().getName(),
                    request.getRequestURI(),
                    request.getMethod());

            // Log headers
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                logger.debug("Header: {} = {}", headerName, request.getHeader(headerName));
            }
        }
        return true;
    }

    @Override
    public void postHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, ModelAndView modelAndView) {
        if (handler instanceof HandlerMethod handlerMethod) {

            logger.info("Response from CommandeController - Method: {} - URI: {} - Status: {}",
                    handlerMethod.getMethod().getName(),
                    request.getRequestURI(),
                    response.getStatus());

        }
    }
}
