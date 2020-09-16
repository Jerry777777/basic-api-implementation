package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Gender;
import com.thoughtworks.rslist.domain.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private List<User> userList = initList();

    private List<User> initList(){
        List<User> list = new ArrayList<>();
        list.add(new User("userA", Gender.MALE, 39, "A@aaa.com", "18888888888"));
        list.add(new User("userB", Gender.FEMALE, 32, "B@aaa.com", "17777777777"));
        list.add(new User("userC", Gender.Transgender, 21, "C@aaa.com", "19999999999"));
        return list;
    }

    @PostMapping("/rs/addUser")
    public void addOne(@RequestBody @Validated User user){
        userList.add(user);
    }

    @GetMapping("/rs/getUserList")
    public List<User> getUserList(){
        return userList;
    }
}
