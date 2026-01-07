import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Database {

    private static Connection connection;
    public static void main(String[] args) throws Exception {
        Class.forName("org.sqlite.JDBC");

        // Connection conn = DriverManager.getConnection("jdbc:sqlite:midnightmedia.db");

        // System.out.println("Connected to SQLite");
        // conn.close();

        initialize();
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
}
