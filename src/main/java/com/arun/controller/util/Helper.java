package com.arun.controller.util;

import com.arun.errorHandler.ServerErrorServlet;
import com.arun.repo.CollegeRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Helper {
    CollegeRepo collegeRepo = new CollegeRepo();
    ServerErrorServlet serverErrorServlet = new ServerErrorServlet();
    Connection connection = null;
    public boolean isStudentExists(int studentId) throws SQLException {
        String checkQuery = "select * from students where id = ?";
            if(connection == null) connection = collegeRepo.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(checkQuery);
            preparedStatement.setInt(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

    }
    public boolean isTeacherExists(int teacherId) throws SQLException {
        String checkQuery = "select * from teachers where id = ?";
            if(connection == null) connection = collegeRepo.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(checkQuery);
            preparedStatement.setInt(1, teacherId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();


    }

    public boolean isTeacherStudentLinked(int teacherId, int studentId) throws SQLException {
        String checkQuery = "select * from student_teachers_link where teacherId = ? and studentId = ?";
            if(connection == null) collegeRepo.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(checkQuery);
            preparedStatement.setInt(1, teacherId);
            preparedStatement.setInt(2, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int DBTeacherId= resultSet.getInt(1);
            int DBStudentId = resultSet.getInt(2);
            return DBStudentId == studentId && DBTeacherId == teacherId;
    }
}
