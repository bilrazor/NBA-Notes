package com.project.BackendNBA_Notes.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TokenBlacklist")
public class TokenBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime dateAdded = LocalDateTime.now();

    // Constructor, getters y setters

    public TokenBlacklist() {
    }

    public TokenBlacklist(String token) {
        this.token = token;
    }

    // Getters y setters para tokenId, token, dateAdded
}
