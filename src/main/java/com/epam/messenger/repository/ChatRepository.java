package com.epam.messenger.repository;

import com.epam.messenger.model.Chat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Integer> {

    List<Chat> findAll();

    List<Chat> findChatsByUsersUserId(Integer userId);
}
