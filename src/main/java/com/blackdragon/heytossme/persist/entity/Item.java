package com.blackdragon.heytossme.persist.entity;

import com.blackdragon.heytossme.type.Category;
import com.blackdragon.heytossme.type.ItemStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Member seller;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @NotNull
    private String title;

    @NotNull
    private String contents;

    @NotNull
    private Integer price;

    @NotNull
    private LocalDateTime dueDate;

    private float latitude;

    private float longitude;

    private String imageUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Exclude
    private Address address;

    public void setAddress(Address address) {
        this.address = address;
    }

}
