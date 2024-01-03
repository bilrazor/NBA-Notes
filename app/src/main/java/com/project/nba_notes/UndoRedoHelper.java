package com.project.nba_notes;

import java.util.Stack;

public class UndoRedoHelper {
    private Stack<NoteState> undoStack = new Stack<>();
    private Stack<NoteState> redoStack = new Stack<>();
    private NoteState currentState;

    public UndoRedoHelper() {
        // Inicializar el estado actual con valores por defecto y un lastModifiedField vacío
        currentState = new NoteState("", "", 0, 0, "");
    }


    public NoteState getCurrentState() {
        return currentState;
    }

    // Llamado cuando hay un cambio en el título o contenido
    public void onTextChanged(String title, String content, int titleCursorPosition, int contentCursorPosition, String lastModifiedField) {
        if (!title.equals(currentState.getTitle()) || !content.equals(currentState.getContent())) {
            undoStack.push(new NoteState(currentState.getTitle(), currentState.getContent(), currentState.getTitleCursorPosition(), currentState.getContentCursorPosition(), currentState.getLastModifiedField()));
            redoStack.clear();
            currentState = new NoteState(title, content, titleCursorPosition, contentCursorPosition, lastModifiedField);
        }
    }
    public void setInitialState(String title, String content, int titleCursorPosition, int contentCursorPosition) {
        currentState = new NoteState(title, content, titleCursorPosition, contentCursorPosition, "");
        undoStack.clear();
        redoStack.clear();
    }




    public NoteState undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(new NoteState(currentState.getTitle(), currentState.getContent(), currentState.getTitleCursorPosition(), currentState.getContentCursorPosition(), currentState.getLastModifiedField()));
            currentState = undoStack.pop();
        }
        return currentState;
    }

    public NoteState redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(new NoteState(currentState.getTitle(), currentState.getContent(), currentState.getTitleCursorPosition(), currentState.getContentCursorPosition(), currentState.getLastModifiedField()));
            currentState = redoStack.pop();
        }
        return currentState;
    }


    public boolean isUndoStackEmpty() {
        return undoStack.isEmpty();
    }

    public boolean isRedoStackEmpty() {
        return redoStack.isEmpty();
    }
}