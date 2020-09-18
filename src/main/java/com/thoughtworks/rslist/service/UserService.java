package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.repositories.UserListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserListRepository userRepository;

    public int addOne(User user){
        return userRepository.addUser(user);
    }

    public List<User> getAllUsers(){
        List<User> userList = userRepository.getUserList();
        return userList;
    }
}