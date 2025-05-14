package com.abcbook.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/header")
public class HeaderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();

        // Lấy thông tin người dùng từ session (giả lập nếu null)
        String fullName = (String) request.getSession().getAttribute("fullName");
        String role = (String) request.getSession().getAttribute("role");
        if (fullName == null) fullName = "K. Anderson";
        if (role == null) role = "admin";

        // Xuất HTML cho Header
        writer.append("<!DOCTYPE html>");
        writer.append("<header id=\"header\" class=\"header fixed-top d-flex align-items-center\">");
        writer.append("<div class=\"d-flex align-items-center justify-content-between\">");
        writer.append("<a href=\"/bookstore/dashboard\" class=\"logo d-flex align-items-center\">");
        writer.append("<span class=\"d-none d-lg-block\">Bookstore</span>");
        writer.append("</a>");
        writer.append("<i class=\"bi bi-list toggle-sidebar-btn\"></i>");
        writer.append("</div>");

        writer.append("<nav class=\"header-nav ms-auto\">");
        writer.append("<ul class=\"d-flex align-items-center\">");
        writer.append("<li class=\"nav-item dropdown\">");
        writer.append("<a class=\"nav-link nav-icon\" href=\"#\" data-bs-toggle=\"dropdown\">");
        writer.append("<i class=\"bi bi-bell\"></i>");
        writer.append("<span class=\"badge bg-primary badge-number\">4</span>");
        writer.append("</a>");
        writer.append("<ul class=\"dropdown-menu dropdown-menu-end dropdown-menu-arrow notifications\">");
        writer.append("<li class=\"dropdown-header\">");
        writer.append("You have 4 new notifications");
        writer.append("<a href=\"#\"><span class=\"badge rounded-pill bg-primary p-2 ms-2\">View all</span></a>");
        writer.append("</li>");
        writer.append("<li><hr class=\"dropdown-divider\"></li>");
        writer.append("<li class=\"notification-item\">");
        writer.append("<i class=\"bi bi-exclamation-circle text-warning\"></i>");
        writer.append("<div>");
        writer.append("<h4>New Book Added</h4>");
        writer.append("<p>A new book has been added to inventory.</p>");
        writer.append("<p>30 min. ago</p>");
        writer.append("</div>");
        writer.append("</li>");
        writer.append("<li><hr class=\"dropdown-divider\"></li>");
        writer.append("<li class=\"notification-item\">");
        writer.append("<i class=\"bi bi-check-circle text-success\"></i>");
        writer.append("<div>");
        writer.append("<h4>Order Confirmed</h4>");
        writer.append("<p>Order #2457 has been confirmed.</p>");
        writer.append("<p>1 hr. ago</p>");
        writer.append("</div>");
        writer.append("</li>");
        writer.append("</ul>");
        writer.append("</li>");

        writer.append("<li class=\"nav-item dropdown pe-3\">");
        writer.append("<a class=\"nav-link nav-profile d-flex align-items-center pe-0\" href=\"#\" data-bs-toggle=\"dropdown\">");
        writer.append("<span class=\"d-none d-md-block dropdown-toggle ps-2\">").append(fullName).append("</span>");
        writer.append("</a>");
        writer.append("<ul class=\"dropdown-menu dropdown-menu-end dropdown-menu-arrow profile\">");
        writer.append("<li class=\"dropdown-header\">");
        writer.append("<h6>").append(fullName).append("</h6>");
        writer.append("<span data-role=\"").append(role).append("\">").append(role).append("</span>");
        writer.append("</li>");
        writer.append("<li><a class=\"dropdown-item d-flex align-items-center\" href=\"#\"><i class=\"bi bi-person\"></i><span>My Profile</span></a></li>");
        writer.append("<li><a class=\"dropdown-item d-flex align-items-center\" href=\"#\"><i class=\"bi bi-gear\"></i><span>Account Settings</span></a></li>");
        writer.append("<li><a class=\"dropdown-item d-flex align-items-center\" href=\"#\"><i class=\"bi bi-question-circle\"></i><span>Need Help?</span></a></li>");
        writer.append("<li><a class=\"dropdown-item d-flex align-items-center\" href=\"/bookstore/logout\"><i class=\"bi bi-box-arrow-right\"></i><span>Sign Out</span></a></li>");
        writer.append("</ul>");
        writer.append("</li>");
        writer.append("</ul>");
        writer.append("</nav>");
        writer.append("</header>");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}