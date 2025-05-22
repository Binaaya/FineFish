<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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
<!-- Include Admin Navigation -->
<jsp:include page="/pages/admin/adminnav.jsp">
    <jsp:param name="currentPage" value="dashboard" />
</jsp:include>

        <!-- Main Content -->
        <main class="main-content">
            <div class="dashboard-header">
                <h1>Dashboard Overview</h1>
                <p>Welcome back, ${sessionScope.user.name}!</p>
            </div>
            
            <!-- Stats Cards -->
            <div class="stats-cards">
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-info">
                            <p>Total Products</p>
                            <h3>${totalProducts}</h3>
                        </div>
                        <div class="stat-icon">
                            <i class="fas fa-box-open"></i>
                        </div>
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-info">
                            <p>Total Orders</p>
                            <h3>${totalOrders}</h3>
                        </div>
                        <div class="stat-icon">
                            <i class="fas fa-shopping-cart"></i>
                        </div>
                    </div>
                </div>
                
                <a href="${pageContext.request.contextPath}/SalesReportServlet" class="stat-card" style="text-decoration: none; color: inherit;">
                    <div class="stat-header">
                        <div class="stat-info">
                            <p>Total Revenue</p>
                            <h3>$<fmt:formatNumber value="${totalRevenue}" pattern="#,##0.00" /></h3>
                        </div>
                        <div class="stat-icon">
                            <i class="fas fa-dollar-sign"></i>
                        </div>
                    </div>
                </a>
                
                <div class="stat-card">
                    <div class="stat-header">
                        <div class="stat-info">
                            <p>Active Users</p>
                            <h3>${activeUsers}</h3>
                        </div>
                        <div class="stat-icon">
                            <i class="fas fa-users"></i>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Dashboard Content -->
            <div class="dashboard-content">
                <!-- Recent Orders -->
                <div class="content-card">
                    <div class="card-header">
                        <h2>Recent Orders</h2>
                    </div>
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>Order ID</th>
                                    <th>Customer</th>
                                    <th>Amount</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="order" items="${recentOrders}">
                                    <tr>
                                        <td>#${order.orderId}</td>
                                        <td>${order.username}</td>
                                        <td>$<fmt:formatNumber value="${order.amount}" pattern="#,##0.00" /></td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty recentOrders}">
                                    <tr>
                                        <td colspan="3" style="text-align: center;">No orders found</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
                
                <!-- Popular Products -->
                <div class="content-card">
                    <div class="card-header">
                        <h2>Popular Products</h2>
                    </div>
                    <div class="product-list">
                        <c:forEach var="product" items="${popularProducts}">
                            <div class="product-item">
                                <div class="product-icon">
                                    <c:choose>
                                        <c:when test="${not empty product.photo}">
                                            <img src="${pageContext.request.contextPath}/images/products/${product.photo}" 
                                                 alt="${product.name}" style="width: 100%; height: 100%; object-fit: cover;">
                                        </c:when>
                                        <c:otherwise>
                                            <i class="fas fa-fish"></i>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="product-info">
                                    <h4>${product.name}</h4>
                                    <p>${product.categoryName}</p>
                                </div>
                                <div class="product-price">
                                    <p>$<fmt:formatNumber value="${product.price}" pattern="#,##0.00" /></p>
                                    <p>${productSalesMap[product.id]} sold</p>
                                </div>
                            </div>
                        </c:forEach>
                        
                        <c:if test="${empty popularProducts}">
                            <div class="empty-state" style="padding: 2rem; text-align: center;">
                                <i class="fas fa-box-open" style="font-size: 2rem; color: #ccc; margin-bottom: 1rem;"></i>
                                <p>No products available</p>
                            </div>
                        </c:if>
                    </div>
                </div>

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>