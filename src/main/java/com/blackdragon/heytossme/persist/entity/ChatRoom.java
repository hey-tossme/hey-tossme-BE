package com.blackdragon.heytossme.persist.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Queue;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long buyerId;
    @NotNull
    private Long sellerId;

    @NotNull
    private  Long itemId;
    @NotNull
    private boolean accountTransferStatus;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Queue<ChatMessage> chatMessageQueue;
}
