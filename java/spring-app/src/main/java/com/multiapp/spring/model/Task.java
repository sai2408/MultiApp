package com.multiapp.spring.model;

public class Task {

    private long id;
    private String title;
    private boolean done;

    public Task() {
    }

    public Task(long id, String title, boolean done) {
        this.id = id;
        this.title = title;
        this.done = done;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
