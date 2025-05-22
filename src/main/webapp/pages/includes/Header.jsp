<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title} - FineFish - Premium Seafood</title>
    <!-- Base CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/headfoot.css">
    
    
    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<jsp:include page="/pages/includes/messages.jsp" />
    <!-- Navbar -->
    <nav class="navbar">
      <div class="container">
        <!-- Logo -->
        <div class="logo">
          <a href="${pageContext.request.contextPath}/index.jsp" class="logo-link">
            <div class="logo-icon">
              <i class="fas fa-fish"></i>
            </div>
            <span class="logo-text">FineFish</span>
          </a>
        </div>

        <!-- Desktop Navigation -->
        <div class="nav-menu">
          <a href="${pageContext.request.contextPath}/index.jsp" class="nav-link ${param.active == 'home' ? 'active' : ''}">Home</a>
          <a href="${pageContext.request.contextPath}/GetCategoriesServlet" class="nav-link ${param.active == 'products' ? 'active' : ''}">Products</a>
          <a href="${pageContext.request.contextPath}/pages/user/About.jsp" class="nav-link ${param.active == 'about' ? 'active' : ''}">About Us</a>

          <!-- Search Bar -->
          <div class="search-bar">
            <form action="${pageContext.request.contextPath}/SearchServlet" method="get" class="search-form">
              <i class="fas fa-search"></i>
              <input type="text" name="searchTerm" placeholder="Search for seafood..." class="search-input">
              <button type="submit" class="search-button">Search</button>
            </form>
          </div>

          <div class="nav-actions">
            <c:choose>
                <c:when test="${empty sessionScope.user}">
                    <!-- User not logged in -->
                    <a href="${pageContext.request.contextPath}/LogInController" class="btn-login">
                        <i class="fas fa-user"></i>
                        Login/Register
                    </a>
                    
                    <!-- Cart icon without count when not logged in -->
                    <div class="cart-container">
                      <a href="${pageContext.request.contextPath}/LogInController" class="cart-button">
                        <i class="fas fa-shopping-cart"></i>
                      </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <!-- User logged in -->
                    <div class="user-dropdown">
                        <a href="#" class="btn-login dropdown-toggle">
                            <i class="fas fa-user"></i>
                            ${sessionScope.user.name}
                        </a>
                        <div class="dropdown-menu">
                            <c:if test="${sessionScope.user.role == 'admin'}">
                                <a href="${pageContext.request.contextPath}/AdminDashboardServlet">Admin Dashboard</a>
                            </c:if>
                            <a href="${pageContext.request.contextPath}/UpdateProfileServlet">My Account</a>
                            <a href="${pageContext.request.contextPath}/LogOutController">Logout</a>
                        </div>
                    </div>
                    
                     <div class="cart-container">
                      <a href="${pageContext.request.contextPath}/CartServlet?action=view" class="cart-button">
                        <i class="fas fa-shopping-cart"></i>
                        <span class="cart-badge">${sessionScope.cartCount != null ? sessionScope.cartCount : 0}</span>
                      </a>
                    </div>
                </c:otherwise>
            </c:choose>
          </div>
        </div>

        <!-- Mobile menu button -->
        <button class="mobile-menu-btn">
          <i class="fas fa-bars"></i>
        </button>
      </div>
    </nav>