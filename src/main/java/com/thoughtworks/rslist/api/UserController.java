package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.repositories.UserListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserListRepository userListRepository;

    @PostMapping("/rs/addUser")
    public void addOne(@RequestBody @Validated User user){
        userListRepository.addUser(user);
    }

    @GetMapping("/rs/getUserList")
    public List<User> getUserList(){
        return userListRepository.getUserList();
    }
}
