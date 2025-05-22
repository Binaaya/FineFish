<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
        
<!-- Include Header -->
<jsp:include page="/pages/includes/Header.jsp">
    <jsp:param name="title" value="Products" />
    <jsp:param name="css" value="product" />
    <jsp:param name="active" value="products" />
</jsp:include>

<!-- Page Header -->
<header class="page-header">
  <div class="container">
    <h1 class="page-title">Our Premium Seafood Selection</h1>
    <div class="breadcrumb">
      <a href="${pageContext.request.contextPath}/index.jsp">Home</a>
      <span>/</span>
      <span>Products</span>
    </div>
  </div>
</header>

<!-- Main Content -->
<main class="container main-container">
  <jsp:include page="/pages/includes/messages.jsp" />
  
  <div class="main-content">
    <!-- Sidebar -->
    <aside class="sidebar">
      <h2 class="sidebar-title">Categories</h2>
      <ul class="category-list" id="categoryList">
        <li class="category-item">
          <a href="${pageContext.request.contextPath}/GetCategoriesServlet" 
             class="category-link ${param.categoryId == null ? 'active' : ''}">
            All Products <span class="category-count">(${totalProductCount})</span>
          </a>
        </li>
        <c:forEach var="category" items="${categories}">
          <li class="category-item">
            <a href="${pageContext.request.contextPath}/GetCategoriesServlet?categoryId=${category.id}" 
               class="category-link ${param.categoryId == category.id ? 'active' : ''}">
              ${category.name} <span class="category-count">(${category.productCount})</span>
            </a>
          </li>
        </c:forEach>
      </ul>
    </aside>

    <!-- Products -->
    <div class="products-container">
      <div class="products-header">
		<h2 class="products-title" id="productsTitle">
		  <c:choose>
		    <c:when test="${not empty searchTerm}">
		      Search Results for: "${searchTerm}" (${fn:length(products)} results)
		    </c:when>
		    <c:when test="${not empty categoryName}">
		      ${categoryName}
		    </c:when>
		    <c:otherwise>
		      All Products
		    </c:otherwise>
		  </c:choose>
		</h2>
        <select class="sort-options" id="sortOptions">
          <option value="featured">Sort by: Featured</option>
          <option value="price-asc" ${param.sort == 'price-asc' ? 'selected' : ''}>Price: Low to High</option>
          <option value="price-desc" ${param.sort == 'price-desc' ? 'selected' : ''}>Price: High to Low</option>
          <option value="newest" ${param.sort == 'newest' ? 'selected' : ''}>Newest First</option>
        </select>
      </div>

      <div class="products-grid" id="productsGrid">
        <c:choose>
          <c:when test="${empty products}">
            <div class="empty-state">
              <i class="fas fa-fish"></i>
              <h3>No products found</h3>
              <p>We couldn't find any products matching your criteria. Try a different category or search term.</p>
            </div>
          </c:when>
          <c:otherwise>
            <c:forEach var="product" items="${products}">
              <div class="product-card" onclick="openProductDetails(this)">
                <div class="product-image">
                  <img src="${pageContext.request.contextPath}/images/products/${product.photo}" alt="${product.name}" />
                  <c:if test="${product.quantity == 0}">
                    <div class="product-tag out-of-stock">Out of Stock</div>
                  </c:if>
                  <c:if test="${product.quantity > 0 && product.quantity < 5}">
                    <div class="product-tag low-stock">Only ${product.quantity} left!</div>
                  </c:if>
                </div>
                <div class="product-info">
                  <h3 class="product-name">${product.name}</h3>
                  <p class="product-description">${product.categoryName}</p>
                  <div class="product-footer">
                    <span class="product-price">$<fmt:formatNumber value="${product.price}" pattern="#,##0.00" /></span>
                    <c:choose>
                      <c:when test="${product.quantity > 0}">
                        <button class="add-to-cart" onclick="event.stopPropagation(); addToCart(${product.id}, 1)">
                          Add to Cart
                        </button>
                      </c:when>
                      <c:otherwise>
                        <button class="out-of-stock-btn" disabled>Out of Stock</button>
                      </c:otherwise>
                    </c:choose>
                  </div>
                </div>
                <!-- Hidden Product Details -->
                <div class="product-details-hidden">
                  <span class="product-id">${product.id}</span>
                  <span class="product-full-description">${product.description}</span>
                  <span class="product-quantity">${product.quantity}</span>
                </div>
              </div>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  </div>
  
  <!-- Product Detail Modal -->
  <div id="productModal" class="product-modal">
    <div class="product-modal-content">
      <span class="close-modal">&times;</span>
      <div class="product-modal-inner">
        <div class="product-modal-image">
          <img id="modalProductImage" src="" alt="Product Image">
          <div id="modalProductTag" class="product-tag"></div>
        </div>
        <div class="product-modal-info">
          <h2 id="modalProductName"></h2>
          <p id="modalProductCategory" class="product-category"></p>
          <div class="product-price-section">
            <span id="modalProductPrice" class="modal-price"></span>
          </div>
          <div class="product-description-section">
            <h3>Description</h3>
            <p id="modalProductDescription"></p>
          </div>
          <div class="product-quantity-section">
            <label for="quantityInput">Quantity:</label>
            <div class="quantity-controls">
              <button onclick="decreaseQuantity()">-</button>
              <input type="number" id="quantityInput" value="1" min="1">
              <button onclick="increaseQuantity()">+</button>
            </div>
          </div>
          <div class="modal-actions">
            <button id="modalAddToCartBtn" class="btn-secondary" onclick="addToCartFromModal()">
              Add to Cart
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</main>

