package com.newsvision.poll.repository;

import com.newsvision.poll.entity.PollVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PollVoteRepository extends JpaRepository<PollVote, Long> {
    boolean existsByUserIdAndPollOption_Poll_Id(Long userId, Long pollId);
    void deleteByPollOption_Poll_Id(Long pollId);
    int countByPollOption_Poll_Id(Long pollId);

    boolean existsByPollOption_Poll_IdAndUser_Id(Long pollOptionPollId, Long userId);
}
