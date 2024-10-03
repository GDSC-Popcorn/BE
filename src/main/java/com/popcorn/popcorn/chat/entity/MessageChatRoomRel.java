package com.popcorn.popcorn.chat.entity;

import jakarta.persistence.*;

@Entity
public class MessageChatRoomRel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long messageChatRoomId;

    @ManyToOne
    @JoinColumn(name="chat_room_id")
    private ChatRoom chatRoom;
    @ManyToOne
    @JoinColumn(name="message_id")
    private Message message;
}
