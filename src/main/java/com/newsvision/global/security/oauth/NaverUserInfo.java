package com.newsvision.global.security.oauth;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {
    private final Map<String, Object> response;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.response = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return (String) response.get("id");
    }

    @Override
    public String getEmail() {
        return (String) response.get("email");
    }

    @Override
    public String getName() {
        return (String) response.get("name");
    }
}
