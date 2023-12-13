package com.project.BackendNBA_Notes.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.Text;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String content;
    private boolean favorite;
    @Column
    private LocalDateTime lastModified;

    @PrePersist
    @PreUpdate
    private void setLastModified() {
        this.lastModified = LocalDateTime.now();
    }
}
