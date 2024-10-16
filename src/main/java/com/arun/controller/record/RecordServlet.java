package com.arun.controller.record;

import com.arun.errorHandler.ClientErrorServlet;
import com.arun.errorHandler.ServerErrorServlet;
import com.arun.repo.CollegeRepo;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.sql.*;

public class RecordServlet extends HttpServlet {
    CollegeRepo collegeRepo = new CollegeRepo();
    ServerErrorServlet serverErrorServlet = new ServerErrorServlet();
    ClientErrorServlet clientErrorServlet = new ClientErrorServlet();
    Connection connection = null;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String getRecordQuery = "select * from records";
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

        } catch (SQLException e) {
            serverErrorServlet.sendInternalServerError(request, response, "SQL");
            throw new RuntimeException(e);
        } catch (IOException e){
            serverErrorServlet.sendInternalServerError(request, response, "IO");
            throw new RuntimeException(e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response){
        String addRecordQuery = "insert into records (teacherId, studentId, recordDate, isPresent) " +
                "values(?,?,?,?)";
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
        String editRecordQuery = "update records " +
                "set teacherId = ?, studentId = ?, recordDate = ?,isPresent = ?   " +
                "where recordId = ?";
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
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
    public void  doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String deleteRecordQuery = "delete from records where recordId = ?";
        try{
            if(connection == null) connection = collegeRepo.createConnection();
            int recordId = Integer.parseInt(request.getParameter("recordId"));
            boolean recordExists = findIfRecordExist(recordId);
            if(recordExists){
                PreparedStatement preparedStatement = connection.prepareStatement(deleteRecordQuery);
                preparedStatement.setInt(1, recordId);
                preparedStatement.executeUpdate();
                System.out.println("Record deleted");
                this.doGet(request, response);
            } else {
                clientErrorServlet.sendErrorResponse(request, response, "Given Record ID is Invalid");
            }

        } catch (SQLException e ) {
            serverErrorServlet.sendInternalServerError(request, response, "SQL");
            throw new RuntimeException(e);
        } catch (IOException e){
            serverErrorServlet.sendInternalServerError(request, response, "IO");
            throw new RuntimeException(e);
        }
    }

    public Boolean findIfRecordExist(int recordId) throws SQLException{
        String findWithIdQuery = "select * from records where recordId = ?";
        if(connection == null) connection = collegeRepo.createConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(findWithIdQuery);
        preparedStatement.setInt(1, recordId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }


}
