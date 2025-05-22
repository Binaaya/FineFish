<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Check if user is logged in -->
<c:if test="${empty sessionScope.user}">
    <c:redirect url="/pages/user/Login.jsp" />
</c:if>
<!-- base css -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/userdashboard.css">
<!-- Include Header -->
<jsp:include page="/pages/includes/Header.jsp">
    <jsp:param name="title" value="Delete Account" />
    <jsp:param name="css" value="dashboard" />
</jsp:include>

<!-- Page Header -->
<header class="page-header">
  <div class="container">
    <h1 class="page-title">Delete Account</h1>
    <div class="breadcrumb">
      <a href="${pageContext.request.contextPath}/index.jsp">Home</a>
      <span>/</span>
      <a href="${pageContext.request.contextPath}/pages/user/Dashboard.jsp">My Account</a>
      <span>/</span>
      <span>Delete Account</span>
    </div>
  </div>
</header>

<!-- Main Content -->
<main class="main-container">
  <jsp:include page="/pages/includes/messages.jsp" />
  
  <div class="delete-account-container">
    <div class="warning-box">
      <h3><i class="fas fa-exclamation-triangle"></i> Warning: Account Deletion</h3>
      <p>You are about to permanently delete your account. This action cannot be undone.</p>
      <p>All your personal information, order history, and saved preferences will be removed from our system.</p>
    </div>
    
    <form action="${pageContext.request.contextPath}/DeleteUserServlet" method="post">
      <div class="form-group">
        <label for="confirmation">Type "DELETE" to confirm:</label>
        <input type="text" id="confirmation" name="confirmation" class="form-input" required>
        <p class="input-hint">This will permanently delete your account from our system. Please type "DELETE" in uppercase to confirm.</p>
      </div>
      
      <div class="form-actions">
        <a href="${pageContext.request.contextPath}/pages/user/Dashboard.jsp" class="btn-secondary">
          Cancel
        </a>
        <button type="submit" class="btn-danger">
          Delete My Account
        </button>
      </div>
    </form>
  </div>
</main>

<!-- Include Footer -->
<jsp:include page="/pages/includes/Footer.jsp">
    <jsp:param name="js" value="dashboard" />
</jsp:include>