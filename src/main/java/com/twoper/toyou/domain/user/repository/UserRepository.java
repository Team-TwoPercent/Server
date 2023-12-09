package com.twoper.toyou.domain.user.repository;

import com.twoper.toyou.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
     User findByUsername(String username);
     User findByName(String name);
}
