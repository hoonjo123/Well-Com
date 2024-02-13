package com.wellcom.domain.Item;

import com.wellcom.domain.SharingRoom.SharingRoom;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "item")
    private SharingRoom sharingRoom;

    @Column(nullable = false)
    private String name;

    private String imagePath;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    public void updateItem(String name, String imagePath){
        this.name = name;
        this.imagePath = imagePath;
    }

    public void doneItem(){
        this.itemStatus = ItemStatus.DONE;
    }
}