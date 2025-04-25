package com.newsvision.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class EmailVerification {
    @Id
    private String email;
    private String code;
    private LocalDateTime expiredAt;

    public EmailVerification(String email, String code, int minutesToLive) {
        this.email = email;
        this.code = code;
        this.expiredAt = LocalDateTime.now().plusMinutes(minutesToLive);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }

    public boolean matchCode(String input) {
        return this.code.equals(input);
    }
}
