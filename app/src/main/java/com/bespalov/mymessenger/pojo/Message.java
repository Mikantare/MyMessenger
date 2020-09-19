package com.bespalov.mymessenger.pojo;

public class Message {
    private String author;
    private String textMessage;
    private long date;
    private String imageUrl;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Message(String author, String textMessage, long date, String imageUrl) {
        this.author = author;
        this.textMessage = textMessage;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    public Message() {
    }
}
