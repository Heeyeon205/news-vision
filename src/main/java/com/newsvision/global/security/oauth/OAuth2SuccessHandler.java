package com.newsvision.global.security.oauth;

import com.newsvision.global.jwt.JwtTokenProvider;
import com.newsvision.global.jwt.RefreshTokenRepository;
import com.newsvision.user.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = oAuth2User.getUser();
        Long userId = user.getId();
        String username = user.getUsername();

        String accessToken = jwtTokenProvider.createToken(userId, username, user.getRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(username);
        refreshTokenRepository.save(username, refreshToken);

//        String redirectUrl = "http://localhost:5173/oauth2/redirect"
        String redirectUrl = "https://newsion.kro.kr/oauth2/redirect"
                + "?accessToken=" + accessToken
                + "&refreshToken=" + refreshToken
                + "&userId=" + user.getId()
                + "&nickname=" + user.getNickname()
                + "&image=" + user.getImage()
                + "&role=" + user.getRole().name();
        log.warn("access token: {}", accessToken);
        log.warn("refresh token: {}", refreshToken);
        response.sendRedirect(redirectUrl);
    }
}
