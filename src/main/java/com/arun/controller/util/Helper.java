package com.arun.controller.util;

import com.arun.errorHandler.ServerErrorServlet;
import com.arun.repo.CollegeRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.*;

public class Helper {
    CollegeRepo collegeRepo = new CollegeRepo();
    ServerErrorServlet serverErrorServlet = new ServerErrorServlet();
    Connection connection = null;
    public boolean isStudentIdValid(int studentId) throws SQLException {
        String checkQuery = "select * from students where id = ?";
            if(connection == null) connection = collegeRepo.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(checkQuery);
            preparedStatement.setInt(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

    }
    public boolean isTeacherIdValid(int teacherId) throws SQLException {
        String checkQuery = "select * from teachers where id = ?";
            if(connection == null) connection = collegeRepo.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(checkQuery);
            preparedStatement.setInt(1, teacherId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
    }
    public boolean isRecordIdValid(int recordId){
        String checkQuery = "select * from records where recordId = ?";
        try {
            if(connection == null) connection = collegeRepo.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(checkQuery);
            preparedStatement.setInt(1, recordId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isRecordExists(int studentId, int teacherId, String recordDate) throws SQLException {
        String checkQuery = "select * from records where studentId = ? and  teacherId = ? and  recordDate = ?";
        if(connection == null) collegeRepo.createConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(checkQuery);
        preparedStatement.setInt(1, studentId);
        preparedStatement.setInt(2, teacherId);
        preparedStatement.setDate(3, Date.valueOf(recordDate));
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
            if(resultSet.next()) {
                int DBTeacherId = resultSet.getInt(1);
                int DBStudentId = resultSet.getInt(2);
                return DBStudentId == studentId && DBTeacherId == teacherId;
            }
            return false;
    }


    public void deleteStudentLink(int studentId) throws SQLException {
        String deleteLinkQuery = "delete from student_teachers_link where studentId = ?;";
        if(connection == null) connection = collegeRepo.createConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(deleteLinkQuery);
        preparedStatement.setInt(1, studentId);
        preparedStatement.executeUpdate();
    }

    public void deleteStudentRecords(int studentId) throws SQLException {
        String deleteRecordsQuery = "delete from records where studentId = ?";
        if(connection == null) connection = collegeRepo.createConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(deleteRecordsQuery);
        preparedStatement.setInt(1, studentId);
        preparedStatement.executeUpdate();
    }

}
