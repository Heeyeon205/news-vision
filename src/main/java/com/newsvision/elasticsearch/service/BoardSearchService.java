package com.newsvision.elasticsearch.service;

import com.newsvision.board.entity.Board;
import com.newsvision.board.controller.response.BoardResponse;
import com.newsvision.elasticsearch.document.BoardDocument;
import com.newsvision.elasticsearch.repository.BoardSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardSearchService {

    private final BoardSearchRepository boardSearchRepository;

    // 게시글 저장 (JPA → Elasticsearch)
    public void saveBoard(Board board) {
        BoardDocument doc = BoardDocument.builder()
                .id(board.getId())
                .content(board.getContent())
                .categoryName(board.getCategory().getName())
                .username(board.getUser().getUsername())
                .image(board.getImage())
                .createdAt(board.getCreateAt())
                .build();

        boardSearchRepository.save(doc);
    }

    // 게시글 삭제
    public void deleteBoard(Long boardId) {
        boardSearchRepository.deleteById(boardId);
    }

    // 게시글 검색 (content 기준)
//    public List<BoardResponse> searchBoard(String keyword) {
//        return boardSearchRepository.searchByContent(keyword)
//                .stream()
//                .map(doc -> new BoardResponse(
//                        doc.getContent(),
//                        null,  // categoryId는 없어도 무방하거나 추가할 수도 있음
//                        doc.getCreatedAt(),
//                        null,  // relativeTime → 프론트에서 처리해도 되고, util 써도 되고
//                        null,  // userId → 필요 시 추가
//                        doc.getImage(),
//                        0L,    // view
//                        null,  // newsId
//                        false, // isReported
//                        0L,    // likeCount
//                        0L     // commentCount
//                ))
//                .toList();
//    }
}
