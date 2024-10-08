package com.popcorn.popcorn.chat.entity;

import com.popcorn.popcorn.entity.Member;
import jakarta.persistence.*;

@Entity
public class ChatRoomMemberRel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

}