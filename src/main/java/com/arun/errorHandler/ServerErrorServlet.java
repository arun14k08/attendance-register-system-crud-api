package com.arun.errorHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

public class ServerErrorServlet {
    public void sendInternalServerError(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject responseMessage = new JSONObject();
        responseMessage.put("message", "Internal Server Error with" + message);
        PrintWriter printWriter = response.getWriter();
        printWriter.println(responseMessage);
    }
}
