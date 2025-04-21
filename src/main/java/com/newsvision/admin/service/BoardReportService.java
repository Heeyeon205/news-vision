package com.newsvision.admin.service;

import com.newsvision.admin.controller.response.BoardReportResponse;
import com.newsvision.board.repository.BoardReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardReportService {

    private final BoardReportRepository boardReportRepository;

    public List<BoardReportResponse> getAllReports() {
        return boardReportRepository.findAll().stream()
                .map(report -> BoardReportResponse.builder()
                        .id(report.getId())
                        .board(String.valueOf(report.getBoard().getId()))
                        .user(String.valueOf(report.getUser().getId()))
                        .build())
                .collect(Collectors.toList());
    }
}