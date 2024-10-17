package com.arun.controller.record;

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

import java.io.*;
import java.sql.*;

public class RecordServlet extends HttpServlet {
    CollegeRepo collegeRepo = new CollegeRepo();
    ServerErrorServlet serverErrorServlet = new ServerErrorServlet();
    ClientErrorServlet clientErrorServlet = new ClientErrorServlet();
    MessengerServlet messengerServlet = new MessengerServlet();
    Helper helper = new Helper();
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

        messengerServlet.sendMessage(request, response, finalResultData);
        System.out.println("Records sent to client as JSON");
        preparedStatement.close();

        } catch (SQLException e) {
            serverErrorServlet.sendInternalServerError(request, response, "SQL");
            throw new RuntimeException(e);
        } catch (IOException e){
            serverErrorServlet.sendInternalServerError(request, response, "IO");
            throw new RuntimeException(e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String addRecordQuery = "insert into records (teacherId, studentId, recordDate, isPresent) " +
                "values(?,?,?,?)";
        try{
            if(request.getParameter("teacherId") == null){
                clientErrorServlet.sendErrorResponse(request, response, "Teacher ID is not provided");
                return;
            }
            if(request.getParameter("studentId") == null){
                clientErrorServlet.sendErrorResponse(request, response, "Student ID is not provided");
                return;
            }
            if(request.getParameter("recordDate") == null){
                clientErrorServlet.sendErrorResponse(request, response, "record Date is not provided");
                return;
            }
            if(request.getParameter("isPresent") == null){
                clientErrorServlet.sendErrorResponse(request, response, "isPresent value is not provided");
                return;
            }
            int teacherId = Integer.parseInt(request.getParameter("teacherId"));
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            String date = request.getParameter("recordDate");
            boolean isPresent = request.getParameter("isPresent").equals("true");

            if(!helper.isStudentIdValid(studentId)){
                clientErrorServlet.sendErrorResponse(request, response, "Student ID is invalid");
                return;
            }
            if(!helper.isTeacherIdValid(teacherId)){
                clientErrorServlet.sendErrorResponse(request, response, "Teacher ID is invalid");
                return;
            }
            if(!helper.isTeacherStudentLinked(teacherId, studentId)){
                clientErrorServlet.sendErrorResponse(request, response, "Student is not linked with the provided teacher");
                return;
            }
            if(helper.isRecordExists(studentId, teacherId, date)){
                clientErrorServlet.sendErrorResponse(request, response, "Record for the same date exists");
                return;
            }


            if(connection == null) connection = collegeRepo.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(addRecordQuery);
            preparedStatement.setInt(1, teacherId);
            preparedStatement.setInt(2, studentId);
            preparedStatement.setString(3, date);
            preparedStatement.setBoolean(4, isPresent);
            preparedStatement.executeUpdate();

            messengerServlet.sendMessage(request, response, "Record Added successfully");
            System.out.println("Record added successfully!!!");
            preparedStatement.close();


        } catch (SQLException e) {
            serverErrorServlet.sendInternalServerError(request, response, "SQL");
            throw new RuntimeException(e);
        } catch (IOException e){
            serverErrorServlet.sendInternalServerError(request, response, "IO");
            throw new RuntimeException(e);
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response){
        String editRecordQuery = "update records " +
                "set teacherId = ?, studentId = ?, recordDate = ?,isPresent = ?   " +
                "where recordId = ?";
        try{
            if(request.getParameter("recordId") == null){
                clientErrorServlet.sendErrorResponse(request, response, "Record ID is not provided");
                return;
            }
            if(request.getParameter("teacherId") == null){
                clientErrorServlet.sendErrorResponse(request, response, "Teacher ID is not provided");
                return;
            }
            if(request.getParameter("studentId") == null){
                clientErrorServlet.sendErrorResponse(request, response, "Student ID is not provided");
                return;
            }
            if(request.getParameter("recordDate") == null){
                clientErrorServlet.sendErrorResponse(request, response, "record Date is not provided");
                return;
            }
            if(request.getParameter("isPresent") == null){
                clientErrorServlet.sendErrorResponse(request, response, "isPresent value is not provided");
                return;
            }
            int recordId = Integer.parseInt(request.getParameter("recordId"));
            int teacherId = Integer.parseInt(request.getParameter("teacherId"));
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            String date = request.getParameter("recordDate");
            boolean isPresent = request.getParameter("isPresent").equals("true");

            if(!helper.isStudentIdValid(studentId)){
                clientErrorServlet.sendErrorResponse(request, response, "Student ID is invalid");
                return;
            }
            if(!helper.isTeacherIdValid(teacherId)){
                clientErrorServlet.sendErrorResponse(request, response, "Teacher ID is invalid");
                return;
            }
            if(!helper.isRecordIdValid(recordId)){
                clientErrorServlet.sendErrorResponse(request, response, "Record ID is invalid");
                return;
            }
            if(!helper.isTeacherStudentLinked(teacherId, studentId)){
                clientErrorServlet.sendErrorResponse(request, response, "Student is not linked with the provided teacher");
                return;
            }
            if(helper.isRecordExists(studentId, teacherId, date)){
                clientErrorServlet.sendErrorResponse(request, response, "Record for the same date exists");
                return;
            }

            if(connection == null) connection = collegeRepo.createConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(editRecordQuery);
            preparedStatement.setInt(1, teacherId);
            preparedStatement.setInt(2, studentId);
            preparedStatement.setString(3, date);
            preparedStatement.setBoolean(4, isPresent);
            preparedStatement.setInt(5, recordId);

            preparedStatement.executeUpdate();
            messengerServlet.sendMessage(request, response, "Record Edited successfully");
            System.out.println("Record Edited successfully!!!");

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void  doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String deleteRecordQuery = "delete from records where recordId = ?";
        try{
            if(connection == null) connection = collegeRepo.createConnection();
            int recordId = Integer.parseInt(request.getParameter("recordId"));

            if(!helper.isRecordIdValid(recordId)){
                clientErrorServlet.sendErrorResponse(request,response,"Record ID is invalid");
                return;
            }
            PreparedStatement preparedStatement = connection.prepareStatement(deleteRecordQuery);
            preparedStatement.setInt(1, recordId);
            preparedStatement.executeUpdate();
            messengerServlet.sendMessage(request, response, "Record Deleted Successfully");
            System.out.println("Record deleted successfully");

        } catch (SQLException e ) {
            serverErrorServlet.sendInternalServerError(request, response, "SQL");
            throw new RuntimeException(e);
        } catch (IOException e){
            serverErrorServlet.sendInternalServerError(request, response, "IO");
            throw new RuntimeException(e);
        }
    }



}
