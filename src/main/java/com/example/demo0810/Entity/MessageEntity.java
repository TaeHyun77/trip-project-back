package com.example.demo0810.Entity;

import com.example.demo0810.Entity.etc.BaseTimeEntity;
import com.example.demo0810.Entity.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class MessageEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER) // 송신자
    @JoinColumn(name = "sender_id")
    public UserEntity sender;

    @ManyToOne(fetch = FetchType.EAGER) // 수신자
    @JoinColumn(name = "receiver_id")
    public UserEntity receiver;

    @Column(nullable = false)
    private boolean deletedBySender; // 송신자가 삭제했는지

    @Column(nullable = false)
    private boolean deletedByReceiver; // 수신자가 삭제했는지

    public void deleteBySender() {
        this.deletedBySender = true;
    }

    public void deleteByReceiver() {
        this.deletedByReceiver = true;
    }

    public boolean isDeleted() {
        return isDeletedBySender() && isDeletedByReceiver();
    }

    @Builder
    public MessageEntity (String title, String content, UserEntity sender, UserEntity receiver, boolean deletedByReceiver, boolean deletedBySender) {
        this.title = title;
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.deletedByReceiver = deletedByReceiver;
        this.deletedBySender = deletedBySender;
    }
}
