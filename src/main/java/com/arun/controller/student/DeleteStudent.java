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

public class DeleteStudent extends HttpServlet {
    public void doDelete(HttpServletRequest request, HttpServletResponse response){
        String deleteStudentQuery = "delete from students where id = ?";
        CollegeRepo collegeRepo = new CollegeRepo();
        try {
            int studentId = Integer.parseInt(request.getParameter("id"));
            Connection connection = collegeRepo.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteStudentQuery);
            preparedStatement.setInt(1, studentId);

            int affectedRows = preparedStatement.executeUpdate();

            if(affectedRows == 0){
                System.out.println("Invalid Request!!");

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                JSONObject responseMessage = new JSONObject();
                responseMessage.put("message", "Invalid Request");
                PrintWriter printWriter = response.getWriter();
                printWriter.println(responseMessage);

            } else {
                System.out.println("Deleted the student successfully");
                response.sendRedirect("/get-all-students");
            }
            connection.close();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
