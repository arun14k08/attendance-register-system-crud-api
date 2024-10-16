package com.arun.errorHandler;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

public class ClientErrorServlet extends HttpServlet {
    public void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject responseMessage = new JSONObject();
        responseMessage.put("message", errorMessage);
        PrintWriter printWriter = null;
        printWriter = response.getWriter();
        printWriter.println(responseMessage);
    }

}
