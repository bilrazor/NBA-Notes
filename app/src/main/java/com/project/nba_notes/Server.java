package com.project.nba_notes;

public class Server{
    public static String name = "http://10.0.2.2:8081";
}
/*@PutMapping("/{id}")
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
*/