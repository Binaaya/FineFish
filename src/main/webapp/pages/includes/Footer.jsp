<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!-- Footer -->
    <footer class="footer">
      <div class="container">
        <div class="footer-grid">
          <div class="footer-about">
            <div class="footer-logo">
              <div class="footer-logo-icon">
                <i class="fas fa-fish"></i>
              </div>
              <span>FineFish</span>
            </div>
            <p>Sustainable seafood for everyone.</p>
            <div class="social-icons">
              <a href="#" class="social-icon">
                <i class="fab fa-facebook-f"></i>
              </a>
              <a href="#" class="social-icon">
                <i class="fab fa-instagram"></i>
              </a>
              <a href="#" class="social-icon">
                <i class="fab fa-twitter"></i>
              </a>
            </div>
          </div>

          <div class="footer-links">
            <h3>Shop</h3>
            <ul>
              <li><a href="${pageContext.request.contextPath}/GetCategoriesServlet">All Products</a></li>
              <li><a href="${pageContext.request.contextPath}/GetCategoriesServlet">New Arrivals</a></li>
              <li><a href="${pageContext.request.contextPath}/GetCategoriesServlet">Best Sellers</a></li>
              <li><a href="${pageContext.request.contextPath}/GetCategoriesServlet">Seasonal Specials</a></li>
            </ul>
          </div>

          <div class="footer-links">
            <h3>About</h3>
            <ul>
              <li><a href="${pageContext.request.contextPath}/pages/user/About.jsp">Our Story</a></li>
              <li><a href="${pageContext.request.contextPath}/pages/user/About.jsp#sustainability">Sustainability</a></li>
              <li><a href="${pageContext.request.contextPath}/pages/user/About.jsp#partners">Fishermen Partners</a></li>
              <li><a href="${pageContext.request.contextPath}/pages/user/About.jsp#careers">Careers</a></li>
            </ul>
          </div>

          <div class="footer-links">
            <h3>Help</h3>
            <ul>
              <li><a href="${pageContext.request.contextPath}/pages/footercontents/Contact.jsp"target="_blank">Contact Us</a></li>
              <li><a href="${pageContext.request.contextPath}/pages/footercontents/FAQ.jsp"target="_blank">FAQs</a></li>
              <li><a href="${pageContext.request.contextPath}/pages/footercontents/Shipping.jsp"target="_blank">Shipping Info</a></li>
              <li><a href="${pageContext.request.contextPath}/pages/footercontents/Returns.jsp"target="_blank">Returns Policy</a></li>
            </ul>
          </div>
        </div>

        <div class="footer-bottom">
          <p>&copy; 2023 FineFish. All rights reserved.</p>
          <div class="footer-legal">
            <a href="${pageContext.request.contextPath}/pages/footercontents/Privacy.jsp"target="_blank">Privacy Policy</a>
            <a href="${pageContext.request.contextPath}/pages/footercontents/Terms.jsp" target="_blank">Terms of Service</a>
          </div>
        </div>
      </div>
    </footer>

    <!-- JavaScript files -->
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
    
</body>
</html>