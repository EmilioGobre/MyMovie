package com.example.mymovie.POJO;

public class POJO {
    private final String title;
    private float rating;
    private final String Year;
    private final int image;

    public POJO(String title, String description, int image) {
        this.title = title;
        this.Year = description;
        this.image = image;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public float getRating() {
        return rating;
    }

    public String getYear() {
        return Year;
    }

    public int getImage() {
        return image;
    }
}

