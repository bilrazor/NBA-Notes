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


    @GetMapping("/title")
    public List<NoteDTO> searchNote(@RequestParam(name = "title", required=true) String title ,  @AuthenticationPrincipal UserDetailsImpl userDetails){
        List<Note> notes = noteRepository.searchNotesByUserId(userDetails.getId(),title);
        return notes.stream()
                .map(dtoService::convertToDTO)
                .collect(Collectors.toList());
    }

      @Query("SELECT n FROM Note n WHERE n.user.id = :userId AND n.title LIKE :title%")
    List<Note> searchNotesByUserId(@Param("userId") Long userId, @Param("title") String title);

*/

//set.error en los editext
//AÑADIR el title en el backend a la hora de parsearlo


/*@Getter
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
noteDTO.setLatitude(note.getLatitude());
        noteDTO.setLongitude(note.getLongitude());

 */

