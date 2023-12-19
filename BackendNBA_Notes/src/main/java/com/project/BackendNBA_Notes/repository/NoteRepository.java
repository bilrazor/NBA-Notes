package com.project.BackendNBA_Notes.repository;

import com.project.BackendNBA_Notes.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    // Método para encontrar todas las notas pertenecientes a un usuario específico
    List<Note> findAllByUserId(Long userId);

    // Método para encontrar todas las notas favoritas de un usuario específico
    @Query("SELECT n FROM Note n WHERE n.user.id = :userId AND n.favorite = true")
    List<Note> findFavoritesByUserId(@Param("userId") Long userId);

}
