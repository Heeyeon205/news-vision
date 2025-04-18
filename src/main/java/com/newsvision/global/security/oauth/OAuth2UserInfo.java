package com.newsvision.global.security.oauth;

public interface OAuth2UserInfo {
    String getProvider();   // google, naver, kakao
    String getProviderId(); // sub      id
    String getEmail();
    String getName();
}
