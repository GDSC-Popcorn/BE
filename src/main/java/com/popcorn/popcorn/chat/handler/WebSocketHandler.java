package com.popcorn.popcorn.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popcorn.popcorn.chat.dto.ChatMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        String payload = message.getPayload();
        log.info("{}",payload);
        ChatMessageDto chatMessageDto = objectMapper.convertValue(payload, ChatMessageDto.class);
        String roomId = chatMessageDto.getRoomId();

        // 해당 채팅방에 세션이 등록되어 있는지 확인

    }
}