<!-- Include Footer -->
<jsp:include page="/pages/includes/Footer.jsp">
    <jsp:param name="js" value="product" />
</jsp:include>

<style>
/* Additional CSS for product page */
.product-tag.out-of-stock {
    background-color: #dc3545;
    color: white;
}

.product-tag.low-stock {
    background-color: #ffc107;
    color: #333;
}

.out-of-stock-btn {
    background-color: #dc3545;
    color: white;
    border: none;
    padding: 0.5rem 1rem;
    border-radius: 50px;
    font-weight: 600;
    cursor: not-allowed;
    opacity: 0.7;
}

.message {
    position: fixed;
    top: 20px;
    right: 20px;
    padding: 1rem 1.5rem;
    border-radius: 4px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    z-index: 9999;
    opacity: 0;
    transform: translateY(-20px);
    transition: opacity 0.3s, transform 0.3s;
}

.message.show {
    opacity: 1;
    transform: translateY(0);
}

.message.fade-out {
    opacity: 0;
    transform: translateY(-20px);
}

.message.success {
    background-color: #d4edda;
    color: #155724;
    border-left: 4px solid #28a745;
}

.message.error {
    background-color: #f8d7da;
    color: #721c24;
    border-left: 4px solid #dc3545;
}

.main-container {
    padding-bottom: 4rem;
}

/* Product Card Styles */
.product-card {
    cursor: pointer;
    transition: all 0.3s ease;
}

.product-details-hidden {
    display: none;
}

/* Product Modal Styles */
.product-modal {
    display: none;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 1000;
    justify-content: center;
    align-items: center;
}

