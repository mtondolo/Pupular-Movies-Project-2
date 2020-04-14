package com.example.popularmovies.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class MovieEntry {

    @PrimaryKey(autoGenerate = true)
    private int roomDbId;
    private String apiId;
    private String title;

    @Ignore
    public MovieEntry(String apiId, String title) {
        this.apiId = apiId;
        this.title = title;
    }

    public MovieEntry(int roomDbId, String apiId, String title) {
        this.roomDbId = roomDbId;
        this.apiId = apiId;
        this.title = title;
    }

    public int getRoomDbId() {
        return roomDbId;
    }

    public String getApiId() {
        return apiId;
    }

    public String getTitle() {
        return title;
    }

    public void setRoomDbId(int roomDbId) {
        this.roomDbId = roomDbId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
