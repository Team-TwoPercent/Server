package com.example.twoper.Letter.repository;

import com.example.twoper.Letter.model.Letter;
import com.twoper.toyou.domain.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Repository
public interface LetterRepository extends JpaRepository<Letter, Integer> {

  List<Letter> findAllByReceiver(User user);
  List<Letter> findAllBySender (User user);
}
