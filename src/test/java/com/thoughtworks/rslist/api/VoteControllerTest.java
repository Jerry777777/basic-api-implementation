package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.Gender;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserEntityRepository;
import com.thoughtworks.rslist.repository.VoteEntityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VoteControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    UserEntityRepository userEntityRepository;

    @Autowired
    VoteEntityRepository voteEntityRepository;

    UserPO userPO = null;

    RsEventPO rsEventPO = null;

    @BeforeEach
    void setUp() {
        userPO = UserPO.builder()
                .age(20)
                .email("a@a.com")
                .gender(Gender.MALE)
                .phone("11234567890")
                .userName("user 0")
                .voteNum(10).build();
        userPO = userEntityRepository.save(userPO);
        rsEventPO = RsEventPO.builder()
                .eventName("第一条事件")
                .keyword("经济")
                .userPO(userPO)
                .build();
        rsEventPO = rsEventRepository.save(rsEventPO);
        for (int i = 0; i < 8; i++) {
            VotePO votePO = VotePO.builder().localDateTime(LocalDateTime.now()).rsEvent(rsEventPO)
                    .user(userPO).num(i + 1).build();
            voteEntityRepository.save(votePO);
        }
    }

    @AfterEach
    void tearDown() {
        voteEntityRepository.deleteAll();
        rsEventRepository.deleteAll();
        userEntityRepository.deleteAll();
    }

    @Test
    void should_get_vote_record() throws Exception {
        mockMvc.perform(get("/voteRecord")
                .param("userId", String.valueOf(userPO.getId()))
                .param("rsEventId", String.valueOf(rsEventPO.getId())))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId", is(userPO.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventPO.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(5)));

    }

    @Test
    void should_get_vote_record_add_page() throws Exception {
        mockMvc.perform(get("/voteRecord")
                .param("userId", String.valueOf(userPO.getId()))
                .param("rsEventId", String.valueOf(rsEventPO.getId()))
                .param("pageIndex", "1"))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].userId", is(userPO.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventPO.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(1)))
                .andExpect(jsonPath("$[1].voteNum", is(2)))
                .andExpect(jsonPath("$[2].voteNum", is(3)))
                .andExpect(jsonPath("$[3].voteNum", is(4)))
                .andExpect(jsonPath("$[4].voteNum", is(5)));

        mockMvc.perform(get("/voteRecord")
                .param("userId", String.valueOf(userPO.getId()))
                .param("rsEventId", String.valueOf(rsEventPO.getId()))
                .param("pageIndex", "2"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].userId", is(userPO.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventPO.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(6)))
                .andExpect(jsonPath("$[1].voteNum", is(7)))
                .andExpect(jsonPath("$[2].voteNum", is(8)));
    }

    @Test
    void should_vote_rsEvent() throws Exception {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        String timeString = df.format(time);
        Vote vote = Vote.builder().userId(userPO.getId()).time(timeString).voteNum(5).rsEventId(rsEventPO.getId()).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(vote);
        mockMvc.perform(post("/rs/vote").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("message", is("vote success")));
    }

    @Test
    void should_get_vote_record_between_select_time() throws Exception {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String voteTime0 = "2017-09-28 17:07:05";
        String startTime = "2020-01-01 00:00:00";

        String voteTime1 = "2020-02-02 00:00:00";
        String voteTime2 = "2020-03-03 00:00:00";

        String endTime = "2020-09-01 00:00:00";
        String voteTime3 = "2020-10-23 00:00:00";
        String[] times = {voteTime0, voteTime1, voteTime2, voteTime3};

        for (int i = 0; i < 4; i++) {
            VotePO vote = VotePO.builder().num(i+1).user(userPO).rsEvent(rsEventPO)
                    .localDateTime(LocalDateTime.parse(times[i], df)).build();
            voteEntityRepository.save(vote);
        }

        mockMvc.perform(get("/voteBetweenTime")
                .param("startTime", startTime)
                .param("endTime", endTime))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId", is(userPO.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventPO.getId())));

    }
}