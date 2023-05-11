package com.example.cinema;

public class resFilm {
    private  String title, description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public resFilm(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
