package com.arun.controller.record;

import com.arun.repo.CollegeRepo;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddRecord extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response){
        CollegeRepo collegeRepo = new CollegeRepo();
        String addRecordQuery = "insert into records (teacherId, studentId, recordDate, isPresent) values(?,?,?,?)";
        try {
            Connection connection = collegeRepo.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(addRecordQuery);
            preparedStatement.setString(1, request.getParameter("teacherId"));
            preparedStatement.setString(2, request.getParameter("studentId"));
            preparedStatement.setString(3, request.getParameter("recordDate"));
            preparedStatement.setBoolean(4, request.getParameter("isPresent").equals("true"));
            preparedStatement.executeUpdate();

            JSONObject responseMessage = new JSONObject();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter printWriter = response.getWriter();
            responseMessage.put("message", "Record Added successfully");
            printWriter.println(responseMessage);

            System.out.println("Record added successfully!!!");
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


}
