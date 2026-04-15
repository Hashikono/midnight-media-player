package models;
import java.sql.Blob;


public class Playlist {
    public int id;
    public String name;
    public Blob image;

    public Playlist(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Playlist(String name)
    {
        this.name = name;
    }

    public Playlist(String name, Blob image)
    {
        this.name = name;
        this.image = image;
    }

    public Playlist(int id, String name, Blob image)
    {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
