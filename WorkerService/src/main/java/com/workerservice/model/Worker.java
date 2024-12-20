package com.workerservice.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String imageUrl;
    private String manager;
    private String status;
    @Column(nullable = false)
    private boolean deleted = false;
}
