package com.project.BackendNBA_Notes.service;

import com.project.BackendNBA_Notes.dto.NoteDTO;
import com.project.BackendNBA_Notes.models.Note;
import org.springframework.stereotype.Service;

@Service
public class DtoService {
    public NoteDTO convertToDTO(Note note) {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setId(note.getId());
        noteDTO.setTitle(note.getTitle());
        noteDTO.setContent(note.getContent());
        noteDTO.setFavorite(note.isFavorite());
        noteDTO.setLastModified(note.getLastModified());
        noteDTO.setLatitude(note.getLatitude());
        noteDTO.setLongitude(note.getLongitude());
        return noteDTO;
    }
}