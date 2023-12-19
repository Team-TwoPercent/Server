package com.twoper.toyou.domain.letter.repository;

import com.twoper.toyou.domain.letter.model.Letter;
import com.twoper.toyou.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LetterRepository extends JpaRepository<Letter, Integer> {

  List<Letter> findAllByReceiver(User user);
  List<Letter> findAllBySender (User user);
}
