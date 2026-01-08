package models;
import java.sql.Blob;


public class Playlist {
    public String name;
    public Blob image;

    public Playlist(String name)
    {
        this.name = name;
    }
}
