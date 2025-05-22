<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- Base CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/orderdetails.css">
    
<!-- Check if user is logged in -->
<c:if test="${empty sessionScope.user}">
    <c:redirect url="/pages/user/Login.jsp" />
</c:if>

<!-- Include Header -->
<jsp:include page="/pages/includes/Header.jsp">
    <jsp:param name="title" value="Order Details" />
    <jsp:param name="css" value="userdashboard" />
</jsp:include>

<!-- Page Header -->
<header class="page-header">
  <div class="container">
    <h1 class="page-title">Order Details</h1>
    <div class="breadcrumb">
      <a href="${pageContext.request.contextPath}/index.jsp">Home</a>
      <span>/</span>
      <a href="${pageContext.request.contextPath}/pages/user/Dashboard.jsp">My Account</a>
      <span>/</span>
      <span>Order Details</span>
    </div>
  </div>
</header>

<!-- Main Content -->
<main class="container">
  <jsp:include page="/pages/includes/messages.jsp" />
  
  <div class="order-details-container">
    <!-- Order Summary -->
    <div class="order-summary">
      <div class="order-header">
        <div>
          <h2>Order #${order.orderId}</h2>
          <p class="order-date">Placed on: <fmt:formatDate value="${order.orderDate}" pattern="MMMM d, yyyy h:mm a" /></p>
        </div>
      </div>
      
      <div class="order-summary-grid">
        <div class="summary-box">
          <h3>Order Summary</h3>
          <div class="summary-details">
            <div class="summary-row">
              <span>Subtotal</span>
              <span>$<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00" /></span>
            </div>
            <div class="summary-row">
              <span>Shipping</span>
              <span>$<fmt:formatNumber value="10.00" pattern="#,##0.00" /></span>
            </div>
            <div class="summary-row total">
              <span>Total</span>
              <span>$<fmt:formatNumber value="${order.totalAmount + 10.00}" pattern="#,##0.00" /></span>
            </div>
          </div>
        </div>
        
        <div class="summary-box">
          <h3>Shipping Address</h3>
          <p>${sessionScope.user.fname} ${sessionScope.user.surname}</p>
          <p>${sessionScope.user.phoneNumber}</p>
          <p>${sessionScope.user.address}</p>
        </div>
      </div>
    </div>
    
    <!-- Order Items -->
    <div class="order-items">
      <h2>Ordered Items</h2>
      
      <div class="items-list">
        <c:forEach var="item" items="${order.orderItems}">
          <div class="order-item">
            <div class="item-image">
              <c:if test="${not empty item.imageUrl}">
                <img src="${pageContext.request.contextPath}/${item.imageUrl}" alt="${item.productName}" />
              </c:if>
              <c:if test="${empty item.imageUrl}">
                <div class="no-image">No Image</div>
              </c:if>
            </div>
            
            <div class="item-details">
              <h3 class="item-name">${item.productName}</h3>
              
              <div class="item-info">
                <span>Category: ${item.category}</span>
                <span>Price: $<fmt:formatNumber value="${item.price}" pattern="#,##0.00" /></span>
                <span>Quantity: ${item.quantity}</span>
              </div>
              
              <div class="item-subtotal">
                <span>Subtotal:</span>
                <span>$<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00" /></span>
              </div>
            </div>
          </div>
        </c:forEach>
      </div>
    </div>
    
    <!-- Order Actions -->
    <div class="order-actions">
      <a href="${pageContext.request.contextPath}/pages/user/Dashboard.jsp" class="btn-secondary">
        <i class="fas fa-arrow-left"></i> Back to Dashboard
      </a>
      
      <a href="${pageContext.request.contextPath}/GetCategoriesServlet" class="btn-primary">
        <i class="fas fa-shopping-bag"></i> Continue Shopping
      </a>
    </div>
  </div>
</main>

<!-- Include Footer -->
<jsp:include page="/pages/includes/Footer.jsp" />