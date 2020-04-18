package com.example.popularmovies.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class MovieEntry {

    @PrimaryKey()
    private int id;
    private String title;

    @Ignore
    public MovieEntry(String title) {
        this.title = title;
    }

    public MovieEntry(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    public void setId(int id) {
        this.id = id;
    }

}
