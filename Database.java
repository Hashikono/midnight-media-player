import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import models.Media;
import models.Playlist;

public class Database {

    private static Connection connection;
    public static void main(String[] args) throws Exception {
        Class.forName("org.sqlite.JDBC");
        initialize();

        // Playlist temp = new Playlist();

        // temp.path = "C:/temp/path.mp3";
        // temp.name = "cool&awesome";
        // temp.format = "mp3";
        // temp.path = "imagine dragons";

        // int mediaID = createPlaylist(temp);
        // System.out.println(mediaID);

        // System.out.println(getMediaCount(1));
        // insertToPlaylist(1, 1);;
        // System.out.println(getMediaCount(1));

        // for(Media data : getMediaInPlaylist(1))
        // {
        //     System.out.println(data.name);
        // }

        System.out.println(findPlaylistById(1));

    }

    private static String getDatabaseUrl() throws Exception {
        Path dbPath = Paths.get(
            System.getenv("APPDATA"),
            "MidnightMedia",
            "midnightmedia.db"
        );

        Files.createDirectories(dbPath.getParent());
        return "jdbc:sqlite:" + dbPath.toString();
    }

    public static Connection getConnection() throws Exception {
        if(connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(getDatabaseUrl());
        }

        return connection;
    }

    public static void initialize() throws Exception {
        try(Statement statement = getConnection().createStatement()) {
            statement.execute("""
                CREATE TABLE IF NOT EXISTS media (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                path TEXT NOT NULL,
                name TEXT NOT NULL,
                format TEXT NOT NULL,
                author TEXT,
                album TEXT
                )"""
            );

            statement.execute("""
                CREATE TABLE IF NOT EXISTS playlist (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL
                )"""
            );

            statement.execute("""
                CREATE TABLE IF NOT EXISTS playlist_media (
                playlist_id INTEGER NOT NULL,
                media_id INTEGER NOT NULL,
                position INTEGER NOT NULL,
                probability REAL DEFAULT 1,

                PRIMARY KEY (playlist_id, media_id),

                FOREIGN KEY (playlist_id)
                    REFERENCES playlist(id)
                    ON DELETE CASCADE,

                FOREIGN KEY (media_id)
                    REFERENCES media(id)
                    ON DELETE CASCADE
                )"""
            );
        }
    }

    public static void addToPlaylist(int playlistId, int mediaId, int position) throws Exception {
        String sql = """
            INSERT INTO playlist_media (playlist_id, media_id, position)
        """;
        
        try(PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            ps.setInt(2, mediaId);
            ps.setInt(3, position);

            ps.executeUpdate();
        }
    }

//#region MediaFiles
    public static int insertMedia(Media data) throws Exception {
        String sql = """
            INSERT INTO media (path, name, format, author, album)
            VALUES (?, ?, ?, ?, ?)
        """;

        try(PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, data.path);
            ps.setString(2, data.name);
            ps.setString(3, data.format);
            ps.setString(4, data.author);
            ps.setString(5, data.album);

            ps.executeUpdate();

            try(ResultSet keys = ps.getGeneratedKeys()) {
                if(keys.next()) {
                    return keys.getInt(1);
                }
            }

            throw new RuntimeException("Failed to insert media");
        }
    }

    public static List<Media> getAllMedia() throws Exception {
        List<Media> mediaList = new ArrayList<Media>();
        String sql = "SELECT * FROM media";

        try(PreparedStatement ps = Database.getConnection().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                Media data = new Media(
                    rs.getString("path"),
                    rs.getString("name"),
                    rs.getString("format"),
                    rs.getString("author"),
                    rs.getString("album")
                );

                mediaList.add(data);
            }
        }


        return mediaList;
    }

    public static List<Media> getMediaInPlaylist(int playlist) throws Exception {
        List<Media> mediaList = new ArrayList<Media>();
        String sql1 = "SELECT * FROM playlist_media WHERE playlist_id = ?";

        try(PreparedStatement ps1 = Database.getConnection().prepareStatement(sql1)) {
            ps1.setInt(1, playlist);

            try(ResultSet rs1 = ps1.executeQuery()) {
                while(rs1.next()) {
                    String sql2 = "SELECT * FROM media WHERE id = ?";

                    try(PreparedStatement ps2 = Database.getConnection().prepareStatement(sql2)) {
                        ps2.setInt(1, rs1.getInt("media_id"));

                        try(ResultSet rs2 = ps2.executeQuery()) {
                            while(rs2.next())
                            {
                                Media data = new Media(
                                    rs2.getString("path"),
                                    rs2.getString("name"),
                                    rs2.getString("format"),
                                    rs2.getString("author"),
                                    rs2.getString("album")
                                );

                                mediaList.add(data);
                            }
                        }   
                    }
                }
            }
        }

        return mediaList;
    }

    public static Media findMediaById(int id) throws Exception {
        String sql = "SELECT * FROM media WHERE id = ?";

        try(PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next())
                {
                    return new Media(
                        rs.getString("path"),
                        rs.getString("name"),
                        rs.getString("format"),
                        rs.getString("author"),
                        rs.getString("album")
                    );
                }
            }   
        }

        throw new RuntimeException("Failed find media");
    }


//#endregion MediaFiles

//#region Playlists
    public static int createPlaylist(Playlist data) throws Exception {
        String sql = """
            INSERT INTO playlist (name)
            VALUES (?)
        """;

        try(PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, data.name);

            ps.executeUpdate();

            try(ResultSet keys = ps.getGeneratedKeys()) {
                if(keys.next()) {
                    return keys.getInt(1);
                }
            }

            throw new RuntimeException("Failed to create playlist");
        }
    }

    public static void insertToPlaylist(int playlist, int media) throws Exception {
        String sql = """
            INSERT INTO playlist_media (playlist_id, media_id, position)
            VALUES (?, ?, ?)
        """;

        int playlistMediaCount = getMediaCount(playlist);
        try(PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, playlist);
            ps.setInt(2, media);
            ps.setInt(3, playlistMediaCount);

            ps.executeUpdate();
        }
    }

    public static int getMediaCount(int playlistId) throws Exception {

        String sql = """
            SELECT COUNT(*)
            FROM playlist_media
            WHERE playlist_id = ?
        """;

        try (PreparedStatement ps =
                 Database.getConnection().prepareStatement(sql)) {

            ps.setInt(1, playlistId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }

    public static List<Playlist> getAllPlaylists() throws Exception {
        List<Playlist> mediaList = new ArrayList<Playlist>();
        String sql = "SELECT * FROM playlist";

        try(PreparedStatement ps = Database.getConnection().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                Playlist data = new Playlist(
                    rs.getString("name")
                );

                mediaList.add(data);
            }
        }


        return mediaList;
    }

    public static Playlist findPlaylistById(int id) throws Exception {
        String sql = "SELECT * FROM playlist WHERE id = ?";

        try(PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next())
                {
                    return new Playlist(
                        rs.getString("name")
                    );
                }
            }   
        }

        throw new RuntimeException("Failed find playlist");
    }
//#endregion Playlists
}
