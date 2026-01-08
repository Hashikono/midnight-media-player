package models;
import java.sql.Blob;


public class Playlist {
    public int id;
    public String name;
    public Blob image;

    public Playlist(int id, String name)
    {
        this.name = name;
    }

    public Playlist(String name)
    {
        this.name = name;
    }
}
