package com.newsvision.global.Utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeUtil {

    public static String formatRelativeTime(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return "방금 전";
        } else if (seconds < 3600) {
            return (seconds / 60) + "분 전";
        } else if (seconds < 86400) {
            return (seconds / 3600) + "시간 전";
        } else if (seconds < 7 * 86400) {
            return (seconds / 86400) + "일 전";
        } else if (seconds < 30 * 86400) {
            return (seconds / (7 * 86400)) + "주 전";
        } else if (seconds < 365 * 86400) {
            return (seconds / (30 * 86400)) + "개월 전";
        } else {
            return (seconds / (365 * 86400)) + "년 전";
        }
    }

    public static String dDayCaculate(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        long seconds = duration.getSeconds();
        if (seconds < 60) {
            return "곧 마감!";
        }else if (seconds < 3600) {
            return (seconds / 60) + "분 남았어요!";
        } else if (seconds < 86400) {
            return (seconds / 3600) + "시간 남았어요!";
        } else if (seconds < 7 * 86400) {
            return (seconds / 86400) + "일 남았어요!";
        } else if (seconds < 30 * 86400) {
            return (seconds / (7 * 86400)) + "주 남았어요!";
        } else if (seconds < 365 * 86400) {
            return (seconds / (30 * 86400)) + "개월 남았어요!";
        } else {
            return (seconds / (365 * 86400)) + "년 남았어요!";
        }
    }
}
