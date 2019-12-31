package com.example.demo.jpa;

import com.example.demo.dataobject.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author SanKai
 * @since 2019-11-06
 */
@Repository
public interface UserJpaDAO extends JpaRepository<UserDO, Long> {

    UserDO findByUsername(String name);

    UserDO findByUsernameAndPassword(String name, String password);


}
