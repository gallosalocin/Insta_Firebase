package com.gallosalocin.instafirebase.model;

public class Comment {

    private String id;
    private String comment;
    private String publisher;

    public Comment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Comment(String id, String comment, String publisher) {
        this.id = id;
        this.comment = comment;
        this.publisher = publisher;
    }
}
