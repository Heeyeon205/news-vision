package com.newsvision.notice.service;

import com.newsvision.global.Utils.TimeUtil;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.notice.dto.response.NoticeUserResponse;
import com.newsvision.notice.dto.response.NoticeEventResponse;
import com.newsvision.notice.entity.Notice;
import com.newsvision.notice.repository.NoticeRepository;
import com.newsvision.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final NoticeRepository noticeRepository;

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(5 * 60 * 1000L);
        emitters.put(userId, emitter);
        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError(e -> emitters.remove(userId));

        try {
            emitter.send(SseEmitter.event().name("connect").data("connected"));
        } catch (IOException e) {
            throw new RuntimeException("SSE 연결 실패", e);
        }
        return emitter;
    }

    public void sendNotification(Long receiverId, NoticeEventResponse response) {
        SseEmitter emitter = emitters.get(receiverId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(response));
            } catch (IOException e) {
                log.error(e.getMessage());
                emitters.remove(receiverId);
            }
        }
    }

    @Transactional
    public void save(Notice notice) {
        noticeRepository.save(notice);
    }

    public List<NoticeUserResponse> getAllNotice(Long userId){
        List<Notice> notices = noticeRepository.findByReceiverId(userId);
        return notices.stream()
                .sorted(Comparator.comparing(Notice::getCreatedAt).reversed())
                .map(notice -> NoticeUserResponse.builder()
                        .id(notice.getId())
                        .title(notice.getTitle())
                        .url(notice.getUrl())
                        .isRead(notice.isRead())
                        .createdAt(TimeUtil.formatRelativeTime(notice.getCreatedAt()))
                        .userId(notice.getSenderId().getId())
                        .image(notice.getSenderId().getImage())
                        .nickname(notice.getSenderId().getNickname())
                        .build())
                .toList();
    }

    public Notice findById(Long id) {
        return noticeRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT));
    }

    @Transactional
    public void checkRead(Long id) {
        Notice notice = findById(id);
        notice.updateRead(true);
    }

    @Transactional
    public void createAndSendNotice(User sender, User receiver, Notice.Type type, String url, String title) {
        if (sender.getId().equals(receiver.getId())) { return; }

        Notice notice = Notice.builder()
                .senderId(sender)
                .receiver(receiver)
                .type(type)
                .url(url)
                .title(title)
                .isRead(false)
                .build();
        save(notice);

        NoticeEventResponse response = new NoticeEventResponse(
                sender.getId(),
                type.name(),
                title,
                url,
                false
        );
        sendNotification(receiver.getId(), response);
    }

}
