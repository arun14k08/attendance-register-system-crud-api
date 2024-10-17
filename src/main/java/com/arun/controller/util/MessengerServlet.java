package com.arun.controller.util;

import com.arun.errorHandler.ServerErrorServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

public class MessengerServlet extends HttpServlet {
    ServerErrorServlet serverErrorServlet = new ServerErrorServlet();
    public void sendMessage(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JSONObject responseMessage = new JSONObject();
            responseMessage.put("message", message);
            PrintWriter printWriter = response.getWriter();
            printWriter.println(responseMessage);
        } catch (IOException e) {
            serverErrorServlet.sendInternalServerError(request, response, "IO");
            throw new RuntimeException(e);
        }
    }
    public void sendMessage(HttpServletRequest request, HttpServletResponse response, JSONArray data) throws IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JSONObject responseMessage = new JSONObject();
            responseMessage.put("data", data);
            PrintWriter printWriter = response.getWriter();
            printWriter.println(responseMessage);
        } catch (IOException e) {
            serverErrorServlet.sendInternalServerError(request, response, "IO");
            throw new RuntimeException(e);
        }
    }
    public void sendMessage(HttpServletRequest request, HttpServletResponse response, JSONObject data) throws IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JSONObject responseMessage = new JSONObject();
            responseMessage.put("data", data);
            PrintWriter printWriter = response.getWriter();
            printWriter.println(responseMessage);
        } catch (IOException e) {
            serverErrorServlet.sendInternalServerError(request, response, "IO");
            throw new RuntimeException(e);
        }
    }

}
