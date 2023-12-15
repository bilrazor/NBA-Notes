package com.project.BackendNBA_Notes.controller;

import com.project.BackendNBA_Notes.dto.NoteDTO;
import com.project.BackendNBA_Notes.models.Note;
import com.project.BackendNBA_Notes.models.User;
import com.project.BackendNBA_Notes.repository.NoteRepository;
import com.project.BackendNBA_Notes.repository.UserRepository;
import com.project.BackendNBA_Notes.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/notes")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<NoteDTO> createNote(@RequestBody Note note, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        note.setUser(user); // Asignar el usuario a la nota
        Note newNote = noteRepository.save(note);

        // Convertir Note a NoteDTO
        NoteDTO noteDTO = convertToDTO(newNote);
        return ResponseEntity.ok(noteDTO);
    }

    private NoteDTO convertToDTO(Note note) {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setId(note.getId());
        noteDTO.setContent(note.getContent());
        noteDTO.setFavorite(note.isFavorite());
        noteDTO.setLastModified(note.getLastModified());
        // Setear otros campos necesarios
        return noteDTO;
    }

    @GetMapping("/{idNote}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long idNote, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        Note note = noteRepository.findById(idNote)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        if (!note.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return ResponseEntity.ok(note);
    }

    @GetMapping
    public List<Note> getAllNotes(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return noteRepository.findAllByUserId(userDetails.getId());
    }

    @GetMapping("/favorites")
    public List<Note> getFavorites(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return noteRepository.findFavoritesByUserId(userDetails.getId());
    }

    @DeleteMapping("/list")
    public ResponseEntity<?> deleteMultipleNotes(@RequestBody List<Long> ids, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        for (Long id : ids) {
            Note note = noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Note not found"));
            if (!note.getUser().getId().equals(userDetails.getId())) {
                throw new RuntimeException("Access denied");
            }
        }

        ids.forEach(noteRepository::deleteById);
        return ResponseEntity.ok("Notas eliminadas con éxito");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note noteDetails, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Note not found"));
        if (!note.getUser().getId().equals(userDetails.getId())) {
            throw new RuntimeException("Access denied");
        }

        note.setContent(noteDetails.getContent());
        note.setFavorite(noteDetails.isFavorite());
        noteRepository.save(note);
        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Note not found"));
        if (!note.getUser().getId().equals(userDetails.getId())) {
            throw new RuntimeException("Access denied");
        }

        noteRepository.deleteById(id);
        return ResponseEntity.ok("Eliminado con exito");
    }
}
