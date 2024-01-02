package com.project.nba_notes;

import java.util.Stack;

public class UndoRedoHelper {
    private Stack<String> undoStack = new Stack<>();
    private Stack<String> redoStack = new Stack<>();
    private String currentState = ""; // Añadido para mantener el estado actual del texto

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String text) {
        currentState = text;
    }

    public void onTextChanged(String text) {
        undoStack.push(currentState);
        redoStack.clear();
        currentState = text;
    }

    public String undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(currentState);
            currentState = undoStack.pop();
            return currentState;
        } else {
            return currentState; // Devuelve el estado actual si la pila de deshacer está vacía
        }
    }

    public String redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(currentState);
            currentState = redoStack.pop();
            return currentState;
        } else {
            return currentState; // Devuelve el estado actual si la pila de rehacer está vacía
        }
    }

    public boolean isUndoStackEmpty() {
        return undoStack.isEmpty();
    }

    public boolean isRedoStackEmpty() {
        return redoStack.isEmpty();
    }
}
