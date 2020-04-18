package com.example.popularmovies.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class MovieEntry {

    @PrimaryKey(autoGenerate = true)
    private int roomDbId;
    private int id;
    private String title;

    @Ignore
    public MovieEntry(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public MovieEntry(int roomDbId, int id, String title) {
        this.roomDbId = roomDbId;
        this.id = id;
        this.title = title;
    }

    public int getRoomDbId() {
        return roomDbId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setRoomDbId(int roomDbId) {
        this.roomDbId = roomDbId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
