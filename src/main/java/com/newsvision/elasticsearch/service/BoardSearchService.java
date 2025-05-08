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
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardSearchService {
    private final BoardSearchRepository boardSearchRepository;
    private final ElasticsearchClient elasticsearchClient;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public void saveBoard(Board board, int likeCount, int commentCount) {
        LocalDateTime formattedDateTime = LocalDateTime.parse(board.getCreatedAt().format(FORMATTER), FORMATTER);

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

    public void deleteBoard(Long boardId) {
        boardSearchRepository.deleteById(boardId);
    }

    public List<BoardResponse> searchBoard(String keyword) throws Exception {
        String analyzerSuffix = getAnalyzerSuffix(keyword);
        log.info("검색 필드: content.{}", analyzerSuffix);
        log.info("검색어: {}", keyword);

        try {
            SearchResponse<BoardDocument> response = elasticsearchClient.search(s -> s
                            .index("boards")
                            .query(q -> q
                                    .match(m -> m
                                            .field("content." + analyzerSuffix)
                                            .query(keyword)
                                    )
                            ),
                    BoardDocument.class);

            List<Hit<BoardDocument>> hits = response.hits().hits();

            if (hits == null || hits.isEmpty()) {
                log.info("검색 결과 없음");
                return List.of();
            }

            return hits.stream()
                    .map(Hit::source)
                    .filter(Objects::nonNull)
                    .map(doc -> {
                        BoardResponse res = new BoardResponse();
                        res.setBoardId(doc.getId());
                        res.setNickname(doc.getNickname());
                        res.setUserImage(doc.getUserImage());
                        res.setIcon(doc.getIcon());
                        res.setImage(doc.getImage());
                        res.setContent(doc.getContent());
                        res.setCategoryId(null);
                        res.setCreatedAt(doc.getCreatedAt() != null
                                ? doc.getCreatedAt().format(FORMATTER)
                                : "날짜 없음");
                        res.setNewsId(doc.getNewsId());
                        res.setIsReported(doc.getIsReported());
                        res.setLikeCount(doc.getLikeCount());
                        res.setCommentCount(doc.getCommentCount());
                        return res;
                    })
                    .toList();
        } catch (Exception e) {
            log.error("Elasticsearch 게시글 검색 중 오류 발생", e);
            e.printStackTrace();
            throw new RuntimeException("게시글 검색 실패", e);
        }
    }

    private String getAnalyzerSuffix(String input) {
        boolean hasKor = input.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
        boolean hasEng = input.matches(".*[a-zA-Z]+.*");
        if (hasKor && hasEng) return "mixed";
        if (hasKor) return "kor";
        return "eng";
    }
}
