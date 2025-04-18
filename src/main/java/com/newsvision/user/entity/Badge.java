package com.newsvision.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "badges")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String icon;
    private String title;
    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        ROLE_ADMIN, ROLE_CREATOR
    }
}
