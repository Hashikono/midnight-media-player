package models;

public class Media {
    public String path;
    public String name;
    public String format;
    public String author;
    public String album;

    public Media(String path, String name, String format, String author, String album)
    {
        this.path = path;
        this.name = name;
        this.format = format;
        this.author = author;
        this.album = album;
    };
}
