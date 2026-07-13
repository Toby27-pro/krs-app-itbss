package com.mycompany.krs_sistem.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connection {

    public static Connection koneksi() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/krs_akademik", "root", "");
        } catch (ClassNotFoundException e) {
            System.err.println("Class Not Found : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("SQL Error : " + e.getMessage());
        }
        return conn;
    }
}
