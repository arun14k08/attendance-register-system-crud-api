package com.arun.controller.record;

import com.arun.repo.CollegeRepo;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class RecordController extends HttpServlet {
    CollegeRepo collegeRepo = new CollegeRepo();
    Connection connection = null;
    String getRecordQuery = "select * " +
                            "from records";
    String addRecordQuery = "insert into records (teacherId, studentId, recordDate, isPresent) " +
                            "values(?,?,?,?)";
    String editRecordQuery = "update records " +
                             "set teacherId = ?, studentId = ?, recordDate = ?,isPresent = ?   " +
                             "where recordId = ?";
    String deleteRecordQuery = "delete from records " +
                               "where recordId = ?";

    public void doGet(HttpServletRequest request, HttpServletResponse response){
        try{
            if(connection == null) connection = collegeRepo.createConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(getRecordQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            ResultSetMetaData md = resultSet.getMetaData();
            int numCols = md.getColumnCount();
            JSONArray finalResultData = new JSONArray();
            while(resultSet.next()){
                JSONObject row = new JSONObject();
                for(int i = 0; i < numCols; i++){
                    String columnName = md.getColumnName(i+1);
                    row.put(columnName, resultSet.getObject(columnName));
                }
                finalResultData.put(row);
            }

            PrintWriter printWriter = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            printWriter.print(finalResultData);

            System.out.println("Records sent to client as JSON");
            preparedStatement.close();
            printWriter.close();

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response){
        try{
            if(connection == null) connection = collegeRepo.createConnection();
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


        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response){
        try{
            if(connection == null) connection = collegeRepo.createConnection();
            int recordId = Integer.parseInt(request.getParameter("recordId"));
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            int teacherId = Integer.parseInt(request.getParameter("teacherId"));
            Date recordDate = Date.valueOf(request.getParameter("recordDate"));
            boolean isPresent = Boolean.parseBoolean(request.getParameter("isPresent"));

            PreparedStatement preparedStatement = connection.prepareStatement(editRecordQuery);
            preparedStatement.setInt(1, teacherId);
            preparedStatement.setInt(2, studentId);
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


        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void  doDelete(HttpServletRequest request, HttpServletResponse response){
        try{
            if(connection == null) connection = collegeRepo.createConnection();
            int recordId = Integer.parseInt(request.getParameter("recordId"));

            PreparedStatement preparedStatement = connection.prepareStatement(deleteRecordQuery);

            preparedStatement.setInt(1, recordId);

            int affectedRows = preparedStatement.executeUpdate();


            if(affectedRows == 0){
                System.out.println("Invalid Request!!!");

                response.setStatus(400);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                JSONObject responseMessage = new JSONObject();
                responseMessage.put("message", "Invalid Request");
                PrintWriter printWriter = response.getWriter();
                printWriter.println(responseMessage);

            } else {
                System.out.println("Deleted the record successfully");
                this.doGet(request, response);
            }


        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
