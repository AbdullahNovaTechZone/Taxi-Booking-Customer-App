package com.novatechzone.config;

import com.novatechzone.dto.RequestMetaDTO;
import com.novatechzone.util.JwtUtils;
import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    JwtUtils jwtUtils;
    RequestMetaDTO requestMetaDTO;

    public JwtInterceptor(RequestMetaDTO requestMetaDTO) {
        this.requestMetaDTO = requestMetaDTO;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String auth = request.getHeader("authorization");
        System.out.println(request.getRequestURI());
        if (
                !(
                        request.getRequestURI().contains("customer/auth/")
                )
        ) {
            Claims claims = jwtUtils.verifyToken(auth);

            requestMetaDTO.setCustomerId(Integer.parseInt(claims.getIssuer()));
            requestMetaDTO.setName(claims.get("username").toString());
            requestMetaDTO.setMobile(claims.get("mobile").toString());
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
