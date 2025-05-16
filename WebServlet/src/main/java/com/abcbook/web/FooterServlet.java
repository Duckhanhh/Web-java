package com.abcbook.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/footer")
public class FooterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("textátext/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();

        // Xuất HTML cho Footer
        writer.append("<!DOCTYPE html>");
        writer.append("<footer id=\"footer\" class=\"footer\">");
        writer.append("<div class=\"copyright\">");
        writer.append("© Copyright <strong><span>Bookstore</span></strong>. All Rights Reserved");
        writer.append("</div>");
        writer.append("<div class=\"credits\">");
        writer.append("Designed by <a href=\"https://bootstrapmade.com/\">BootstrapMade</a>");
        writer.append("</div>");
        writer.append("</footer>");
    }
}