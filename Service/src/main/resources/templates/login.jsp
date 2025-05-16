<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
    Lưu ý: Dòng taglib cho prefix="c" (JSTL Core) được thêm vào
    nếu bạn có ý định sử dụng các thẻ <c:...> của JSTL sau này.
    Trong code hiện tại, nó không cần thiết nhưng là practice tốt nếu dùng JSTL.
    Thuộc tính xmlns:c="http://www.w3.org/2000/svg" trong thẻ <html> gốc có vẻ không đúng,
    nó có thể là tàn dư của Thymeleaf hoặc một namespace XML khác không liên quan đến JSTL.
    Nó đã được loại bỏ vì không cần thiết cho JSP hoặc HTML thông thường.
--%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">

    <title>Login AbcBook Managment</title>
    <meta content="" name="description">
    <meta content="" name="keywords">

    <%-- Sử dụng Expression Language (EL) để thêm context path nếu cần --%>
    <link href="${pageContext.request.contextPath}/assets/img/favicon.png" rel="icon">
    <link href="${pageContext.request.contextPath}/assets/img/apple-touch-icon.png" rel="apple-touch-icon">

    <link href="https://fonts.gstatic.com" rel="preconnect">
    <link
            href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i|Nunito:300,300i,400,400i,600,600i,700,700i|Poppins:300,300i,400,400i,500,500i,600,600i,700,700i"
            rel="stylesheet">

    <%-- Sử dụng Expression Language (EL) để thêm context path nếu cần --%>
    <link href="${pageContext.request.contextPath}/assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/vendor/boxicons/css/boxicons.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/vendor/quill/quill.snow.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/vendor/quill/quill.bubble.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/vendor/remixicon/remixicon.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/vendor/simple-datatables/style.css" rel="stylesheet">

    <%-- Sử dụng Expression Language (EL) để thêm context path nếu cần --%>
    <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
</head>

<body>

<main>
    <div class="container">

        <section class="section register min-vh-100 d-flex flex-column align-items-center justify-content-center py-4">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-lg-4 col-md-6 d-flex flex-column align-items-center justify-content-center">

                        <div class="d-flex justify-content-center py-4">
                            <%-- Sử dụng Expression Language (EL) để thêm context path nếu cần --%>
                            <img class="logo d-flex align-items-center w-auto" src="${pageContext.request.contextPath}/assets/img/main_logo.png" alt="">
                        </div>
                        <div class="card mb-3">

                            <div class="card-body">
                                <%-- Form action cũng nên sử dụng context path --%>
                                <form class="row g-3 pt-4 pb-2 needs-validation" id="loginForm" novalidate
                                      action="${pageContext.request.contextPath}/auth/login" method="post">
                                    <div class="col-12">
                                        <label for="email" class="form-label">Email</label>
                                        <div class="input-group has-validation">
                                            <input type="text" name="email" class="form-control" id="email" required>
                                            <div class="invalid-feedback">Vui lòng nhập email</div>
                                        </div>
                                    </div>
                                    <div class="col-12">
                                        <label for="password" class="form-label">Mật khẩu</label>
                                        <input type="password" name="password" class="form-control" id="password" required>
                                        <div class="invalid-feedback">Vui lòng nhập mật khẩu</div>
                                    </div>
                                    <div class="col-12">
                                        <%-- Phần hiển thị lỗi inline, có thể dùng EL để hiển thị lỗi từ server nếu có --%>
                                        <div id="error" style="color: red; font-size: 14px;">${error}</div>
                                        <%-- Hoặc chỉ là một placeholder cho JS --%>
                                    </div>
                                    <div class="col-12">
                                        <button class="btn w-100" type="submit" style="background-color: crimson; color: white;">Đăng nhập</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</main><a href="#" class="back-to-top d-flex align-items-center justify-content-center"><i
        class="bi bi-arrow-up-short"></i></a>

<div class="modal fade" id="errorModal" tabindex="-1" aria-labelledby="errorModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-danger">
            <div class="modal-header bg-danger text-white">
                <h5 class="modal-title" id="errorModalLabel">
                    <i class="bi bi-exclamation-triangle-fill"></i> Lỗi đăng nhập
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                        aria-label="Đóng"></button>
            </div>
            <div class="modal-body text-danger fw-bold" id="errorMessage">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<%-- Thẻ div ẩn chứa dữ liệu lỗi từ server, sử dụng Expression Language ${error} --%>
<div id="error-data" data-error="${error}" style="display: none;"></div>


<%-- Đặt các script ở cuối body để cải thiện tốc độ tải trang --%>
<%-- Sử dụng Expression Language (EL) để thêm context path nếu cần --%>
<script src="${pageContext.request.contextPath}/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const errorElement = document.getElementById("error-data");
        if (errorElement) {
            const errorMsg = errorElement.getAttribute("data-error");
            // Kiểm tra errorMsg có tồn tại, không rỗng và không phải là chuỗi "${error}" (trường hợp EL không phân giải được)
            if (errorMsg && errorMsg.trim().length > 0 && errorMsg !== '${error}') {
                document.getElementById("errorMessage").innerText = errorMsg;
                const modal = new bootstrap.Modal(document.getElementById("errorModal"));
                modal.show();
            }
        }
    });
</script>

</body>
</html>