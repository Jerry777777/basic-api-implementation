package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.UserPO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserPO, Integer> {
    List<UserPO> findAll();

    UserPO findUserPOById(Integer id);
}
