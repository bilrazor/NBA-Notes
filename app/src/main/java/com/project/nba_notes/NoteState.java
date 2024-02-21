package com.project.nba_notes;

public class NoteState {
    private String title;
    private String content;
    private int titleCursorPosition;
    private int contentCursorPosition;
    private String lastModifiedField;

    public NoteState(String title, String content, int titleCursorPosition, int contentCursorPosition, String lastModifiedField) {
        this.title = title;
        this.content = content;
        this.titleCursorPosition = titleCursorPosition;
        this.contentCursorPosition = contentCursorPosition;
        this.lastModifiedField = lastModifiedField;
    }

    // Getters y setters para title, content, titleCursorPosition y contentCursorPosition

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

    // Getters y setters para lastModifiedField

    public String getLastModifiedField() {
        return lastModifiedField;
    }

    public void setLastModifiedField(String lastModifiedField) {
        this.lastModifiedField = lastModifiedField;
    }
}
