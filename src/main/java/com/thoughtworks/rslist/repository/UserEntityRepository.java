package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.UserPO;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface UserEntityRepository extends CrudRepository<UserPO, Integer> {
    List<UserPO> findAll();
    UserPO findUserEntityById(Integer id);
    @Transactional
    int deleteUserEntityById(Integer id);
    @Override
    void deleteAll();
}
