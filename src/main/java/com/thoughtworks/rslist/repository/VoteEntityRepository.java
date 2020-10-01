package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.VotePO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteEntityRepository extends PagingAndSortingRepository<VotePO, Integer> {
    List<VotePO> findAllByUserIdAndRsEventId(int userId, int rsEventId, Pageable pageable);
    @Query(value = "from VoteEntity where localDateTime between ?1 and ?2")
    List<VotePO> findAllBetweenTime(LocalDateTime startTime, LocalDateTime endTime);
}
