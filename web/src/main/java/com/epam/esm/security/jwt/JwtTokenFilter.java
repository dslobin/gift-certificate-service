package com.epam.esm.security.jwt;

import com.epam.esm.advice.ControllerExceptionHandler;
import com.epam.esm.advice.ErrorResponse;
import com.epam.esm.advice.ResourceCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * JWT token filter that handles all HTTP requests to application.
 */
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String token = resolveToken(request);
            setAuthentication(token);
            filterChain.doFilter(request, response);
        } catch (JwtAuthenticationException e) {
            String jsonErrorResponse = createErrorResponse(e.getMessage(), request.getRequestURI());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(jsonErrorResponse);
        }
    }

    private String createErrorResponse(String errorMessage, String requestPath)
            throws JsonProcessingException {
        int errorCode = ControllerExceptionHandler.getErrorCode(HttpStatus.UNAUTHORIZED, ResourceCode.USER_ACCOUNT);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(ZonedDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(errorMessage)
                .errorCode(errorCode)
                .path(requestPath)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(errorResponse);
    }

    private void setAuthentication(String token)
            throws JwtAuthenticationException {
        if (!StringUtils.isEmpty(token) && jwtTokenProvider.validateToken(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return bearerToken;
    }
}
