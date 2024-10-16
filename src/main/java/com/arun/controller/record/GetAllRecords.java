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

public class GetAllRecords extends HttpServlet {
        String getRecordQuery = "select * from records";
        public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            CollegeRepo collegeRepo = new CollegeRepo();

            try {
                Connection connection = collegeRepo.createConnection();
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
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
}
