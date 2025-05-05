package com.newsvision.poll.repository;

import com.newsvision.poll.entity.Poll;
import com.newsvision.poll.entity.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollOptionRepository extends JpaRepository<PollOption, Long> {
    void deleteByPollId(Long pollId);

    List<PollOption> findByPoll(Poll poll);
}
