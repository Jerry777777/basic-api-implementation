package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public int register(User user) {
        UserPO userPO = new UserPO().builder()
                .userName(user.getName())
                .age(user.getAge())
                .gender(user.getGender())
                .email(user.getEmail())
                .phone(user.getPhone())
                .voteNum(user.getVoteNum())
                .build();
        UserPO saved = userRepository.save(userPO);
        return saved.getId();
    }

    public UserPO findUserById(int id) {
        return userRepository.findUserPOById(id);
    }

    @Transactional
    public int deleteUserById(int id) {
        return userRepository.deleteUserEntityById(id);
    }
}