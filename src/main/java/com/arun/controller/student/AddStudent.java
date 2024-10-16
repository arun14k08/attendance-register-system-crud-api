package com.arun.controller.student;

import com.arun.repo.CollegeRepo;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class AddStudent extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response){
        CollegeRepo collegeRepo = new CollegeRepo();
        String addStudentQuery = "insert into students(name, age, gender, teacherId) values(?,?,?,?)";
        try {
            Connection connection = collegeRepo.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(addStudentQuery);
            preparedStatement.setString(1, request.getParameter("name"));
            preparedStatement.setInt(2, Integer.parseInt(request.getParameter("age")));
            preparedStatement.setString(3, request.getParameter("gender"));
            preparedStatement.setInt(4, Integer.parseInt(request.getParameter("teacherId")));
            preparedStatement.executeUpdate();

            System.out.print("name: " + request.getParameter("name") + " ");
            System.out.print("age: " + request.getParameter("age") + " ");
            System.out.print("gender: " + request.getParameter("gender") + " ");
            System.out.println("teacherId: " + request.getParameter("teacherId"));
            System.out.println("Student added successfully!!!");

            JSONObject responseMessage = new JSONObject();
            responseMessage.put("message", "Student Added Successfully");

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter printWriter = response.getWriter();
            printWriter.println(responseMessage);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
