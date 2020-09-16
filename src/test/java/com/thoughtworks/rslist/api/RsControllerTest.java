package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.Gender;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(1)
    void should_get_list() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("经济")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("社会")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("民生")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void should_get_one() throws Exception {
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyWord", is("经济")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyWord", is("社会")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyWord", is("民生")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void should_get_sub_list() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("经济")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("社会")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=2&end=3"))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("社会")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("民生")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("经济")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("社会")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("民生")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void should_add_one_event() throws Exception {
        RsEvent rsEvent = new RsEvent(4,"第四条事件", "国际", new User("UserD", Gender.MALE, 43, "D@aaa.com", "11234567893"));

        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/add").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("经济")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("社会")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("民生")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void should_update_one_event() throws Exception {
        int reId = 1;
        RsEvent rsEvent = new RsEvent(1,"第一条事件", "教育", null);

        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventString = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(put("/rs/update/{id}", reId).content(rsEventString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyWord", is("教育")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void should_delete_one_event() throws Exception {
        int rsId = 1;
        mockMvc.perform(delete("/rs/delete/{id}", rsId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("社会")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("民生")))
                .andExpect(status().isOk());
    }

//    @Test
//    @Order(7)
//    void should_add_one_event_with_exited_user() throws Exception {
//        RsEvent rsEvent = new RsEvent(4,"第四条事件", "国际",
//                new User("userA", Gender.MALE, 39, "A@aaa.com", "11234567890"));
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
//
//        mockMvc.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[0].keyWord", is("经济")))
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("社会")))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyWord", is("民生")))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/rs/getUserList"))
//                .andExpect(jsonPath("$.*", hasSize(3)));
//    }

}

