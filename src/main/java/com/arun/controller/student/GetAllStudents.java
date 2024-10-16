package com.arun.controller.student;

import com.arun.repo.CollegeRepo;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class GetAllStudents extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        CollegeRepo collegeRepo = new CollegeRepo();
        String getAllStudentsQuery = "select * from students";
        try {
            Connection connection = collegeRepo.createConnection();
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
            connection.close();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }

}
