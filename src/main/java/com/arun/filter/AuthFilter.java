package com.arun.filter;

import com.arun.errorHandler.ClientErrorServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


public class AuthFilter implements Filter {
    ClientErrorServlet clientErrorServlet = new ClientErrorServlet();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        if(session == null){
            clientErrorServlet.sendErrorResponse(request, response, "Kindly login to access the resource", 401);
            System.out.println("User is not authenticated");
        } else {
            System.out.println("User is authenticated");
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
