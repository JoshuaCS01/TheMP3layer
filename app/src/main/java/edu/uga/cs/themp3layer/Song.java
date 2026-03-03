package edu.uga.cs.themp3layer;

import android.net.Uri;

import java.util.ArrayList;

public class Song {
    private long id;
    private String name;
    private String artist;
    private String genre;
    private String album;
    private String image;
    private Uri contentUri;

public Song(long id, String name, String artist, String genre, String album, String image, Uri contentUri ) {
    this.id = id;
    this.name = name;
    this. artist = artist;
    this. genre = genre;
    this.album = album;
    this.image = image;
    this.contentUri = contentUri;
}
    public long getId(){
        return id;
    }

    public String getName(){
        return name;
    }
    public String getArtist(){
        return artist;
    }

    public String getGenre(){
        return genre;
    }

    public String getAlbum(){
        return album;
    }

    public String getImage(){
        return image;
    }

    public Uri getContentUri(){
        return contentUri;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

