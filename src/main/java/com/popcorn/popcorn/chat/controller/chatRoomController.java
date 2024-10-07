package com.popcorn.popcorn.chat.controller;

import com.popcorn.popcorn.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class chatRoomController {
    @Autowired
    ChatService chatService;

    @PostMapping("/create")
    public ResponseEntity<Object> createChatRoom(@RequestParam(name = "sendId") Long userId,@RequestParam(name="roomId")Long roomId){
        Boolean result = chatService.createChatRoom(roomId,userId);

        return new ResponseEntity<>(result, HttpStatus.CREATED);


    }
}
