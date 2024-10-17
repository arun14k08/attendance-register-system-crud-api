package com.arun.controller.security;

import com.arun.controller.util.MessengerServlet;
import com.arun.errorHandler.ClientErrorServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Date;

public class Login extends HttpServlet {
    MessengerServlet messengerServlet = new MessengerServlet();
    ClientErrorServlet clientErrorServlet = new ClientErrorServlet();
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if(session != null){
            messengerServlet.sendMessage(request, response, "You are already logged in");
        } else {
            if(request.getParameter("name") == null){
                clientErrorServlet.sendErrorResponse(request, response, "Name is not provided");
                return;
            }
            if(request.getParameter("password") == null){
                clientErrorServlet.sendErrorResponse(request, response, "Password is not provided");
                return;
            }
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            if(name.equals("name") && password.equals("password")){
                HttpSession session1 = request.getSession(true);
                session1.setAttribute("session", new Date());
                messengerServlet.sendMessage(request, response, "Logged in successfully");
                System.out.println("User logged in successfully");
                return;
            }
            clientErrorServlet.sendErrorResponse(request, response, "username or password is incorrect", 401);


        }
    }
}
