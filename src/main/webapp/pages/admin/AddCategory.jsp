<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %>

<c:if test="${empty sessionScope.user || sessionScope.user.role != 'admin'}">
  <c:redirect url="/pages/user/Login.jsp" />
</c:if>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Add Category - FineFish Admin</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/style.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/admin.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />

    <style>
      .admin-form-wrapper {
        max-width: 600px;
        margin: 2rem auto;
      }

      .admin-form-card {
        background-color: #ffffff;
        border-radius: 12px;
        padding: 2rem;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        border: 1px solid #e1e1e1;
      }

      .form-title {
        font-size: 1.5rem;
        font-weight: 600;
        margin-bottom: 1.5rem;
        color: #333333;
        text-align: center;
      }

      .form-group {
        margin-bottom: 1.5rem;
      }

      .form-group label {
        display: block;
        margin-bottom: 0.5rem;
        font-weight: 500;
        color: #333333;
      }

      .form-input {
        width: 100%;
        padding: 0.75rem;
        border: 1px solid #e1e1e1;
        border-radius: 8px;
        font-size: 1rem;
        font-family: inherit;
      }

      .form-input:focus {
        outline: none;
        border-color: #3a86ff;
        box-shadow: 0 0 0 3px rgba(58, 134, 255, 0.15);
      }

      .form-actions {
        display: flex;
        justify-content: flex-end;
        gap: 1rem;
        margin-top: 2rem;
      }

      .btn-primary,
      .btn-secondary {
        padding: 0.75rem 1.5rem;
        border-radius: 50px;
        font-weight: 600;
        text-decoration: none;
        cursor: pointer;
        transition: all 0.3s ease;
      }

      .btn-primary {
        background-color: #3a86ff;
        color: #ffffff;
        border: none;
      }

      .btn-primary:hover {
        background-color: #2667cc;
      }

      .btn-secondary {
        background-color: #6c757d;
        color: #ffffff;
      }

      .btn-secondary:hover {
        background-color: #5a6268;
      }
    </style>
  </head>
  <body>
    <!-- Include Admin Navigation -->
    <jsp:include page="/pages/admin/adminnav.jsp">
      <jsp:param name="currentPage" value="categories" />
    </jsp:include>

    <main class="main-content">
      <jsp:include page="/pages/includes/messages.jsp" />

      <!-- Welcome Section -->
      <div class="admin-welcome">
        <h2>Add New Category</h2>
        <p>Create a new category for products in your inventory.</p>
      </div>

      <!-- Add Category Form -->
      <div class="admin-form-wrapper">
        <div class="admin-form-card">
          <h3 class="form-title">Category Details</h3>

          <form
            action="${pageContext.request.contextPath}/AddCategoryServlet"
            method="post"
            class="admin-form"
          >
            <div class="form-group">
              <label for="categoryId"
                >Category ID <span class="required">*</span></label
              >
              <input
                type="number"
                id="categoryId"
                name="categoryId"
                class="form-input"
                required
                placeholder="Enter category ID"
              />
            </div>

            <div class="form-group">
              <label for="categoryName"
                >Category Name <span class="required">*</span></label
              >
              <input
                type="text"
                id="categoryName"
                name="categoryName"
                class="form-input"
                required
                placeholder="Enter category name"
              />
            </div>

            <div class="form-actions">
              <a
                href="${pageContext.request.contextPath}/GetCategoriesServlet?destination=admin"
                class="btn-secondary"
                >Cancel</a
              >
              <button type="submit" class="btn-primary">Add Category</button>
            </div>
          </form>
        </div>
      </div>

      <!-- Back to Dashboard Link -->
      <div class="admin-back-link">
        <a href="${pageContext.request.contextPath}/AdminDashboardServlet">
          <i class="fas fa-arrow-left"></i> Back to Dashboard
        </a>
      </div>
    </main>

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
  </body>
</html>
