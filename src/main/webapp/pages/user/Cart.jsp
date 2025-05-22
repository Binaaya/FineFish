<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- Load cart if not already loaded -->
<c:if test="${empty sessionScope.cart && not empty sessionScope.user}">
  <c:redirect url="${pageContext.request.contextPath}/CartServlet?action=view" />
</c:if>
<!-- base css -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css">

<!-- Include Header -->
<jsp:include page="/pages/includes/Header.jsp">
    <jsp:param name="title" value="Shopping Cart" />
    <jsp:param name="css" value="cart" />
</jsp:include>

<!-- Page Header -->
<header class="page-header">
  <div class="container">
    <h1 class="page-title">Your Shopping Cart</h1>
    <div class="breadcrumb">
      <a href="${pageContext.request.contextPath}/index.jsp">Home</a>
      <span>/</span>
      <span>Cart</span>
    </div>
  </div>
</header>

<!-- Main Content -->
<main class="container">
  <jsp:include page="/pages/includes/messages.jsp" />
  
  <div class="cart-container">
    <!-- If cart is empty or user not logged in -->
    <c:if test="${empty sessionScope.cart.cartProducts || empty sessionScope.user}">
      <div class="empty-cart">
        <i class="fas fa-shopping-cart"></i>
        <h2>Your cart is empty</h2>
        <p>Looks like you haven't added any items to your cart yet.</p>
        <a href="${pageContext.request.contextPath}/GetCategoriesServlet" class="btn-primary">Continue Shopping</a>
      </div>
    </c:if>
    
    <!-- If cart has items -->
    <c:if test="${not empty sessionScope.cart.cartProducts && not empty sessionScope.user}">
      <div class="cart-content">
        <div class="cart-items">
          <h2>Cart Items (${sessionScope.cartCount})</h2>
          
          <div class="cart-header">
            <span class="cart-header-product">Product</span>
            <span class="cart-header-price">Price</span>
            <span class="cart-header-quantity">Quantity</span>
            <span class="cart-header-total">Total</span>
            <span class="cart-header-action">Action</span>
          </div>
          
          <c:forEach var="item" items="${sessionScope.cart.cartProducts}">
            <div class="cart-item" data-cart-item-id="${item.cartItemId}">
              <div class="cart-item-product">
                <img src="${pageContext.request.contextPath}/${item.imageUrl}" alt="${item.productName}" />
                <div class="cart-item-details">
                  <h3>${item.productName}</h3>
                  <p>${item.productDescription}</p>
                  <c:if test="${item.availableQuantity < 5}">
                    <p class="low-stock-warning">Only ${item.availableQuantity} left in stock!</p>
                  </c:if>
                </div>
              </div>
              
              <div class="cart-item-price">$<fmt:formatNumber value="${item.price}" pattern="#,##0.00" /></div>
              
              <div class="cart-item-quantity">
                <!-- Changed to direct servlet actions instead of JavaScript -->
                <div class="quantity-control">
                  <form action="${pageContext.request.contextPath}/CartServlet" method="post" class="quantity-form">
                    <input type="hidden" name="action" value="decrease">
                    <input type="hidden" name="cartItemId" value="${item.cartItemId}">
                    <button type="submit" class="quantity-btn decrease">-</button>
                  </form>
                  
                  <span class="quantity-display">${item.quantity}</span>
                  
                  <form action="${pageContext.request.contextPath}/CartServlet" method="post" class="quantity-form">
                    <input type="hidden" name="action" value="increase">
                    <input type="hidden" name="cartItemId" value="${item.cartItemId}">
                    <button type="submit" class="quantity-btn increase">+</button>
                  </form>
                </div>
                
                <!-- Manual quantity update form -->
                <form action="${pageContext.request.contextPath}/CartServlet" method="post" class="manual-quantity-form">
                  <input type="hidden" name="action" value="update">
                  <input type="hidden" name="cartItemId" value="${item.cartItemId}">
                  <input type="number" min="1" max="${item.availableQuantity}" name="quantity" value="${item.quantity}" 
                         class="quantity-input" />
                  <button type="submit" class="update-btn">Update</button>
                </form>
              </div>
              
              <div class="cart-item-total">$<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00" /></div>
              
              
				<!-- Use this for your remove button in Cart.jsp -->
				<form action="${pageContext.request.contextPath}/CartServlet" method="post" ">
				    <input type="hidden" name="action" value="remove">
				    <input type="hidden" name="cartItemId" value="${item.cartItemId}">
				    <button type="submit" class="remove-item" onclick="return confirm('Are you sure you want to remove this item?');">
				        <i class="fas fa-trash-alt"></i>
				    </button>
				</form>              
              
            </div>
          </c:forEach>
          
          <div class="cart-actions">
            <a href="${pageContext.request.contextPath}/GetCategoriesServlet" class="btn-secondary">Continue Shopping</a>
            <form action="${pageContext.request.contextPath}/CartServlet" method="post" style="display: inline;">
              <input type="hidden" name="action" value="clear">
              <button type="submit" class="btn-danger" onclick="return confirm('Are you sure you want to clear cart?');">Clear Cart</button>
            </form>
          </div>
        </div>
        
        <div class="cart-summary">
          <h2>Order Summary</h2>
          
          <div class="summary-row">
            <span>Subtotal</span>
            <span>$<fmt:formatNumber value="${sessionScope.cart.totalAmount}" pattern="#,##0.00" /></span>
          </div>
          
          <div class="summary-row">
            <span>Shipping</span>
            <span>$<fmt:formatNumber value="10.00" pattern="#,##0.00" /></span>
          </div>
          
          <div class="summary-row total">
            <span>Total</span>
            <span>$<fmt:formatNumber value="${sessionScope.cart.totalAmount + 10.00}" pattern="#,##0.00" /></span>
          </div>
          
          <form id="checkout-form" action="${pageContext.request.contextPath}/OrderServlet" method="post">
            <input type="hidden" name="action" value="create">
            <button type="submit" id="checkout-btn" class="btn-primary btn-full">
              Proceed to Checkout
            </button>
          </form>
          
          <div class="payment-methods">
            <p>We Accept</p>
            <div class="payment-icons">
              <i class="fab fa-cc-visa"></i>
              <i class="fab fa-cc-mastercard"></i>
              <i class="fab fa-cc-amex"></i>
              <i class="fab fa-cc-paypal"></i>
            </div>
          </div>
        </div>
      </div>
    </c:if>
  </div>
</main>

<!-- Include Footer -->
<jsp:include page="/pages/includes/Footer.jsp">
    <jsp:param name="js" value="cart" />
</jsp:include>

<!-- Only modal and UI decoration JavaScript -->
<script>
document.addEventListener('DOMContentLoaded', function() {
  // No functionality JavaScript - just UI decoration
  console.log("Cart page loaded.");
});
</script>