package com.abcbook.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/sidebar")
public class SidebarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();

        // Lấy vai trò người dùng từ session
        String role = (String) request.getSession().getAttribute("role");
        if (role == null) role = "admin";

        // Xuất HTML cho Sidebar
        writer.append("<!DOCTYPE html>");
        writer.append("<aside id=\"sidebar\" class=\"sidebar\">");
        writer.append("<ul class=\"sidebar-nav\" id=\"sidebar-nav\">");
        writer.append("<li class=\"nav-item\">");
        writer.append("<a class=\"nav-link\" href=\"#dashboard\" data-section=\"dashboard\">");
        writer.append("<i class=\"bi bi-grid\"></i>");
        writer.append("<span>Dashboard</span>");
        writer.append("</a>");
        writer.append("</li>");
        writer.append("<li class=\"nav-item\" data-role=\"admin,staff\">");
        writer.append("<a class=\"nav-link collapsed\" href=\"#inventory\" data-section=\"inventory\">");
        writer.append("<i class=\"bi bi-box\"></i>");
        writer.append("<span>Quản lý kho</span>");
        writer.append("</a>");
        writer.append("</li>");
        writer.append("<li class=\"nav-item\" data-role=\"admin,staff\">");
        writer.append("<a class=\"nav-link collapsed\" href=\"#books\" data-section=\"books\">");
        writer.append("<i class=\"bi bi-book\"></i>");
        writer.append("<span>Quản lý sách</span>");
        writer.append("</a>");
        writer.append("</li>");
        writer.append("<li class=\"nav-item\" data-role=\"admin,staff\">");
        writer.append("<a class=\"nav-link collapsed\" href=\"#orders\" data-section=\"orders\">");
        writer.append("<i class=\"bi bi-cart\"></i>");
        writer.append("<span>Quản lý đơn hàng</span>");
        writer.append("</a>");
        writer.append("</li>");
        writer.append("<li class=\"nav-item\" data-role=\"admin,staff\">");
        writer.append("<a class=\"nav-link collapsed\" href=\"#purchase-orders\" data-section=\"purchase-orders\">");
        writer.append("<i class=\"bi bi-truck\"></i>");
        writer.append("<span>Quản lý nhập hàng</span>");
        writer.append("</a>");
        writer.append("</li>");
        writer.append("<li class=\"nav-item\" data-role=\"admin\">");
        writer.append("<a class=\"nav-link collapsed\" href=\"#staff\" data-section=\"staff\">");
        writer.append("<i class=\"bi bi-person\"></i>");
        writer.append("<span>Quản lý nhân viên</span>");
        writer.append("</a>");
        writer.append("</li>");
        writer.append("</ul>");
        writer.append("</aside>");

        // JavaScript để kiểm tra vai trò và điều hướng
        writer.append("<script>");
        writer.append("const userRole = '").append(role).append("';");
        writer.append("document.querySelectorAll('[data-role]').forEach(item => {");
        writer.append("  const roles = item.getAttribute('data-role').split(',');");
        writer.append("  if (!roles.includes(userRole)) {");
        writer.append("    item.style.display = 'none';");
        writer.append("  }");
        writer.append("});");
        writer.append("document.querySelectorAll('.nav-link').forEach(link => {");
        writer.append("  link.addEventListener('click', (e) => {");
        writer.append("    e.preventDefault();");
        writer.append("    const target = link.getAttribute('data-section');");
        writer.append("    document.querySelectorAll('.section').forEach(section => {");
        writer.append("      section.classList.add('d-none');");
        writer.append("    });");
        writer.append("    const targetSection = document.getElementById(target);");
        writer.append("    if (targetSection) {");
        writer.append("      targetSection.classList.remove('d-none');");
        writer.append("    }");
        writer.append("  });");
        writer.append("});");
        writer.append("</script>");
    }
}