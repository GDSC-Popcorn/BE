package com.popcorn.popcorn.chat.service;

import com.popcorn.popcorn.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    public boolean findUsingChatRoom(Long roomId){
        ValueOperations<String,Object> values = redisTemplate.opsForValue();
        return values.get(roomId) != null;
    }
    public Boolean createChatRoom(Long roomId, Long userId){
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        String chatRoomKey = (String) roomId.toString()+"-"+userId.toString(); // 키값 : 채팅방 아이디-유저 아이디
        String data = "1";
        values.set(chatRoomKey, data, 3L);
        return null;
    }


}
