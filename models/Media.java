package models;

public class Media {
    public int id;
    public String path;
    public String name;
    public String format;
    public String author;
    public String album;

    public Media(int id, String path, String name, String format, String author, String album)
    {
        this.id = id;
        this.path = path;
        this.name = name;
        this.format = format;
        this.author = author;
        this.album = album;
    }

    public Media(String path, String name, String format, String author, String album)
    {
        this.path = path;
        this.name = name;
        this.format = format;
        this.author = author;
        this.album = album;
    }

    public Media() {
        this.path = null;
        this.name = null;
        this.format = null;
        this.author = null;
        this.album = null;
    };
}
