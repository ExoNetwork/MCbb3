/**************************************************************************************************
 * Copyright (c) 2013, Björn Heinrichs <manf@derpymail.org>                                       *
 * Permission to use, copy, modify, and/or distribute this software                               *
 * for any purpose with or without fee is hereby granted, provided                                *
 * that the above copyright notice and this permission notice appear in all copies.               *
 *                                                                                                *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD           *
 * TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS.              *
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL     *
 * DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,                 *
 * WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING                 *
 * OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.                          *
 **************************************************************************************************/

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
