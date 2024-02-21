package com.project.BackendNBA_Notes.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NoteDTO {
    private Long id;
    private String title;
    private String content;
    private boolean favorite;
    private LocalDateTime lastModified;
    private Double latitude;
    private Double longitude;
    // Getter y setters
}
