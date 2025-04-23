package com.newsvision.admin.service;

import com.newsvision.admin.controller.response.CommentReportResponse;
import com.newsvision.board.entity.Board;
import com.newsvision.board.entity.BoardReport;
import com.newsvision.board.entity.Comment;
import com.newsvision.board.entity.CommentReport;
import com.newsvision.board.repository.BoardRepository;
import com.newsvision.board.repository.CommentReportRepository;
import com.newsvision.board.repository.CommentRepository;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentReportService {

    private final CommentReportRepository commentReportRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public List<CommentReportResponse> getAllCommentReports() {
        return commentReportRepository.findAll().stream()
                .map(report -> CommentReportResponse.builder()
                        .id(report.getId())
                        .comment(String.valueOf(report.getComment().getId()))
                        .user(String.valueOf(report.getUser().getId()))
                        .build())
                .collect(Collectors.toList());
    }


    public List<CommentReportResponse> getMaxAllCommentReports() {
        return commentReportRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .map(report -> CommentReportResponse.builder()
                        .id(report.getId())
                        .comment(String.valueOf(report.getComment().getId()))
                        .user(String.valueOf(report.getUser().getId()))
                        .build())
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
        comment.setIsReported(true); // isReported = true 설정
        comment.setContent("관리자로 인해 삭제된 게시글 입니다.");
        commentRepository.save(comment);
    }




}