package com.arun.controller.student;

import com.arun.repo.CollegeRepo;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditStudent extends HttpServlet {
    public void doPut(HttpServletRequest request, HttpServletResponse response){
        CollegeRepo collegeRepo = new CollegeRepo();
        try {
            String studentName = request.getParameter("name");
            int age = Integer.parseInt(request.getParameter("age"));
            String gender = request.getParameter("gender");
            int teacherId = Integer.parseInt(request.getParameter("teacherId"));
            int studentId = Integer.parseInt(request.getParameter("id"));

            Connection connection = collegeRepo.createConnection();
            String editStudentQuery = "update students set name = ?, age = ?, gender = ?, teacherId = ? where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(editStudentQuery);
            preparedStatement.setString(1, studentName);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            preparedStatement.setInt(4, teacherId);
            preparedStatement.setInt(5, studentId);

            JSONObject responseMessage = new JSONObject();

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            PrintWriter printWriter = response.getWriter();

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows == 0){
                response.setStatus(400);
                responseMessage.put("message", "Invalid request");
                System.out.println("Invalid request!!");
            } else {
                responseMessage.put("message", "Student Edited successfully");
                System.out.println("Student Data edited for student id = " + studentId );
            }

            printWriter.println(responseMessage);
            connection.close();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
