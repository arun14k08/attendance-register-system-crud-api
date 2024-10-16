package com.arun.controller.record;

import com.arun.repo.CollegeRepo;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditRecord extends HttpServlet {
    public void doPut(HttpServletRequest request, HttpServletResponse response){
        CollegeRepo collegeRepo = new CollegeRepo();
        String editRecordQuery = "update records set teacherId = ?, studentId = ?, recordDate = ?,isPresent = ?   where recordId = ?";
        try {

            int recordId = Integer.parseInt(request.getParameter("recordId"));
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            int teacherId = Integer.parseInt(request.getParameter("teacherId"));
            Date recordDate = Date.valueOf(request.getParameter("recordDate"));
            boolean isPresent = Boolean.parseBoolean(request.getParameter("isPresent"));

            Connection connection =  collegeRepo.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(editRecordQuery);
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, teacherId);
            preparedStatement.setString(3, recordDate.toString());
            preparedStatement.setBoolean(4, isPresent);
            preparedStatement.setInt(5, recordId);

            int affectedRows = preparedStatement.executeUpdate();
            JSONObject responseMessage = new JSONObject();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter printWriter = response.getWriter();

            if(affectedRows == 0){
                response.setStatus(400);
                responseMessage.put("message", "Invalid request");
                System.out.println("Invalid request");
            } else {
                responseMessage.put("message", "Record Edited successfully");
                System.out.println("Record Edited successfully!!!");
            }

            printWriter.println(responseMessage);
            connection.close();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}
