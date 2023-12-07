package com.example.twoper.jwt.repoditory;

import com.example.twoper.jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
     User findByUsername(String username);
     User findByName(String name);
}
