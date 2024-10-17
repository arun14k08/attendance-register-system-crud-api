package com.arun.controller.student;

import com.arun.controller.util.Helper;
import com.arun.controller.util.MessengerServlet;
import com.arun.errorHandler.ClientErrorServlet;
import com.arun.errorHandler.ServerErrorServlet;
import com.arun.repo.CollegeRepo;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class StudentServlet extends HttpServlet {
    CollegeRepo collegeRepo = new CollegeRepo();
    ServerErrorServlet serverErrorServlet = new ServerErrorServlet();
    ClientErrorServlet clientErrorServlet = new ClientErrorServlet();
    MessengerServlet messengerServlet = new MessengerServlet();
    Connection connection = null;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String getAllStudentsQuery = "select * from students";
        try {
            if(connection == null)connection = collegeRepo.createConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getAllStudentsQuery);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int numCols = metaData.getColumnCount();
            JSONArray finalResultData = new JSONArray();
            while(resultSet.next()){
                JSONObject row = new JSONObject();
                for (int i = 0; i < numCols; i++) {
                    String columnName = metaData.getColumnName(i + 1);
                    row.put(columnName, resultSet.getObject(i + 1));
                }
                finalResultData.put(row);
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter printWriter = response.getWriter();
            printWriter.println(finalResultData);
            System.out.println("All Students Data sent to client as JSON");
            printWriter.close();
        } catch (SQLException e) {
            serverErrorServlet.sendInternalServerError(request, response, "SQL");
            throw new RuntimeException(e);
        } catch (IOException e){
            serverErrorServlet.sendInternalServerError(request, response, "IO");
            throw new RuntimeException(e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response){
        String addStudentQuery = "insert into students(name, age, gender) values(?,?,?)";
        Helper helper = new Helper();
        try {
            if(connection == null)connection = collegeRepo.createConnection();

            if(request.getParameter("name") == null){
                clientErrorServlet.sendErrorResponse(request, response, "Student name is not provided");
                return;
            }
            if(request.getParameter("age") == null){
                clientErrorServlet.sendErrorResponse(request, response, "Age is not provided");
                return;
            }
            if (request.getParameter("gender") == null) {
                clientErrorServlet.sendErrorResponse(request, response, "Gender is not provided");
                return;
            }
            String studentName = request.getParameter("name");
            int age = Integer.parseInt(request.getParameter("age"));
            String gender = request.getParameter("gender");

            PreparedStatement preparedStatement = connection.prepareStatement(addStudentQuery);
            preparedStatement.setString(1, studentName);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            preparedStatement.executeUpdate();

            System.out.print("name: " + studentName + " ");
            System.out.print("age: " + age + " ");
            System.out.print("gender: " + gender + " ");
            System.out.println("Student added successfully!!!");
            messengerServlet.sendMessage(request, response, "Student Added Successfully");

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response){
        String editStudentQuery = "update students set name = ?, age = ?, gender = ? where id = ?";
        try {
            String studentName = request.getParameter("name");
            int age = Integer.parseInt(request.getParameter("age"));
            String gender = request.getParameter("gender");

            int studentId = Integer.parseInt(request.getParameter("id"));

            if(connection == null) connection = collegeRepo.createConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(editStudentQuery);
            preparedStatement.setString(1, studentName);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            preparedStatement.setInt(4, studentId);

            JSONObject responseMessage = new JSONObject();

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            PrintWriter printWriter = response.getWriter();

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows == 0){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                responseMessage.put("message", "Invalid request");
                System.out.println("Invalid request!!");
            } else {
                responseMessage.put("message", "Student Edited successfully");
                System.out.println("Student Data edited for student id = " + studentId );
            }

            printWriter.println(responseMessage);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response){
        String deleteStudentQuery = "delete from students where id = ?";
        try {
            int studentId = Integer.parseInt(request.getParameter("id"));
            if(connection == null) connection = collegeRepo.createConnection();
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

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }


}
