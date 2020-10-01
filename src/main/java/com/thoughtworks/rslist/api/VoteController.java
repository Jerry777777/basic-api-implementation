package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserEntityRepository;
import com.thoughtworks.rslist.repository.VoteEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VoteController {
    @Autowired
    VoteEntityRepository voteRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    UserEntityRepository userEntityRepository;

    @GetMapping("/voteRecord")
    public ResponseEntity<List<Vote>> getVoteRecord(@RequestParam int userId,
                                                    @RequestParam int rsEventId,
                                                    @RequestParam(required = false) int pageIndex,
                                                    @RequestParam(required = false) String startTime,
                                                    @RequestParam(required = false) String endTime) {
        Pageable pageable = PageRequest.of(pageIndex - 1, 5);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ResponseEntity.ok(voteRepository.findAllByUserIdAndRsEventId(userId, rsEventId, pageable).stream().map(
                item -> Vote.builder().voteNum(item.getNum()).userId(item.getUser().getId()).time(df.format(item.getLocalDateTime()))
                        .rsEventId(item.getRsEvent().getId()).build()).collect(Collectors.toList()));
    }

    @PostMapping("/rs/vote")
    public ResponseEntity<String> voteToRsEvent(@RequestBody Vote vote) {
        UserPO user = userEntityRepository.findUserEntityById(vote.getUserId());
        if (user.getVoteNum() >= vote.getVoteNum()){
            VotePO saved = voteRepository.save(VotePO.builder()
                    .localDateTime(LocalDateTime.parse(vote.getTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .rsEvent(rsEventRepository.findRsEventEntityById(vote.getRsEventId()))
                    .user(userEntityRepository.findUserEntityById(vote.getUserId()))
                    .num(vote.getVoteNum()).build());
            if (saved != null)
                return ResponseEntity.ok().header("message", "vote success").build();
        }
        return ResponseEntity.badRequest().header("message", "vote fail").build();
    }

    @GetMapping("/voteBetweenTime")
    public ResponseEntity<List<Vote>> getVoteRecord(@RequestParam() String startTime,
                                                    @RequestParam() String endTime){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startTime, df);
        LocalDateTime end = LocalDateTime.parse(endTime, df);
        List<VotePO> votes = voteRepository.findAllBetweenTime(start, end);
        return ResponseEntity.ok(votes.stream().map(
                item -> Vote.builder().voteNum(item.getNum()).userId(item.getUser().getId()).time(df.format(item.getLocalDateTime()))
                        .rsEventId(item.getRsEvent().getId()).build()).collect(Collectors.toList()));
    }
}
