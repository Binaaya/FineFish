<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty sessionScope.user || sessionScope.user.role != 'admin'}">
    <c:redirect url="/pages/user/Login.jsp" />
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Delete Product - FineFish Admin</title>
    <!-- Stylesheets -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <!-- Basic styles (simplified to ensure they work) -->
    <style>
            
        /* Products list */
        .products-list {
            margin-top: 2rem;
        }
        
        .products-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 1rem;
            background-color: #ffffff;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
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
        
        .products-table tr:hover {
            background-color: #f9f9f9;
        }
        
        .action-btn {
            padding: 0.5rem 1rem;
            border-radius: 4px;
            text-decoration: none;
            font-weight: 500;
            font-size: 0.875rem;
            cursor: pointer;
            border: none;
            transition: all 0.2s;
            display: inline-block;
        }
        
        .delete-btn {
            background-color: #dc3545;
            color: #ffffff;
        }
        
        .delete-btn:hover {
            background-color: #c82333;
        }
        
        /* Product Details Card */
        .product-details-card {
            background-color: #ffffff;
            border-radius: 12px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border: 1px solid #e1e1e1;
        }
        
        .product-details-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
            padding-bottom: 1rem;
            border-bottom: 1px solid #e1e1e1;
        }
        
        .product-details-title {
            font-size: 1.5rem;
            font-weight: 600;
            color: #333333;
        }
        
        .product-details-id {
            font-size: 1rem;
            color: #666666;
        }
        
        .product-details-content {
            display: grid;
            grid-template-columns: 1fr 2fr;
            gap: 2rem;
        }
        
        .product-image {
            max-width: 100%;
            height: auto;
            border-radius: 8px;
            border: 1px solid #e1e1e1;
        }
        
        .product-info {
            display: flex;
            flex-direction: column;
            gap: 1rem;
        }
        
        .product-info-item {
            display: flex;
            flex-direction: column;
            gap: 0.25rem;
        }
        
        .product-info-label {
            font-weight: 600;
            color: #555555;
        }
        
        .product-info-value {
            color: #333333;
        }
        
        .product-price {
            font-size: 1.5rem;
            font-weight: 600;
            color: #3a86ff;
        }
        
        .product-actions {
            display: flex;
            justify-content: flex-end;
            gap: 1rem;
            margin-top: 2rem;
            padding-top: 1rem;
            border-top: 1px solid #e1e1e1;
        }
        
        /* Buttons */
        .btn {
            padding: 0.75rem 1.5rem;
            border-radius: 50px;
            font-weight: 600;
            text-decoration: none;
            display: inline-block;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s ease;
            border: none;
        }
        
        .btn-primary {
            background-color: #3a86ff;
            color: #ffffff;
        }
        
        .btn-primary:hover {
            background-color: #2667cc;
        }
        
        .btn-danger {
            background-color: #dc3545;
            color: #ffffff;
        }
        
        .btn-danger:hover {
            background-color: #c82333;
        }
        
        .btn-secondary {
            background-color: #6c757d;
            color: #ffffff;
        }
        
        .btn-secondary:hover {
            background-color: #5a6268;
        }
                
        /* Confirmation Modal */
        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            align-items: center;
            justify-content: center;
        }
        
        .modal-content {
            background-color: #ffffff;
            border-radius: 8px;
            max-width: 500px;
            width: 90%;
            padding: 2rem;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }
        
        .modal-title {
            font-size: 1.5rem;
            font-weight: 600;
            margin-bottom: 1rem;
            color: #333333;
        }
        
        .modal-message {
            margin-bottom: 1.5rem;
            color: #555555;
        }
        
        .modal-actions {
            display: flex;
            justify-content: flex-end;
            gap: 1rem;
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
        
        /* Responsive */
        @media (max-width: 768px) {
            .page-title {
                font-size: 1.5rem;
            }
            
            .admin-welcome h2 {
                font-size: 1.5rem;
            }
            
            .product-details-content {
                grid-template-columns: 1fr;
            }
            
            .product-actions {
                flex-direction: column;
            }
            
            .btn {
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
            }
            
            .search-box {
                flex-direction: column;
            }
            
            .search-btn {
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
    <h2>Delete Products</h2>
    <p>Select a product from the list below to delete it. Warning: This action cannot be undone!</p>
  </div>


  <!-- Product Details Card (shown only when a product is selected) -->
  <c:if test="${not empty product}">
    <div class="product-details-card">
      <div class="product-details-header">
        <h3 class="product-details-title">${product.name}</h3>
        <span class="product-details-id">ID: ${product.id}</span>
      </div>
      
      <div class="product-details-content">
        <div>
          <c:if test="${not empty product.photo}">
            <img src="${pageContext.request.contextPath}/images/products/${product.photo}" 
                 alt="${product.name}" class="product-image">
          </c:if>
          <c:if test="${empty product.photo}">
            <div class="no-image">No image available</div>
          </c:if>
        </div>
        
        <div class="product-info">
          <div class="product-info-item">
            <span class="product-info-label">Category:</span>
            <span class="product-info-value">${product.categoryName}</span>
          </div>
          
          <div class="product-info-item">
            <span class="product-info-label">Price:</span>
            <span class="product-info-value product-price">$ ${product.price}</span>
          </div>
          
          <div class="product-actions">
            <button class="btn btn-secondary" id="cancelDeleteBtn">Cancel</button>
            <a href="${pageContext.request.contextPath}/DeleteProductServlet?productId=${product.id}" 
               class="btn btn-danger confirm-delete-btn">
              Delete Product
            </a>
          </div>
        </div>
      </div>
    </div>
  </c:if>
  
  <!-- Search Box -->
  <div class="search-container">
    <form action="${pageContext.request.contextPath}/DeleteProductServlet" method="get" class="search-box">
      <input type="text" name="search" class="search-input" placeholder="Search products by name or ID..." value="${param.search}">
      <button type="submit" class="search-btn">
        <i class="fas fa-search"></i> Search
      </button>
    </form>
    <p class="search-tip">Tip: You can search by product name or product ID</p>
  </div>

  <!-- Products Table -->
  <div class="products-list">
    <h3>Available Products</h3>
    <p>Click on the "Delete" button next to a product to remove it.</p>
    
    <table class="products-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Category</th>
          <th>Price</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="product" items="${products}">
          <tr>
            <td>${product.id}</td>
            <td>${product.name}</td>
            <td>${product.categoryName}</td>
            <td>${product.price}</td>
            <td>
              <a href="${pageContext.request.contextPath}/DeleteProductServlet?productId=${product.id}&view=true" 
                 class="action-btn delete-btn">
                <i class="fas fa-trash-alt"></i> Delete
              </a>
            </td>
          </tr>
        </c:forEach>
        
        <c:if test="${empty products}">
          <tr>
            <td colspan="5" style="text-align: center;">No products found.</td>
          </tr>
        </c:if>
      </tbody>
    </table>
  </div>

  <!-- Confirmation Modal -->
  <div class="modal" id="confirmModal">
    <div class="modal-content">
      <h3 class="modal-title">Delete Confirmation</h3>
      <p class="modal-message" id="confirmMessage"></p>
      <div class="modal-actions">
        <button class="btn btn-secondary" id="cancelDelete">Cancel</button>
        <a href="#" class="btn btn-danger" id="confirmDelete">Delete</a>
      </div>
    </div>
  </div>

  <!-- Back to Dashboard Link -->
  <div class="admin-back-link">
    <a href="${pageContext.request.contextPath}/pages/admin/Dashboard.jsp">
      <i class="fas fa-arrow-left"></i> Back to Dashboard
    </a>
  </div>
</main>
<script src="${pageContext.request.contextPath}/js/script.js"></script>

<script>
window.onload = function() {
  // Get modal elements
  const modal = document.getElementById('confirmModal');
  const confirmMessage = document.getElementById('confirmMessage');
  const confirmDelete = document.getElementById('confirmDelete');

  // Get the delete button
  const confirmDeleteBtn = document.querySelector('.confirm-delete-btn');
  
  if (confirmDeleteBtn) {
    confirmDeleteBtn.addEventListener('click', function(e) {
      e.preventDefault();  // Prevent the default link action
      
      // Set the simplified confirmation message in the modal
      confirmMessage.innerHTML = 'Are you sure you want to <strong>delete</strong> <strong>this</strong> product? This action cannot be undone!';

      // Set the confirm button href with the delete link
      confirmDelete.href = this.href;

      // Show the modal
      modal.style.display = 'flex';
    });
  }

  // Close the modal when cancel is clicked
  const cancelDelete = document.getElementById('cancelDelete');
  if (cancelDelete) {
    cancelDelete.addEventListener('click', function() {
      modal.style.display = 'none';  // Hide the modal
    });
  }

  // Handle the cancel button in product details card
  const cancelDeleteBtn = document.getElementById('cancelDeleteBtn');
  if (cancelDeleteBtn) {
    cancelDeleteBtn.addEventListener('click', function() {
      window.location.href = "${pageContext.request.contextPath}/DeleteProductServlet";
    });
  }

  // Close the modal if clicked outside
  window.addEventListener('click', function(e) {
    if (e.target === modal) {
      modal.style.display = 'none';  // Hide the modal
    }
  });
};
</script>
</body>
</html>