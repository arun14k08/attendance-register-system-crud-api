package com.arun.controller.security;

import com.arun.controller.util.MessengerServlet;
import com.arun.errorHandler.ClientErrorServlet;
import com.arun.errorHandler.ServerErrorServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class Logout extends HttpServlet {
    MessengerServlet messengerServlet = new MessengerServlet();
    ServerErrorServlet serverErrorServlet = new ServerErrorServlet();
    ClientErrorServlet clientErrorServlet = new ClientErrorServlet();
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if(session == null){
            clientErrorServlet.sendErrorResponse(request, response, "You are not logged in");
            return;
        }
        session.invalidate();
        System.out.println("User Logged out successfully");
        try {
            messengerServlet.sendMessage(request, response, "Logged out successfully");
        } catch (IOException e) {
            serverErrorServlet.sendInternalServerError(request, response, "IO");
            throw new RuntimeException(e);
        }
    }

}
