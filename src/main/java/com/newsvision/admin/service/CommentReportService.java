package com.newsvision.admin.service;

import com.newsvision.admin.dto.response.CommentReportResponse;
import com.newsvision.board.entity.Comment;
import com.newsvision.board.entity.CommentReport;
import com.newsvision.board.repository.CommentReportRepository;
import com.newsvision.board.repository.CommentRepository;
import com.newsvision.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentReportService {
    private final CommentReportRepository commentReportRepository;
    private final CommentRepository commentRepository;

    public List<CommentReportResponse> getAllCommentReports() {
        return commentReportRepository.findAll().stream()
                .map(report -> {
                    Comment comment = report.getComment();
                    return CommentReportResponse.builder()
                            .id(report.getId())
                            .commentId(String.valueOf(comment.getId()))
                            .commentContent(comment.getContent())
                            .userId(String.valueOf(report.getUser().getId()))
                            .commentWriter(comment.getUser().getNickname())
                            .userNickname(report.getUser().getNickname())
                            .boardId(comment.getBoard().getId())
                            .createdAt(comment.getCreateAt().toString())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public void deleteCategory(Long id) {
        commentReportRepository.deleteById(id);
    }

    @Transactional
    public void markCommentAsReported(Long reportId) {
        CommentReport report = commentReportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("신고 ID가 존재하지 않습니다: " + reportId));

        Comment comment = report.getComment();
        comment.setIsReported(true);
        comment.setContent("관리자로 인해 삭제된 댓글 입니다.");
        commentRepository.save(comment);

        commentReportRepository.deleteByComment(comment);
    }
}