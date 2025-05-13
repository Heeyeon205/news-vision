package com.newsvision.global.jwt;

import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.global.security.CustomUserDetailsService;
import com.newsvision.global.redis.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.split(" ")[1];

            if (jwtTokenProvider.validateToken(token)) {
                log.info("유효한 토큰입니다.");

                if (tokenBlacklistService.isBlacklisted(token)) {
                    filterChain.doFilter(request, response);
                    response.getWriter().write("해당 토큰은 로그아웃 처리되었습니다.");
                    return;
                }

                String username = jwtTokenProvider.getUsername(token);
                CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);

                if(userDetails.getUser().getIsDeleted()) {
                    response.setStatus(401);
                    response.getWriter().write("Deactivated account");
                    return;
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
