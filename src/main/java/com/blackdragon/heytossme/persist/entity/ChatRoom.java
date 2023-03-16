package com.blackdragon.heytossme.persist.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ChatRoom extends BaseTimeEntity implements Comparable<ChatRoom> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "buyer_id")
    @NotNull
    private Member buyer;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id")
    @NotNull
    private Member seller;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;
    @NotNull
    private boolean accountTransferStatus;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<ChatMessage> chatMessageList = new ArrayList<>();

    @Override
    public int compareTo(ChatRoom o) {
        return this.getCreatedAt().compareTo(o.getCreatedAt());
    }
}
