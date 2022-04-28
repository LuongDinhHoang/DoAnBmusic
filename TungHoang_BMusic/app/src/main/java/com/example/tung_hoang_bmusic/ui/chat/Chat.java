package com.example.tung_hoang_bmusic.ui.chat;

/**
 * @author greg
 * @since 6/21/13
 */
public class Chat {

    private String message;
    private String author;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Chat() {
    }

    Chat(String message, String author) {
        this.message = message;
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "message='" + message + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
