package com.newsvision.global.jwt;

import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;

public class JwtUtil {
    public static String extractToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.split(" ")[1];
        }
        throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
    }
}
