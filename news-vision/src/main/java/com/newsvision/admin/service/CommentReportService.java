package com.newsvision.admin.service;

import com.newsvision.admin.controller.response.CommentReportResponse;
import com.newsvision.board.repository.CommentReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentReportService {

    private final CommentReportRepository commentReportRepository;

    public List<CommentReportResponse> getAllCommentReports() {
        return commentReportRepository.findAll().stream()
                .map(report -> CommentReportResponse.builder()
                        .id(report.getId())
                        .comment(String.valueOf(report.getComment().getId()))
                        .user(String.valueOf(report.getUser().getId()))
                        .build())
                .collect(Collectors.toList());
    }
}