<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- Include Header -->
<jsp:include page="/pages/includes/Header.jsp">
    <jsp:param name="title" value="Home" />
    <jsp:param name="css" value="style" />
    <jsp:param name="active" value="home" />
</jsp:include>

<!-- Hero Section -->
<section class="hero">
  <div class="container">
    <div class="hero-content">
      <div class="hero-text">
        <h1 class="hero-title">Fresh Seafood Delivered to Your Door</h1>
        <p class="hero-subtitle">
          Sustainably sourced, expertly handled, and delivered fresh from the ocean to your table.
        </p>
        <div class="hero-buttons">
          <a href="${pageContext.request.contextPath}/GetCategoriesServlet" class="btn-primary">Shop Now <i class="fas fa-arrow-right"></i></a>
        </div>
      </div>
      <div class="hero-image">
        <img src="${pageContext.request.contextPath}/uploads/fishindex.jpeg"
              alt="Fresh seafood selection" />
      </div>
    </div>
  </div>
</section>

<!-- Featured Products Section -->
<section class="featured-products">
  <div class="container">
    <h2 class="section-title">Featured Products</h2>
    <p class="section-subtitle">Discover our most popular seafood offerings</p>
    
    <div class="products-grid" id="productsGrid">
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
                
              </div>
            </c:forEach>
    </div>
    </div>
    <div class="view-all">
      <a href="${pageContext.request.contextPath}/GetCategoriesServlet" class="btn-secondary">View All Products</a>
    </div>
</section>

<!-- Why Choose Us Section -->
<section class="why-us">
  <div class="container">
    <h2 class="section-title">Why Choose FineFish?</h2>
    <p class="section-subtitle">What sets us apart from the competition</p>
    
    <div class="features-grid">
      <div class="feature">
        <div class="feature-icon">
          <i class="fas fa-fish"></i>
        </div>
        <h3>Freshness Guaranteed</h3>
        <p>All our seafood is delivered within 24-48 hours of catch, ensuring peak freshness</p>
      </div>
      
      <div class="feature">
        <div class="feature-icon">
          <i class="fas fa-leaf"></i>
        </div>
        <h3>Sustainably Sourced</h3>
        <p>We partner with fishermen who use responsible fishing methods to protect our oceans</p>
      </div>
      
      <div class="feature">
        <div class="feature-icon">
          <i class="fas fa-shipping-fast"></i>
        </div>
        <h3>Fast Delivery</h3>
        <p>Temperature-controlled packaging and expedited shipping for optimal quality</p>
      </div>
      
      <div class="feature">
        <div class="feature-icon">
          <i class="fas fa-award"></i>
        </div>
        <h3>Premium Quality</h3>
        <p>Chef-approved seafood that meets the highest standards of quality and taste</p>
      </div>
    </div>
  </div>
</section>
      

<!-- Newsletter -->
    <section class="newsletter">
      <div class="container">
        <h2>Join Our Seafood Community</h2>
        <p>
          Sign up for our newsletter to receive exclusive offers, seasonal
          recipes, and the latest on sustainable seafood.
        </p>

        <form class="newsletter-form"  method="POST">
          <input type="email" name="email" placeholder="Your email address" class="newsletter-input" required />
          <button type="submit" class="btn-primary">Subscribe</button>
        </form>
      </div>
    </section>


<!-- Include Footer -->
<jsp:include page="/pages/includes/Footer.jsp">
    <jsp:param name="js" value="home" />
</jsp:include>