<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty sessionScope.user || sessionScope.user.role != 'admin'}">
    <c:redirect url="/pages/user/Login.jsp" />
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Product - FineFish Admin</title>
    <!-- Stylesheets -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <!-- Basic styles -->
    <style>
        
        /* Form */
        .admin-form-wrapper {
            max-width: 800px;
            margin: 0 auto 2rem;
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
            text-align: center;
            color: #333333;
        }
        
        .admin-form {
            display: flex;
            flex-direction: column;
            gap: 1.5rem;
        }
        
        .form-group {
            display: flex;
            flex-direction: column;
            gap: 0.5rem;
        }
        
        .form-group label {
            font-weight: 500;
            color: #333333;
        }
        
        .form-input {
            padding: 0.75rem 1rem;
            border: 1px solid #e1e1e1;
            border-radius: 8px;
            font-family: inherit;
            font-size: 1rem;
            transition: all 0.3s;
        }
        
        .form-input:focus {
            outline: none;
            border-color: #3a86ff;
            box-shadow: 0 0 0 3px rgba(58, 134, 255, 0.15);
        }
        
        .form-input.error {
            border-color: #dc3545;
        }
        
        .input-error-message {
            color: #dc3545;
            font-size: 0.875rem;
            margin-top: 0.25rem;
        }
        
        .form-actions {
            display: flex;
            gap: 1rem;
            margin-top: 1rem;
            justify-content: flex-end;
        }
        
        .form-select {
            padding: 0.75rem 1rem;
            border: 1px solid #e1e1e1;
            border-radius: 8px;
            font-family: inherit;
            font-size: 1rem;
            transition: all 0.3s;
            background-color: white;
        }
        
        .form-select:focus {
            outline: none;
            border-color: #3a86ff;
            box-shadow: 0 0 0 3px rgba(58, 134, 255, 0.15);
        }
        
        /* File input */
        .file-input-container {
            position: relative;
        }
        
        .file-input-label {
            background-color: #f0f0f0;
            padding: 0.75rem 1rem;
            border-radius: 8px;
            border: 1px dashed #aaaaaa;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            transition: all 0.3s;
        }
        
        .file-input-label:hover {
            border-color: #3a86ff;
            background-color: #f5f9ff;
        }
        
        .file-input {
            position: absolute;
            width: 0.1px;
            height: 0.1px;
            opacity: 0;
            overflow: hidden;
            z-index: -1;
        }
        
        .file-name {
            margin-left: 0.5rem;
            font-size: 0.875rem;
            color: #666666;
        }
        
        /* Textarea */
        .form-textarea {
            padding: 0.75rem 1rem;
            border: 1px solid #e1e1e1;
            border-radius: 8px;
            font-family: inherit;
            font-size: 1rem;
            transition: all 0.3s;
            resize: vertical;
            min-height: 100px;
        }
        
        .form-textarea:focus {
            outline: none;
            border-color: #3a86ff;
            box-shadow: 0 0 0 3px rgba(58, 134, 255, 0.15);
        }
        
        /* Buttons */
        .btn-primary {
            background-color: #3a86ff;
            color: #ffffff;
            padding: 0.75rem 2rem;
            border-radius: 50px;
            font-weight: 600;
            border: none;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .btn-primary:hover {
            background-color: #2667cc;
        }
        
        .btn-secondary {
            background-color: #ff9e1b;
            color: #ffffff;
            padding: 0.75rem 1.5rem;
            border-radius: 50px;
            font-weight: 600;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s ease;
            text-align: center;
        }
        
        .btn-secondary:hover {
            background-color: #e08a15;
        }
        
        /* Responsive */
        @media (max-width: 768px) {
            .page-title {
                font-size: 1.5rem;
            }
            
            .admin-welcome h2 {
                font-size: 1.5rem;
            }
            
            .admin-form-card {
                padding: 1.5rem;
            }
            
            .form-actions {
                flex-direction: column;
            }
            .btn-primary, .btn-secondary {
              justify-content: center;
              width: 100%;
            }
        }
    </style>
</head>
<body>


<!-- Include Admin Navigation -->
<jsp:include page="/pages/admin/adminnav.jsp">
    <jsp:param name="currentPage" value="dashboard" />
</jsp:include>
    
<main class="main-content">
<jsp:include page="/pages/includes/messages.jsp" />

  <div class="admin-welcome">
    <h2>Add a new product using the form below.</h2>
    <p>Fill in all the required details to add a new product to the store.</p>
  </div>


  <!-- Add Product Form Card -->
  <div class="admin-form-wrapper">
    <div class="admin-form-card">
      <h3 class="form-title">Add Product</h3>
      
      <form action="${pageContext.request.contextPath}/AddProductServlet" method="post" class="admin-form" enctype="multipart/form-data">
        <div class="form-group">
          <label for="productName">Product Name: <span class="required">*</span></label>
          <input type="text" id="productName" name="productName" class="form-input" required>
        </div>
        
        <div class="form-group">
          <label for="categoryId">Category: <span class="required">*</span></label>
          <select id="categoryId" name="categoryId" class="form-select" required>
            <option value="">Select a category</option>
            <c:forEach var="category" items="${categories}">
              <option value="${category.id}">${category.name}</option>
            </c:forEach>
          </select>
        </div>
        
        <div class="form-group">
          <label for="price">Price ($): <span class="required">*</span></label>
          <input type="number" id="price" name="price" step="0.01" min="0" class="form-input" required>
        </div>
        
        <div class="form-group">
          <label for="quantity">Quantity: <span class="required">*</span></label>
          <input type="number" id="quantity" name="quantity" min="0" class="form-input" value="0" required>
          <small class="help-text">Enter the available stock quantity.</small>
        </div>
        
        <div class="form-group">
          <label for="speciesDescription">Species Description: <span class="required">*</span></label>
          <textarea id="speciesDescription" name="speciesDescription" class="form-textarea" required></textarea>
       
        </div>
        
        <div class="form-group">
          <label for="productImage">Product Image:</label>
          <div class="file-input-container">
            <label for="productImage" class="file-input-label">
              <i class="fas fa-upload"></i>
              <span class="upload-text">Choose an image</span>
              <span class="file-name" id="fileName"></span>
            </label>
            <input type="file" id="productImage" name="productImage" class="file-input" accept="image/*" required>
          </div>
          <small class="help-text">Recommended size: 500 x 500 pixels. Max file size: 5MB.</small>
        </div>
        
        <div class="form-actions">
          <button type="submit" class="btn-primary">Add Product</button>
          <a href="${pageContext.request.contextPath}/pages/admin/Dashboard.jsp" class="btn-secondary">Cancel</a>
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
<script>
  // Display selected filename
  document.getElementById('productImage').addEventListener('change', function() {
    var fileName = this.files[0] ? this.files[0].name : '';
    document.getElementById('fileName').textContent = fileName;
  });
</script>
</body>
</html>