.product-modal-content {
    background-color: white;
    border-radius: 8px;
    width: 90%;
    max-width: 900px;
    max-height: 90vh;
    overflow-y: auto;
    position: relative;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.close-modal {
    position: absolute;
    top: 15px;
    right: 15px;
    font-size: 24px;
    cursor: pointer;
    z-index: 10;
    width: 30px;
    height: 30px;
    background: rgba(255, 255, 255, 0.8);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
}

.product-modal-inner {
    display: flex;
    flex-direction: column;
}

.product-modal-image {
    position: relative;
    width: 100%;
    height: 300px;
}

.product-modal-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.product-modal-info {
    padding: 2rem;
}

.product-category {
    color: #666;
    margin-bottom: 1rem;
}

.modal-price {
    font-size: 1.5rem;
    font-weight: 700;
    color: #333;
    display: block;
    margin-bottom: 1.5rem;
}

.product-description-section h3 {
    font-size: 1.2rem;
    margin-bottom: 0.5rem;
}

.product-description-section p {
    margin-bottom: 1.5rem;
    line-height: 1.6;
}

.product-quantity-section {
    margin-bottom: 1.5rem;
}

.quantity-controls {
    display: flex;
    align-items: center;
    margin-top: 0.5rem;
}

.quantity-controls button {
    width: 30px;
    height: 30px;
    background-color: #f0f0f0;
    border: none;
    border-radius: 4px;
    font-size: 1.2rem;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
}

.quantity-controls input {
    width: 50px;
    height: 30px;
    text-align: center;
    margin: 0 0.5rem;
    border: 1px solid #ddd;
    border-radius: 4px;
}

.modal-actions {
    margin-top: 1rem;
}

.modal-actions button {
    padding: 0.75rem 2rem;
    font-size: 1rem;
}

/* Media queries for responsive modal */
@media (min-width: 768px) {
    .product-modal-inner {
        flex-direction: row;
    }
    
    .product-modal-image {
        width: 50%;
        height: auto;
    }
    
    .product-modal-info {
        width: 50%;
    }
}
</style>

<script>
// Wait for document to be ready
document.addEventListener('DOMContentLoaded', function() {
    // Get sort options dropdown
    const sortOptions = document.getElementById('sortOptions');
    
    // Add event listener for sorting options
    if (sortOptions) {
        sortOptions.addEventListener('change', function() {
            // Get current URL and its parameters
            const url = new URL(window.location.href);
            
            // Update or add sort parameter
            url.searchParams.set('sort', this.value);
            
            // Navigate to the new URL
            window.location.href = url.toString();
        });
    }
    
    // Close modal when clicking on the X button
    document.querySelector('.close-modal').addEventListener('click', function() {
        document.getElementById('productModal').style.display = 'none';
    });
    
    // Close modal when clicking outside the modal content
    window.addEventListener('click', function(event) {
        const modal = document.getElementById('productModal');
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
});

// Global variable to store current product ID for modal
let currentProductId = 0;
let currentProductMaxQuantity = 0;

// Function to open product details modal
function openProductDetails(productCard) {
    // Get product info from the card
    const productName = productCard.querySelector('.product-name').textContent;
    const productPrice = productCard.querySelector('.product-price').textContent;
    const productCategory = productCard.querySelector('.product-description').textContent;
    const productImage = productCard.querySelector('.product-image img').src;
    
    // Get hidden details
    const productId = productCard.querySelector('.product-id').textContent;
    const productDescription = productCard.querySelector('.product-full-description').textContent;
    const productQuantity = parseInt(productCard.querySelector('.product-quantity').textContent);
    
    // Store current product ID and max quantity globally
    currentProductId = productId;
    currentProductMaxQuantity = productQuantity;
    
    // Set values in modal
    document.getElementById('modalProductName').textContent = productName;
    document.getElementById('modalProductPrice').textContent = productPrice;
    document.getElementById('modalProductCategory').textContent = productCategory;
    document.getElementById('modalProductImage').src = productImage;
    document.getElementById('modalProductDescription').textContent = productDescription || 'No description available';
    
    // Reset quantity input to 1
    document.getElementById('quantityInput').value = 1;
    
    // Set max attribute on quantity input
    document.getElementById('quantityInput').max = productQuantity;
    
    // Handle tags and button state based on quantity
    const modalProductTag = document.getElementById('modalProductTag');
    const addToCartBtn = document.getElementById('modalAddToCartBtn');
    const quantitySection = document.querySelector('.product-quantity-section');
    
    if (productQuantity === 0) {
        // Out of stock
        modalProductTag.textContent = 'Out of Stock';
        modalProductTag.className = 'product-tag out-of-stock';
        modalProductTag.style.display = 'block';
        
        addToCartBtn.disabled = true;
        addToCartBtn.textContent = 'Out of Stock';
        addToCartBtn.className = 'out-of-stock-btn';
        
        quantitySection.style.display = 'none';
    } else if (productQuantity < 5) {
        // Low stock
        modalProductTag.textContent = 'Only ' + productQuantity + ' left!';
        modalProductTag.className = 'product-tag low-stock';
        modalProductTag.style.display = 'block';
        
        addToCartBtn.disabled = false;
        addToCartBtn.textContent = 'Add to Cart';
        addToCartBtn.className = 'btn-secondary';
        
        quantitySection.style.display = 'block';
    } else {
        // In stock
        modalProductTag.style.display = 'none';
        
        addToCartBtn.disabled = false;
        addToCartBtn.textContent = 'Add to Cart';
        addToCartBtn.className = 'btn-secondary';
        
        quantitySection.style.display = 'block';
    }
    
    // Display the modal
    document.getElementById('productModal').style.display = 'flex';
}

// Function to decrease quantity in modal
function decreaseQuantity() {
    const input = document.getElementById('quantityInput');
    const currentValue = parseInt(input.value);
    if (currentValue > 1) {
        input.value = currentValue - 1;
    }
}

// Function to increase quantity in modal
function increaseQuantity() {
    const input = document.getElementById('quantityInput');
    const currentValue = parseInt(input.value);
    if (currentValue < currentProductMaxQuantity) {
        input.value = currentValue + 1;
    }
}

// Function to add to cart from modal
function addToCartFromModal() {
    const quantity = parseInt(document.getElementById('quantityInput').value);
    addToCart(currentProductId, quantity);
    // Close modal after adding to cart
    document.getElementById('productModal').style.display = 'none';
}

// Function to add product to cart
function addToCart(productId, quantity) {
    // Check if user is logged in
    const isLoggedIn = ${not empty sessionScope.user};
    
    if (!isLoggedIn) {
        // Redirect to login page
        window.location.href = '${pageContext.request.contextPath}/LogInController';
        return;
    }
    
    // Redirect to CartServlet to add to cart
    window.location.href = '${pageContext.request.contextPath}/CartServlet?action=add&productId=' + productId + '&quantity=' + quantity;
}

// Function to show message
function showMessage(message, type) {
    // Create message element
    const messageElement = document.createElement('div');
    messageElement.className = 'message ' + type;
    messageElement.textContent = message;
    
    // Append message to body
    document.body.appendChild(messageElement);
    
    // Add fade-in class for animation
    setTimeout(() => {
        messageElement.classList.add('show');
    }, 10);
    
    // Remove message after 3 seconds
    setTimeout(() => {
        messageElement.classList.remove('show');
        messageElement.classList.add('fade-out');
        setTimeout(() => {
            document.body.removeChild(messageElement);
        }, 500);
    }, 3000);
}
</script>