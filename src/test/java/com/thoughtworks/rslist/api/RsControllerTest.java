package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.Gender;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserEntityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    UserEntityRepository userEntityRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach()
    void initData() {
        rsEventRepository.deleteAll();
        userEntityRepository.deleteAll();
    }

    @Test
    void should_update_one_event() throws Exception {

    }

    @Test
    void should_delete_one_event() throws Exception {

    }

    @Test
    void should_return_bad_request_when_add_new_event_eventName_is_empty() throws Exception {
        /*RsEvent rsEvent = new RsEvent(4, null, "国际",
                new User("userA", Gender.MALE, 39, "A@aaa.com", "11234567890"));

        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);*/

        String rsEventJson = "{\"id\":4,\"eventName\":null,\"keyWord\":\"国际\"," +
                "\"user\":{\"user_name\":\"userA\",\"user_gender\":\"MALE\",\"user_age\":39," +
                "\"user_email\":\"A@aaa.com\",\"user_phone\":\"11234567890\"}}";

        mockMvc.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void should_return_bad_request_when_add_new_event_keyWord_is_empty() throws Exception {
        /*RsEvent rsEvent = new RsEvent(4, "第四条事件", null,
                new User("userA", Gender.MALE, 39, "A@aaa.com", "11234567890"));

        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);*/

        String rsEventJson = "{\"id\":4,\"eventName\":\"第四条事件\",\"keyWord\":null," +
                "\"user\":{\"user_name\":\"userA\",\"user_gender\":\"MALE\",\"user_age\":39," +
                "\"user_email\":\"A@aaa.com\",\"user_phone\":\"11234567890\"}}";

        mockMvc.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void should_return_bad_request_when_add_new_event_user_is_empty() throws Exception {
        /*RsEvent rsEvent = new RsEvent(4, "第四条事件", "国际", null);

        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);*/

        String rsEventJson = "{\"id\":4,\"eventName\":null,\"keyWord\":\"国际\"," +
                "\"user\":null}";

        mockMvc.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void should_return_bad_request_when_add_new_event_user_name_is_empty() throws Exception {
        /*RsEvent rsEvent = new RsEvent(4, "第四条事件", "国际",
                new User(null, Gender.MALE, 43, "D@aaa.com", "11234567893"));

        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);*/

        String rsEventJson = "{\"id\":4,\"eventName\":\"第四条事件\",\"keyWord\":null," +
                "\"user\":{\"user_name\":null,\"user_gender\":\"MALE\",\"user_age\":39," +
                "\"user_email\":\"A@aaa.com\",\"user_phone\":\"11234567890\"}}";

        mockMvc.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void should_return_400_when_get_sub_list_start_less_than_end() throws Exception {
        mockMvc.perform(get("/rs/list?start=2&end=1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    void should_return_400_when_get_sub_list_start_out_of_bound() throws Exception {
        mockMvc.perform(get("/rs/list?start=5&end=1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    void should_return_400_when_get_sub_list_end_out_of_bound() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=7"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    void should_return_400_when_get_one_index_out_of_bound() throws Exception {
        mockMvc.perform(get("/rs/7"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));
    }

    @Test
    void should_add_new_event() throws Exception {
        UserPO savedUser = userEntityRepository.save(UserPO.builder()
                .userName("user 0").age(20).gender(Gender.MALE).email("0@a.com").phone("11234567890").voteNum(5).build());

        String rsEventJson = "{\"eventName\":\"第四条事件\",\"keyWord\":\"社会\"," +"\"userId\":" + savedUser.getId() +"}";

        mockMvc.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("eventId", String.valueOf(2)));

        List<RsEventPO> events = rsEventRepository.findAll();

        assertEquals(1, events.size());
        assertEquals(2, events.get(0).getId());
    }

    @Test
    void should_add_new_event_when_user_not_exist() throws Exception {
        UserPO savedUser = userEntityRepository.save(UserPO.builder()
                .userName("user 0").age(20).gender(Gender.MALE).email("0@a.com").phone("11234567890").voteNum(5).build());

        String rsEventJson = "{\"eventName\":\"第四条事件\",\"keyWord\":\"社会\"," +"\"userId\":" + 6 +"}";

        mockMvc.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_update_event_when_user_is_right() throws Exception {
        UserPO savedUser = userEntityRepository.save(UserPO.builder()
                .userName("user 0").age(20).gender(Gender.MALE).email("0@a.com").phone("11234567890").voteNum(5).build());
        RsEventPO savedEvent = rsEventRepository.save(RsEventPO.builder().eventName("第一条事件").keyword("社会").build());
        savedUser.setEvents(Collections.singletonList(savedEvent));
        savedEvent.setUserPO(savedUser);

        String rsEventJson = "{\"eventName\":\"第二条事件\",\"keyWord\":\"社会\"," +"\"userId\":" + savedUser.getId() +"}";

        mockMvc.perform(put("/rs/{rsEventId}", savedEvent.getId()).content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        RsEventPO updated = rsEventRepository.findRsEventEntityById(savedEvent.getId());
        assertEquals("第二条事件", updated.getEventName());
        assertEquals("社会", updated.getKeyword());
    }

    @Test
    void should_return_400_in_update_event_when_user_is_empty() throws Exception {
        UserPO savedUser = userEntityRepository.save(UserPO.builder()
                .userName("user 0").age(20).gender(Gender.MALE).email("0@a.com").phone("11234567890").voteNum(5).build());
        RsEventPO savedEvent = rsEventRepository.save(RsEventPO.builder().eventName("第一条事件").keyword("社会").build());
        savedUser.setEvents(Collections.singletonList(savedEvent));
        savedEvent.setUserPO(savedUser);

        String rsEventJson = "{\"eventName\":\"第二条事件\",\"keyWord\":\"社会\"," +"\"userId\":" + 6 +"}";

        mockMvc.perform(put("/rs/{rsEventId}", savedEvent.getId()).content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_update_event_when_user_is_right_and_keyword_is_empty() throws Exception {
        UserPO savedUser = userEntityRepository.save(UserPO.builder()
                .userName("user 0").age(20).gender(Gender.MALE).email("0@a.com").phone("11234567890").voteNum(5).build());
        RsEventPO savedEvent = rsEventRepository.save(RsEventPO.builder().eventName("第一条事件").keyword("社会").build());
        savedUser.setEvents(Collections.singletonList(savedEvent));
        savedEvent.setUserPO(savedUser);

        String rsEventJson = "{\"eventName\":\"第二条事件\",\"keyWord\":null," +"\"userId\":" + savedUser.getId() +"}";

        mockMvc.perform(put("/rs/{rsEventId}", savedEvent.getId()).content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        RsEventPO updated = rsEventRepository.findRsEventEntityById(savedEvent.getId());
        assertEquals("第二条事件", updated.getEventName());
        assertEquals("社会", updated.getKeyword());
    }
}