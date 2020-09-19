package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.Gender;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    void clearData(){
        userRepository.deleteAll();
    }


    @Test
    void should_return_all_user() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[0]", hasKey("user_name")))
                .andExpect(jsonPath("$[0]", hasKey("user_age")))
                .andExpect(jsonPath("$[0]", hasKey("user_gender")))
                .andExpect(jsonPath("$[0]", hasKey("user_email")))
                .andExpect(jsonPath("$[0]", hasKey("user_phone")));
    }

    @Test
    void should_add_user() throws Exception {
        User user = new User("zhangsan", Gender.MALE, 20, "a@b.com", "11234567890");
        String userString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/addUser").content(userString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("addIndex", "4"));

        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$.*", hasSize(4)));
    }

    @Test
    void should_return_bad_request_when_userName_to_long() throws Exception {
        User user = new User("zhangsan123", Gender.MALE, 20, "a@b.com", "11234567890");
        String userString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/addUser").content(userString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void should_return_bad_request_when_userName_is_empty() throws Exception {
        User user = new User(null, Gender.MALE, 20, "a@b.com", "11234567890");
        String userString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/addUser").content(userString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void should_return_bad_request_when_gender_is_null() throws Exception {
        User user = new User("zhangsan", null, 20, "a@b.com", "11234567890");
        String userString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/addUser").content(userString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void should_return_bad_request_when_age_less_than_18() throws Exception {
        User user = new User("zhangsan", Gender.MALE, 17, "a@b.com", "11234567890");
        String userString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/addUser").content(userString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void should_return_bad_request_when_age_more_than_100() throws Exception {
        User user = new User("zhangsan", Gender.MALE, 101, "a@b.com", "11234567890");
        String userString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/addUser").content(userString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void should_return_bad_request_when_email_not_right_format() throws Exception {
        User user = new User("zhangsan", Gender.MALE, 20, "ab.com", "11234567890");
        String userString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/addUser").content(userString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void should_return_bad_request_when_phone_not_right() throws Exception {
        User user = new User("zhangsan", Gender.MALE, 20, "a@b.com", "1123456789");
        String userString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/rs/addUser").content(userString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void should_return_save_to_database_when_register_user() throws Exception {
        User user = new User("userA", Gender.MALE, 39, "A@aaa.com", "11234567890");
        String userString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<UserPO> users = userRepository.findAll();

        assertEquals(1, users.size());
        assertEquals("userA", users.get(0).getUserName());
    }

    @Test
    void should_get_user_by_id() throws Exception {
        User user = new User("userA", Gender.MALE, 39, "A@aaa.com", "11234567890");
        String userString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user").content(userString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/findUserById/{id}", 1))
                .andExpect(jsonPath("$.userName", is(user.getName())));
    }
}