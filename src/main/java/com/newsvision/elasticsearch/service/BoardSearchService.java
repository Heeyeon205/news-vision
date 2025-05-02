package com.newsvision.elasticsearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.newsvision.board.dto.response.BoardResponse;
import com.newsvision.board.entity.Board;
import com.newsvision.elasticsearch.document.BoardDocument;
import com.newsvision.elasticsearch.repository.BoardSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardSearchService {

    private final BoardSearchRepository boardSearchRepository;
    private final ElasticsearchClient elasticsearchClient;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // 게시글 저장
    public void saveBoard(Board board, int likeCount, int commentCount) {
        // createdAt 포맷 보장
        LocalDateTime formattedDateTime = LocalDateTime.parse(board.getCreateAt().format(FORMATTER), FORMATTER);

        BoardDocument doc = BoardDocument.builder()
                .id(board.getId())
                .content(board.getContent())
                .categoryName(board.getCategory().getName())
                .username(board.getUser().getUsername())
                .nickname(board.getUser().getNickname())
                .userImage(board.getUser().getImage())
                .icon(board.getUser().getBadge() != null ? board.getUser().getBadge().getIcon() : null)
                .image(board.getImage())
                .createdAt(formattedDateTime)
                .view(board.getView())
                .newsId(board.getNewsId())
                .isReported(board.getIsReported())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .build();

        boardSearchRepository.save(doc);
    }

    // 게시글 삭제
    public void deleteBoard(Long boardId) {
        boardSearchRepository.deleteById(boardId);
    }

    // 게시글 검색 (content 기준)
    public List<BoardResponse> searchBoard(String keyword) throws Exception {
        String analyzerSuffix = getAnalyzerSuffix(keyword);

        SearchResponse<BoardDocument> response = elasticsearchClient.search(s -> s
                        .index("boards")
                        .query(q -> q
                                .match(m -> m
                                        .field("content." + analyzerSuffix)
                                        .query(keyword)
                                )
                        ),
                BoardDocument.class);

        return response.hits().hits().stream()
                .map(Hit::source)
                .map(doc -> {
                    BoardResponse res = new BoardResponse();
                    res.setBoardId(doc.getId());
                    res.setNickname(doc.getNickname());
                    res.setUserImage(doc.getUserImage());
                    res.setIcon(doc.getIcon());
                    res.setImage(doc.getImage());
                    res.setContent(doc.getContent());
                    res.setCategoryId(null); // 카테고리 ID는 필요시 로직 수정
                    if (doc.getCreatedAt() != null) {
                        res.setCreatedAt(doc.getCreatedAt().format(FORMATTER));
                    } else {
                        res.setCreatedAt("날짜 없음");
                    }
//                    res.setView(doc.getView());
                    res.setNewsId(doc.getNewsId());
                    res.setIsReported(doc.getIsReported());
                    res.setLikeCount(doc.getLikeCount());
                    res.setCommentCount(doc.getCommentCount());
                    return res;
                })
                .toList();
    }

    private String getAnalyzerSuffix(String input) {
        boolean hasKor = input.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
        boolean hasEng = input.matches(".*[a-zA-Z]+.*");

        if (hasKor && hasEng) return "mixed";
        if (hasKor) return "kor";
        return "eng";
    }
}
