package com.blackdragon.heytossme.persist.entity;

import com.sun.istack.NotNull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    private String category;

    @NotNull
    private String title;

    @NotNull
    private String contents;

    @NotNull
    private int price;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime dueDate;

    private float latitude;

    private float longitude;

    private String imageUrl;

    @NotNull
    private String status;
}
