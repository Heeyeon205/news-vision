package com.newsvision.user.service;

import com.newsvision.global.auth.AuthService;
import com.newsvision.global.aws.FileUploaderService;
import com.newsvision.global.jwt.JwtTokenProvider;
import com.newsvision.user.dto.request.JoinUserRequest;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private FileUploaderService fileUploaderService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthService authService;

    @Mock
    private BadgeService badgeService;

    private String defaultImage = "https://newsion-project.s3.ap-northeast-2.amazonaws.com/profiles/1/31a5bf19-fb86-45c5-9345-a330c2e30f21l";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "defaultProfileImage", defaultImage);
    }

    @Test
    void 회원가입_성공() {
        // given
        JoinUserRequest request = new JoinUserRequest("testId", "testPw", "test@test.com");
        String encodedPassword = "testEncodedPassword";

        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);

        // when
        userService.save(request);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals(request.getUsername(), savedUser.getUsername());
        assertEquals(encodedPassword, savedUser.getPassword());
        assertEquals(request.getEmail(), savedUser.getEmail());
        assertNotNull(savedUser.getNickname());
        assertFalse(savedUser.getIsDeleted());
        assertEquals(defaultImage, savedUser.getImage());
    }
}
