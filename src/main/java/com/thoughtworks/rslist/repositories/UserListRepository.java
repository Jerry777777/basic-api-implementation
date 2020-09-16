package com.thoughtworks.rslist.repositories;

import com.thoughtworks.rslist.domain.Gender;
import com.thoughtworks.rslist.domain.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserListRepository {
    private List<User> userList = initList();

    private List<User> initList(){
        List<User> list = new ArrayList<>();
        list.add(new User("userA", Gender.MALE, 39, "A@aaa.com", "11234567890"));
        list.add(new User("userB", Gender.FEMALE, 32, "B@aaa.com", "11234567891"));
        list.add(new User("userC", Gender.Transgender, 21, "C@aaa.com", "11234567892"));
        return list;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public void addUser(User user){
        if (!isExist(user))
            userList.add(user);
    }

    public boolean isExist(User user){
        long count = userList.stream().filter(us -> us.equals(user)).count();
        return count > 0;
    }
}