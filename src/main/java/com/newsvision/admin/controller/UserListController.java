package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.UserListResponse;
import com.newsvision.admin.service.UserListService;
import com.newsvision.global.exception.CustomException;
import com.newsvision.user.repository.UserRepository;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor

public class UserListController {
    private final UserListService userListService;
    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserListController.class);
    @GetMapping("/admin/users")
    public List<UserListResponse> getUserList() {
        return userListService.getAllUsers();
    }

    @PostMapping("/admin/users")
    public UserListResponse createUser(@RequestBody UserListResponse userListResponse) {
        return userListService.saveUser(userListResponse);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) { // 반환 타입을 ResponseEntity<?>로 변경
        try {
            log.info("Attempting to delete user with ID: {}", id);
            userService.delete(id);
            log.info("Successfully deleted user with ID: {}", id);
            // 성공 시 204 No Content 반환
            return ResponseEntity.noContent().build();
        } catch (CustomException e) { // CustomException 처리 (예: 사용자를 찾을 수 없는 경우)
            log.warn("Could not delete user with ID: {}. Reason: {}", id, e.getMessage());
            // 예시: CustomException 종류에 따라 다른 상태 코드 반환 가능
            // 여기서는 User Not Found를 가정하고 404 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", e.getMessage())); // 간단한 에러 메시지 본문 추가
        } catch (DataIntegrityViolationException e) { // 데이터 무결성 제약 조건 위반 처리
            log.error("Data integrity violation while deleting user ID: {}", id, e);
            // 외래 키 제약 조건 등 DB 제약 조건 위반 시 409 Conflict 또는 400 Bad Request 반환 가능
            return ResponseEntity.status(HttpStatus.CONFLICT) // 409 Conflict
                    .body(Map.of("success", false, "message", "삭제할 수 없습니다. 다른 데이터에서 참조하고 있습니다."));
        } catch (Exception e) { // 그 외 모든 예외 처리 (NullPointerException, JpaException 등)
            log.error("An unexpected error occurred while deleting user ID: {}", id, e);
            // 예상치 못한 모든 오류는 500 Internal Server Error 반환
            // 실제 서비스에서는 좀 더 구체적인 에러 메시지나 코드를 포함하는 것이 좋음
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "사용자 삭제 중 서버 내부 오류가 발생했습니다."));
        }
    }
}
