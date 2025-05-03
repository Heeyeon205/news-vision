package com.newsvision.poll.repository;

import com.newsvision.poll.entity.Poll;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PollRepository extends JpaRepository<Poll, Long> {
    @EntityGraph(attributePaths = {"user", "pollOptions"})
    Optional<Poll> findById(Long id);
    List<Poll> findAllByExpiredAtAfter(LocalDateTime now);
    List<Poll> findByExpiredAtAfterOrderByCreatedAtDesc(LocalDateTime now, Pageable topTen);
    List<Poll> findByExpiredAtAfterOrderByExpiredAtAsc(LocalDateTime now, Pageable topTen);
}
