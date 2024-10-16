package com.arun.controller.record;

import com.arun.repo.CollegeRepo;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteRecord extends HttpServlet {
    public void doDelete(HttpServletRequest request, HttpServletResponse response){
        String deleteRecordQuery = "delete from records where recordId = ?";
        CollegeRepo collegeRepo = new CollegeRepo();
        try {
            int recordId = Integer.parseInt(request.getParameter("recordId"));

            Connection connection = collegeRepo.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteRecordQuery);

            preparedStatement.setInt(1, recordId);

            int affectedRows = preparedStatement.executeUpdate();


            if(affectedRows == 0){
                System.out.println("Invalid Request!!");

                response.setStatus(400);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                JSONObject responseMessage = new JSONObject();
                responseMessage.put("message", "Invalid Request");
                PrintWriter printWriter = response.getWriter();
                printWriter.println(responseMessage);

            } else {
                System.out.println("Deleted the record successfully");
                response.sendRedirect("/get-all-records");
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
