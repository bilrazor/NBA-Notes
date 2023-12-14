package com.project.BackendNBA_Notes.controller;

import com.project.BackendNBA_Notes.models.Note;
import com.project.BackendNBA_Notes.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/notes")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;
    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        Note newNote = noteRepository.save(note);
        return ResponseEntity.ok(newNote);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Note not found"));
        return ResponseEntity.ok(note);
    }

    @GetMapping
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @GetMapping("/favorites")
    public List<Note> getFavorites(){
        return  noteRepository.getFavorites();
    }
    @DeleteMapping("/list")
    public ResponseEntity<?> deleteMultipleNotes(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest().body("No IDs provided");
        }

        ids.forEach(noteRepository::deleteById);
        return ResponseEntity.ok("Notas eliminadas con éxito");
    }
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note noteDetails) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Note not found"));
        note.setContent(noteDetails.getContent());
        note.setFavorite(noteDetails.isFavorite());
        noteRepository.save(note);
        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
        noteRepository.deleteById(id);
        return ResponseEntity.ok("Eliminado con exito");
    }
}

