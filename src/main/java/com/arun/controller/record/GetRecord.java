package com.arun.controller.record;

import com.arun.repo.CollegeRepo;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.sql.*;

public class GetRecord extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) {

        String getOneRecordQuery = "select * from records where teacherId = ? ";
        CollegeRepo collegeRepo = new CollegeRepo();
        try {
            int  teacherId = Integer.parseInt(request.getParameter("teacherId"));

            Connection connection = collegeRepo.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(getOneRecordQuery);
            preparedStatement.setInt(1, teacherId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData md = resultSet.getMetaData();
            int numCols = md.getColumnCount();

            JSONArray finalResultData = new JSONArray();
            while (resultSet.next()) {
                JSONObject row = new JSONObject();
                for (int i = 0; i < numCols; i++) {
                    String columnName = md.getColumnName(i + 1);
                    row.put(columnName, resultSet.getObject(columnName));
                }
                finalResultData.put(row);
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter printWriter = response.getWriter();
            printWriter.println(finalResultData);

            System.out.println("Records for teacher ID " + teacherId + " sent to client successfully");
            printWriter.close();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
