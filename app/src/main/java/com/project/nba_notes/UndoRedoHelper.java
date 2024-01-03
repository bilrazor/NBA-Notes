package com.project.nba_notes;

import java.util.Stack;

public class UndoRedoHelper {
    private Stack<NoteState> undoStack = new Stack<>();
    private Stack<NoteState> redoStack = new Stack<>();
    private NoteState currentState;

    public UndoRedoHelper() {
        currentState = new NoteState("", "",0,0);
    }

    public NoteState getCurrentState() {
        return currentState;
    }

    // Llamado cuando hay un cambio en el título o contenido
    public void onTextChanged(String title, String content, int titleCursorPosition, int contentCursorPosition) {
        if (!title.equals(currentState.getTitle()) || !content.equals(currentState.getContent())) {
            undoStack.push(new NoteState(currentState.getTitle(), currentState.getContent(),currentState.getTitleCursorPosition(),currentState.getContentCursorPosition()));
            redoStack.clear();
            currentState = new NoteState(title, content, titleCursorPosition, contentCursorPosition);
        }
    }

    public NoteState undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(new NoteState(currentState.getTitle(), currentState.getContent(),currentState.getTitleCursorPosition(),currentState.getContentCursorPosition()));
            currentState = undoStack.pop();
        }
        return currentState;
    }

    public NoteState redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(new NoteState(currentState.getTitle(), currentState.getContent(),currentState.getTitleCursorPosition(),currentState.getContentCursorPosition()));
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