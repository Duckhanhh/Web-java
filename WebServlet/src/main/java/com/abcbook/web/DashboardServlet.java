package com.abcbook.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();

        // Giả lập thông tin người dùng
        request.getSession().setAttribute("fullName", "Kevin Anderson");
        request.getSession().setAttribute("role", "admin");

        // Bắt đầu HTML
        writer.append("<!DOCTYPE html>");
        writer.append("<html lang=\"en\">");
        writer.append("<head>");
        writer.append("<meta charset=\"utf-8\">");
        writer.append("<meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\">");
        writer.append("<title>Bookstore Dashboard</title>");
        writer.append("<link href=\"https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i|Nunito:300,300i,400,400i,600,600i,700,700i|Poppins:300,300i,400,400i,500,500i,600,600i,700,700i\" rel=\"stylesheet\">");
        writer.append("<link href=\"/abcbook/assets/vendor/bootstrap/css/bootstrap.min.css\" rel=\"stylesheet\">");
        writer.append("<link href=\"/abcbook/assets/vendor/bootstrap-icons/bootstrap-icons.css\" rel=\"stylesheet\">");
        writer.append("<link href=\"/abcbook/assets/vendor/boxicons/css/boxicons.min.css\" rel=\"stylesheet\">");
        writer.append("<link href=\"/abcbook/assets/vendor/simple-datatables/style.css\" rel=\"stylesheet\">");
        writer.append("<link href=\"/abcbook/assets/css/style.css\" rel=\"stylesheet\">");
        writer.append("</head>");
        writer.append("<body>");

        // Include Header
        request.getRequestDispatcher("/header").include(request, response);

        // Include Sidebar
        request.getRequestDispatcher("/sidebar").include(request, response);

        // Main Content (Dashboard)
        writer.append("<main id=\"main\" class=\"main\">");
        writer.append("<section id=\"dashboard\" class=\"section\">");
        writer.append("<div class=\"pagetitle\">");
        writer.append("<h1>Dashboard</h1>");
        writer.append("<nav>");
        writer.append("<ol class=\"breadcrumb\">");
        writer.append("<li class=\"breadcrumb-item\"><a href=\"#dashboard\">Home</a></li>");
        writer.append("<li class=\"breadcrumb-item active\">Dashboard</li>");
        writer.append("</ol>");
        writer.append("</nav>");
        writer.append("</div>");
        writer.append("<div class=\"row\">");
        writer.append("<div class=\"col-lg-4\">");
        writer.append("<div class=\"card\">");
        writer.append("<div class=\"card-body\">");
        writer.append("<h5 class=\"card-title\">Tổng sách trong kho <span>| Hôm nay</span></h5>");
        writer.append("<div class=\"d-flex align-items-center\">");
        writer.append("<div class=\"ps-3\">");
        writer.append("<h6>1,234</h6>");
        writer.append("<span class=\"text-success small pt-1 fw-bold\">5%</span>");
        writer.append("<span class=\"text-muted small pt-2 ps-1\">tăng</span>");
        writer.append("</div>");
        writer.append("</div>");
        writer.append("</div>");
        writer.append("</div>");
        writer.append("</div>");
        writer.append("<div class=\"col-lg-4\">");
        writer.append("<div class=\"card\">");
        writer.append("<div class=\"card-body\">");
        writer.append("<h5 class=\"card-title\">Doanh thu <span>| Tháng này</span></h5>");
        writer.append("<div class=\"d-flex align-items-center\">");
        writer.append("<div class=\"ps-3\">");
        writer.append("<h6>12,345,000 VNĐ</h6>");
        writer.append("<span class=\"text-success small pt-1 fw-bold\">8%</span>");
        writer.append("<span class=\"text-muted small pt-2 ps-1\">tăng</span>");
        writer.append("</div>");
        writer.append("</div>");
        writer.append("</div>");
        writer.append("</div>");
        writer.append("</div>");
        writer.append("<div class=\"col-lg-4\">");
        writer.append("<div class=\"card\">");
        writer.append("<div class=\"card-body\">");
        writer.append("<h5 class=\"card-title\">Đơn hàng <span>| Hôm nay</span></h5>");
        writer.append("<div class=\"d-flex align-items-center\">");
        writer.append("<div class=\"ps-3\">");
        writer.append("<h6>56</h6>");
        writer.append("<span class=\"text-danger small pt-1 fw-bold\">2%</span>");
        writer.append("<span class=\"text-muted small pt-2 ps-1\">giảm</span>");
        writer.append("</div>");
        writer.append("</div>");
        writer.append("</div>");
        writer.append("</div>");
        writer.append("</div>");
        writer.append("</div>");
        writer.append("</section>");
        writer.append("</main>");

        // Include Footer
        request.getRequestDispatcher("/footer").include(request, response);

        // Kết thúc HTML
        writer.append("<script src=\"/abcbook/assets/vendor/bootstrap/js/bootstrap.bundle.min.js\"></script>");
        writer.append("<script src=\"/abcbook/assets/vendor/simple-datatables/simple-datatables.js\"></script>");
        writer.append("<script src=\"/abcbook/assets/js/main.js\"></script>");
        writer.append("</body>");
        writer.append("</html>");
    }
}