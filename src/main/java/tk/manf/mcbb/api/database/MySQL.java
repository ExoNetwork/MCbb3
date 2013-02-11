package tk.manf.mcbb.api.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQL {
    private String hostname, port;
    private Connection conn;

    public MySQL(String hostname, String port) {
        this.hostname = hostname;
        this.port = port;
    }

    public boolean connect(String username, String password, String database) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + hostname + ":"
                    + port + "/" + database + "?" + "user=" + username + "&"
                    + "password=" + password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
