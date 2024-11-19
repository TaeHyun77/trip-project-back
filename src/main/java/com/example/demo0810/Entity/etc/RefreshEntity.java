package com.example.demo0810.Entity.etc;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RefreshEntity extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(length = 512)
    private String refresh;

    private String expiration;
}
