package com.project.nba_notes;

import java.util.Stack;

public class UndoRedoHelper {
    // Pilas para gestionar las operaciones de deshacer y rehacer.
    private Stack<NoteState> undoStack = new Stack<>();
    private Stack<NoteState> redoStack = new Stack<>();

    // Estado actual de la nota.
    private NoteState currentState;

    // Constructor de la clase.
    public UndoRedoHelper() {
        // Inicializar el estado actual con valores por defecto y un lastModifiedField vacío.
        currentState = new NoteState("", "", 0, 0, "");
    }

    // Obtener el estado actual.
    public NoteState getCurrentState() {
        return currentState;
    }

    // Llamado cuando hay un cambio en el título o contenido.
    public void onTextChanged(String title, String content, int titleCursorPosition, int contentCursorPosition, String lastModifiedField) {
        // Si el título o contenido han cambiado, actualizar las pilas.
        if (!title.equals(currentState.getTitle()) || !content.equals(currentState.getContent())) {
            // Guardar el estado actual en la pila de deshacer.
            undoStack.push(new NoteState(currentState.getTitle(), currentState.getContent(), currentState.getTitleCursorPosition(), currentState.getContentCursorPosition(), currentState.getLastModifiedField()));
            // Limpiar la pila de rehacer.
            redoStack.clear();
            // Actualizar el estado actual con los nuevos valores.
            currentState = new NoteState(title, content, titleCursorPosition, contentCursorPosition, lastModifiedField);
        }
    }

    // Establecer el estado inicial de la nota.
    public void setInitialState(String title, String content, int titleCursorPosition, int contentCursorPosition) {
        // Actualizar el estado actual y limpiar las pilas.
        currentState = new NoteState(title, content, titleCursorPosition, contentCursorPosition, "");
        undoStack.clear();
        redoStack.clear();
    }

    // Deshacer la última acción.
    public NoteState undo() {
        if (!undoStack.isEmpty()) {
            // Guardar el estado actual en la pila de rehacer.
            redoStack.push(new NoteState(currentState.getTitle(), currentState.getContent(), currentState.getTitleCursorPosition(), currentState.getContentCursorPosition(), currentState.getLastModifiedField()));
            // Restaurar el estado anterior.
            currentState = undoStack.pop();
        }
        return currentState;
    }

    // Rehacer la última acción deshecha.
    public NoteState redo() {
        if (!redoStack.isEmpty()) {
            // Guardar el estado actual en la pila de deshacer.
            undoStack.push(new NoteState(currentState.getTitle(), currentState.getContent(), currentState.getTitleCursorPosition(), currentState.getContentCursorPosition(), currentState.getLastModifiedField()));
            // Restaurar el estado rehecho.
            currentState = redoStack.pop();
        }
        return currentState;
    }

    // Verificar si la pila de deshacer está vacía.
    public boolean isUndoStackEmpty() {
        return undoStack.isEmpty();
    }

    // Verificar si la pila de rehacer está vacía.
    public boolean isRedoStackEmpty() {
        return redoStack.isEmpty();
    }
}
