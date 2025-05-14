package com.abcbook.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();

        // In context path ra log
        String contextPath = request.getContextPath();
        System.out.println("Context Path: " + contextPath);

        writer.append("<!DOCTYPE html>");
        writer.append("<html lang=\"en\">");
        writer.append("<head>");
        writer.append("<meta charset=\"utf-8\">");
        writer.append("<title>Login</title>");
        writer.append("<link href=\"/abcbook/assets/vendor/bootstrap/css/bootstrap.min.css\" rel=\"stylesheet\">");
        writer.append("</head>");
        writer.append("<body>");
        writer.append("<div class=\"container\">");
        writer.append("<div class=\"row justify-content-center mt-5\">");
        writer.append("<div class=\"col-md-4\">");
        writer.append("<div class=\"card\">");
        writer.append("<div class=\"card-body\">");
        writer.append("<h5 class=\"card-title\">Đăng nhập</h5>");
        writer.append("<form method=\"post\" action=\"/abcbook/login\">");
        writer.append("<div class=\"mb-3\">");
        writer.append("<label for=\"username\" class=\"form-label\">Email</label>");
        writer.append("<input type=\"text\" class=\"form-control\" id=\"username\" name=\"username\" required>");
        writer.append("</div>");
        writer.append("<div class=\"mb-3\">");
        writer.append("<label for=\"password\" class=\"form-label\">Mật khẩu</label>");
        writer.append("<input type=\"password\" class=\"form-control\" id=\"password\" name=\"password\" required>");
        writer.append("</div>");
        writer.append("<button type=\"submit\" class=\"btn btn-primary\">Đăng nhập</button>");
        writer.append("</form>");
        writer.append("</div>");
        writer.append("</div>");
        writer.append("</div>");
        writer.append("</div>");
        writer.append("</div>");
        writer.append("</body>");
        writer.append("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // Giả lập kiểm tra đăng nhập
        if ("admin@bookstore.com".equals(username) && "password".equals(password)) {
            request.getSession().setAttribute("fullName", "Kevin Anderson");
            request.getSession().setAttribute("role", "admin");
            response.sendRedirect("/abcbook/dashboard");
        } else {
            response.sendRedirect("/abcbook/login?error");
        }
    }
}