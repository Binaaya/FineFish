<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty sessionScope.user || sessionScope.user.role != 'admin'}">
    <c:redirect url="/pages/user/Login.jsp" />
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Update Product - FineFish Admin</title>
    <!-- Stylesheets -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <!-- Basic styles  -->
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
        
        /* Current image preview */
        .current-image {
            margin-top: 0.5rem;
            display: flex;
            align-items: center;
            gap: 1rem;
        }
        
        .current-image img {
            max-width: 100px;
            max-height: 100px;
            border-radius: 4px;
            border: 1px solid #e1e1e1;
        }
        
        .current-image-text {
            font-size: 0.875rem;
            color: #666666;
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
            background-color: #6c757d;
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
            background-color: #5a6268;
        }
        
        /* Products list */
        .products-list {
            margin-top: 3rem;
        }
        
        .products-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 1rem;
            background-color: #ffffff;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        
        .products-table th,
        .products-table td {
            padding: 1rem;
            text-align: left;
            border-bottom: 1px solid #e1e1e1;
        }
        
        .products-table th {
            background-color: #f5f5f5;
            font-weight: 600;
        }
        
        .products-table tr:last-child td {
            border-bottom: none;
        }
        
        .action-btn {
            padding: 0.5rem 1rem;
            border-radius: 4px;
            text-decoration: none;
            font-weight: 500;
            font-size: 0.875rem;
            cursor: pointer;
            transition: all 0.2s;
            display: inline-block;
            margin-right: 0.5rem;
        }
        
        .edit-btn {
            background-color: #3a86ff;
            color: #ffffff;
        }
        
        .edit-btn:hover {
            background-color: #2667cc;
        }
        
        .delete-btn {
            background-color: #dc3545;
            color: #ffffff;
        }
        
        .delete-btn:hover {
            background-color: #c82333;
        }
        /* Search Box */
        .search-container {
            margin-bottom: 2rem;
        }
        
        .search-box {
            display: flex;
            gap: 0.5rem;
        }
        
        .search-input {
            flex: 1;
            padding: 0.75rem 1rem;
            border: 1px solid #e1e1e1;
            border-radius: 50px;
            font-family: inherit;
            font-size: 1rem;
        }
        
        .search-input:focus {
            outline: none;
            border-color: #3a86ff;
            box-shadow: 0 0 0 3px rgba(58, 134, 255, 0.15);
        }
        
        .search-btn {
            background-color: #3a86ff;
            color: #ffffff;
            padding: 0.75rem 1.5rem;
            border-radius: 50px;
            font-weight: 600;
            border: none;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .search-btn:hover {
            background-color: #2667cc;
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
                width: 100%;
            }
            
            .products-table {
                font-size: 0.875rem;
            }
            
            .products-table th,
            .products-table td {
                padding: 0.75rem 0.5rem;
            }
            
            .action-btn {
                padding: 0.4rem 0.8rem;
                font-size: 0.75rem;
                margin-bottom: 0.5rem;
                display: block;
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
  <!-- Welcome/Intro Section -->
  <div class="admin-welcome">
    <c:choose>
      <c:when test="${not empty product}">
        <h2>Update Product Details</h2>
        <p>Make changes to the product information and click "Update Product" to save.</p>
      </c:when>
      <c:otherwise>
        <h2>Manage Products</h2>
        <p>Select a product from the list below to update its details.</p>
      </c:otherwise>
    </c:choose>
  </div>

  <!-- Update Product Form -->
  <c:if test="${not empty product}">
    <div class="admin-form-wrapper">
      <div class="admin-form-card">
        <h3 class="form-title">Update Product</h3>
        
        <form action="${pageContext.request.contextPath}/UpdateProductServlet" method="post" class="admin-form" enctype="multipart/form-data">
          <!-- Hidden Product ID -->
          <input type="hidden" name="productId" value="${product.id}">
          
          <div class="form-group">
            <label for="productName">Product Name: <span class="required">*</span></label>
            <input type="text" id="productName" name="productName" class="form-input" value="${product.name}" required>
          </div>
          
          <div class="form-group">
            <label for="categoryId">Category: <span class="required">*</span></label>
            <select id="categoryId" name="categoryId" class="form-select" required>
              <option value="">Select a category</option>
              <c:forEach var="category" items="${categories}">
                <option value="${category.id}" ${category.id eq product.categoryId ? 'selected' : ''}>
                  ${category.name}
                </option>
              </c:forEach>
            </select>
          </div>
          
          <div class="form-group">
            <label for="price">Price ($): <span class="required">*</span></label>
            <input type="number" id="price" name="price" step="0.01" min="0" class="form-input" value="${product.price}" required>
          </div>
          
          <div class="form-group">
            <label for="quantity">Quantity: <span class="required">*</span></label>
            <input type="number" id="quantity" name="quantity" min="0" class="form-input" value="${product.quantity}" required>
          </div>
          <div class="form-group">
    <label for="speciesDescription">Species Description: <span class="required">*</span></label>
    <textarea id="speciesDescription" name="speciesDescription" class="form-textarea" required>${product.description}</textarea>
</div>
          <div class="form-group">
            <label for="productImage">Product Image:</label>
            <div class="file-input-container">
              <label for="productImage" class="file-input-label">
                <i class="fas fa-upload"></i>
                <span class="upload-text">Choose a new image</span>
                <span class="file-name" id="fileName"></span>
              </label>
              <input type="file" id="productImage" name="productImage" class="file-input" accept="image/*">
            </div>
            <small class="help-text">Recommended size: 500 x 500 pixels. Max file size: 5MB.</small>
            
            <c:if test="${not empty product.photo}">
              <div class="current-image">
                <img src="${pageContext.request.contextPath}/images/products/${product.photo}" alt="${product.name}">
                <div class="current-image-text">
                  <p>Current image: ${product.photo}</p>
                  <small>Leave the image field empty to keep the current image.</small>
                </div>
              </div>
            </c:if>
          </div>
          
          <div class="form-actions">
            <a href="${pageContext.request.contextPath}/GetCategoriesServlet?destination=admin" class="btn-secondary">Cancel</a>
            <button type="submit" class="btn-primary">Update Product</button>
          </div>
        </form>
      </div>
    </div>
  </c:if>

 <!-- Search Box -->
  <div class="search-container">
    <form action="${pageContext.request.contextPath}/UpdateProductServlet" method="get" class="search-box">
      <input type="text" name="search" class="search-input" placeholder="Search products by name or ID..." value="${param.search}">
      <button type="submit" class="search-btn">
        <i class="fas fa-search"></i> Search
      </button>
    </form>
    <p class="search-tip">Tip: You can search by product name or product ID</p>
  </div>

  <!-- Products Table -->
  <div class="products-list">
    <h3>All Products</h3>
    <p>Click on "Edit" to update a product's details. You can search by product name or product ID.</p>
    
    <table class="products-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Category</th>
          <th>Price</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="prod" items="${products}">
          <tr>
            <td>${prod.id}</td>
            <td>${prod.name}</td>
            <td>${prod.categoryName}</td>
            <td>$${prod.price}</td>
            <td>
              <a href="${pageContext.request.contextPath}/UpdateProductServlet?productId=${prod.id}" class="action-btn edit-btn">
                <i class="fas fa-edit"></i> Edit
              </a>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
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