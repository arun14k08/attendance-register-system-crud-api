package com.arun.repo;

import java.sql.*;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;

public class CollegeRepo {
    Connection connection = null;
    public Connection createConnection() throws SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/college_db";
            connection = DriverManager.getConnection(url, "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
