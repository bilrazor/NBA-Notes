package com.project.nba_notes;

import java.util.Date;

public class NoteDates {
    private String title;
    private Date lastModified;
    public NoteDates(String title, Date lastModified){
        this.title=title;
        this.lastModified=lastModified;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }


}
