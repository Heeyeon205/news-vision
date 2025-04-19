package com.newsvision.poll.repository;

import com.newsvision.poll.entity.PollVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollVoteRepository extends JpaRepository<PollVote, Long> {
}
