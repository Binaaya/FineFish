<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty sessionScope.user || sessionScope.user.role != 'admin'}">
    <c:redirect url="/pages/user/Login.jsp" />
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - FineFish</title>
    <!-- Stylesheets -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <div class="admin-container">
        <!-- Sidebar Navigation -->
        <aside class="sidebar">
            <div class="sidebar-header">
                <h1>FineFish Admin</h1>
                <button id="sidebar-toggle" class="sidebar-toggle">
                    <i class="fas fa-bars"></i>
                </button>
            </div>
            
            <div class="admin-profile">
                <div class="admin-avatar">
                    <i class="fas fa-user-circle"></i>
                </div>
                <div class="admin-info">
                    <h3>${sessionScope.user.name}</h3>
                    <span class="admin-role">Administrator</span>
                </div>
            </div>
            
            <nav class="sidebar-nav">
                <ul>
                    <li class="${param.currentPage == 'dashboard' ? 'active' : ''}">
                        <a href="${pageContext.request.contextPath}/AdminDashboardServlet">
                            <i class="fas fa-tachometer-alt"></i>
                            <span>Dashboard</span>
                        </a>
                    </li>
                     <li>
                        <a href="${pageContext.request.contextPath}/AddCategoryServlet">
                            <i class="fas fa-folder-plus"></i>
                            <span>Add Category</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/AddProductServlet">
                            <i class="fas fa-plus-circle"></i>
                            <span>Add Product</span>
                        </a>
                    </li>
                   
                    <li>
                        <a href="${pageContext.request.contextPath}/UpdateProductServlet">
                            <i class="fas fa-edit"></i>
                            <span>Manage Products</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/DeleteProductServlet">
                            <i class="fas fa-trash-alt"></i>
                            <span>Remove Product</span>
                        </a>
                    </li>
                    <li class="${param.currentPage == 'salesreport' ? 'active' : ''}">
                        <a href="${pageContext.request.contextPath}/SalesReportServlet">
                            <i class="fas fa-chart-line"></i>
                            <span>Sales Report</span>
                        </a>
                    </li>
                </ul>
            </nav>
            
            <div class="sidebar-footer">
                <a href="${pageContext.request.contextPath}/LogOutController">
                    <i class="fas fa-sign-out-alt"></i>
                    <span>Logout</span>
                </a>
            </div>
        </aside>
        
    </div>
  <!-- Mobile Navigation Toggle -->
    <button class="mobile-nav-toggle" id="mobile-nav-toggle" style="display: none; color: blue;">
        <i class="fas fa-bars"></i>
    </button>
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
    <script>
        // Sidebar toggle functionality
        document.getElementById('sidebar-toggle').addEventListener('click', function() {
            document.querySelector('.admin-container').classList.toggle('sidebar-collapsed');
        });
    </script>
    <script>
    document.addEventListener('DOMContentLoaded', function () {
        const mobileToggle = document.getElementById('mobile-nav-toggle');
        const adminContainer = document.querySelector('.admin-container');
        const sidebar = document.querySelector('.sidebar');

        // Toggle mobile menu visibility
        mobileToggle.addEventListener('click', function () {
            adminContainer.classList.toggle('mobile-menu-visible');
        });

        // Optional: Close sidebar when clicking outside of it
        document.addEventListener('click', function (e) {
            if (
                adminContainer.classList.contains('mobile-menu-visible') &&
                !sidebar.contains(e.target) &&
                !mobileToggle.contains(e.target)
            ) {
                adminContainer.classList.remove('mobile-menu-visible');
            }
        });

        // Optional: Show toggle button only on mobile
        function handleResize() {
            if (window.innerWidth <= 768) {
                mobileToggle.style.display = 'flex';
            } else {
                mobileToggle.style.display = 'none';
                adminContainer.classList.remove('mobile-menu-visible');
            }
        }

        window.addEventListener('resize', handleResize);
        handleResize(); // Initial check
    });
</script>
    
</body>
</html>