package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    List<User> userList = new ArrayList<>();

    @PostMapping("/rs/addUser")
    public void addOne(@RequestBody User user){
        userList.add(user);
    }

    @GetMapping("/rs/getUserList")
    public List<User> getUserList(){
        return userList;
    }
}
