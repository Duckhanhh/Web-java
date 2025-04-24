package com.example;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/header")
public class Test extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write("<h1>Xin chào từ Servlet 123!</h1>");

//        PrintWriter writer = response.getWriter();
//
//        RequestDispatcher rd = request.getRequestDispatcher("/footer");
//        if (rd != null) {
//            rd.include(request, response);
//        }
    }
}