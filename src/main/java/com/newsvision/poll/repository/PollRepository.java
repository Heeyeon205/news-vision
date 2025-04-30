package com.newsvision.poll.repository;

import com.newsvision.poll.controller.response.PollListResponse;
import com.newsvision.poll.entity.Poll;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PollRepository extends JpaRepository<Poll, Long> {
    @EntityGraph(attributePaths = {"user", "pollOptions"})
    Optional<Poll> findById(Long id);
    List<Poll> findAllByExpiredAtAfter(LocalDateTime now);
}
