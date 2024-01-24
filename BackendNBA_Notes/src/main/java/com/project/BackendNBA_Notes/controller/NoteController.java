package com.project.BackendNBA_Notes.controller;

import com.project.BackendNBA_Notes.dto.NoteDTO;
import com.project.BackendNBA_Notes.models.Note;
import com.project.BackendNBA_Notes.models.User;
import com.project.BackendNBA_Notes.repository.NoteRepository;
import com.project.BackendNBA_Notes.repository.UserRepository;
import com.project.BackendNBA_Notes.security.service.UserDetailsImpl;
import com.project.BackendNBA_Notes.service.DtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/notes")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DtoService dtoService;

    @PostMapping
    public ResponseEntity<NoteDTO> createNote(@RequestBody Note note, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        note.setUser(user);
        Note newNote = noteRepository.save(note);
        NoteDTO noteDTO = dtoService.convertToDTO(newNote);
        return ResponseEntity.ok(noteDTO);
    }

    @GetMapping("/{idNote}")
    public ResponseEntity<NoteDTO> getNoteById(@PathVariable Long idNote, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        Note note = noteRepository.findById(idNote)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        if (!note.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        NoteDTO noteDTO = dtoService.convertToDTO(note);
        return ResponseEntity.ok(noteDTO);
    }

    @GetMapping
    public List<NoteDTO> getAllNotes(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Note> notes = noteRepository.findAllByUserId(userDetails.getId());
        return notes.stream()
                .map(dtoService::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/favorites")
    public List<NoteDTO> getFavorites(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Note> notes = noteRepository.findFavoritesByUserId(userDetails.getId());
        return notes.stream()
                .map(dtoService::convertToDTO)
                .collect(Collectors.toList());
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
    public ResponseEntity<NoteDTO> updateNote(@PathVariable Long id,
                                              @RequestBody Note noteDetails,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getUser().getId().equals(userDetails.getId())) {
            throw new RuntimeException("Access denied");
        }

        // Comprueba si el título o el contenido han sido proporcionados
        if (noteDetails.getTitle() != null) {
            note.setTitle(noteDetails.getTitle());
        }
        if (noteDetails.getContent() != null) {
            note.setContent(noteDetails.getContent());
        }

        // Actualiza siempre el estado de favoritos
        note.setFavorite(noteDetails.isFavorite());

        Note updatedNote = noteRepository.save(note);
        NoteDTO noteDTO = dtoService.convertToDTO(updatedNote);

        return ResponseEntity.ok(noteDTO);
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
    @GetMapping("/title")
    public List<NoteDTO> searchNote(@RequestParam(name = "title", required=true) String title ,  @AuthenticationPrincipal UserDetailsImpl userDetails){
        List<Note> notes = noteRepository.searchNotesByUserId(userDetails.getId(),title);
        return notes.stream()
                .map(dtoService::convertToDTO)
                .collect(Collectors.toList());
    }
}

