package com.newsvision.admin.service;

import com.newsvision.admin.controller.response.CommentReportResponse;
import com.newsvision.board.entity.Comment;
import com.newsvision.board.entity.CommentReport;
import com.newsvision.board.repository.CommentReportRepository;
import com.newsvision.board.repository.CommentRepository;
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
                        .commentId(String.valueOf(report.getComment().getId()))
                        .commentContent(report.getComment().getContent())  // 댓글 내용 가져오기
                        .userId(String.valueOf(report.getUser().getId()))
                        .userNickname(report.getUser().getNickname())      // 신고자 닉네임 가져오기
                        .build())
                .collect(Collectors.toList());
    }

    public List<CommentReportResponse> getMaxAllCommentReports() {
        return commentReportRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .map(report -> CommentReportResponse.builder()
                        .id(report.getId())
                        .commentId(String.valueOf(report.getComment().getId()))
                        .commentContent(report.getComment().getContent())  // 댓글 내용 가져오기
                        .userId(String.valueOf(report.getUser().getId()))
                        .userNickname(report.getUser().getNickname())      // 신고자 닉네임 가져오기
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
        comment.setIsReported(true);
        comment.setContent("관리자로 인해 삭제된 게시글 입니다.");
        commentRepository.save(comment);
    }

}