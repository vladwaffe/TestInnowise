package com.imageservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ImageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private Byte[] image;

    private Long worker_id;
}
