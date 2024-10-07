package com.popcorn.popcorn.chat.repository;

import com.popcorn.popcorn.chat.entity.ChatRoom;
import org.springframework.data.repository.CrudRepository;

public interface ChatRoomRepository extends CrudRepository<ChatRoom,Long> {
}
