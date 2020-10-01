package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    UserEntityRepository userEntityRepository;

    public int register(User user) {
        UserPO entity = new UserPO().builder()
                .userName(user.getName())
                .age(user.getAge())
                .gender(user.getGender())
                .email(user.getEmail())
                .phone(user.getPhone())
                .voteNum(user.getVoteNum())
                .build();
        UserPO saved = userEntityRepository.save(entity);
        return saved.getId();
    }

    public UserPO findUserById(int id) {
        return userEntityRepository.findUserEntityById(id);
    }

    @Transactional
    public int deleteUserById(int id) {
        return userEntityRepository.deleteUserEntityById(id);
    }
}