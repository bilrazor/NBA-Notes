package com.project.nba_notes;

public class NoteState {
    private String title;
    private String content;
    private int titleCursorPosition;
    private int contentCursorPosition;


    public NoteState(String title, String content, int titleCursorPosition, int contentCursorPosition) {
        this.title = title;
        this.content = content;
        this.titleCursorPosition = titleCursorPosition;
        this.contentCursorPosition = contentCursorPosition;
    }

    // ... getters y setters para titleCursorPosition y contentCursorPosition ...

    public int getTitleCursorPosition() {
        return titleCursorPosition;
    }

    public void setTitleCursorPosition(int titleCursorPosition) {
        this.titleCursorPosition = titleCursorPosition;
    }

    public int getContentCursorPosition() {
        return contentCursorPosition;
    }

    public void setContentCursorPosition(int contentCursorPosition) {
        this.contentCursorPosition = contentCursorPosition;
    }

    // Métodos getters y setters aquí

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

