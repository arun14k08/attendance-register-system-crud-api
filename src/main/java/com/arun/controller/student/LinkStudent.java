package com.arun.controller.student;

import com.arun.controller.util.Helper;
import com.arun.controller.util.MessengerServlet;
import com.arun.errorHandler.ClientErrorServlet;
import com.arun.errorHandler.ServerErrorServlet;
import com.arun.repo.CollegeRepo;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LinkStudent extends HttpServlet {
    CollegeRepo collegeRepo = new CollegeRepo();
    ServerErrorServlet serverErrorServlet = new ServerErrorServlet();
    ClientErrorServlet clientErrorServlet = new ClientErrorServlet();
    Helper helper = new Helper();
    MessengerServlet messengerServlet = new MessengerServlet();
    Connection connection = null;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        int teacherId = Integer.parseInt(request.getParameter("teacherId"));
        String addStudentTeacherLinkQuery = "insert into student_teachers_link (teacherId, studentId) values (?, ?)";

        try {
            if(connection == null) connection = collegeRepo.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(addStudentTeacherLinkQuery);

            if(!helper.isTeacherIdValid(teacherId) && !helper.isStudentIdValid(studentId)){
                clientErrorServlet.sendErrorResponse(request, response, ":Both Student and Teacher ID is Invalid");
                return;
            }
            if(!helper.isTeacherIdValid(teacherId)){
                clientErrorServlet.sendErrorResponse(request, response, "Given Teacher Id is Invalid");
                return;
            }
            if(!helper.isStudentIdValid(studentId)){
                clientErrorServlet.sendErrorResponse(request, response, "Given Student Id is Invalid");
                return;
            }
            if(helper.isTeacherStudentLinked(teacherId, studentId)){
                clientErrorServlet.sendErrorResponse(request, response, "Teacher and Student are already linked");
                return;
            }

            preparedStatement.setInt(1, teacherId);
            preparedStatement.setInt(2, studentId);
            preparedStatement.executeUpdate();
            messengerServlet.sendMessage(request, response, "Student Linked Successfully");

        } catch (SQLException e) {
            serverErrorServlet.sendInternalServerError(request, response, "SQL");
            throw new RuntimeException(e);
        }
    }

}
