<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- Check if user is logged in -->
<c:if test="${empty sessionScope.user}">
    <c:redirect url="/pages/user/Login.jsp" />
</c:if>

<%
    // Check if orders data is available - if not, fetch it
    if (request.getAttribute("orders") == null) {
        // Forward to OrderServlet to load orders
        request.getRequestDispatcher("/OrderServlet?action=history").include(request, response);
    }
%>
<!-- base css -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/userdashboard.css">

<!-- Include Header -->
<jsp:include page="/pages/includes/Header.jsp">
    <jsp:param name="title" value="My Account" />
    <jsp:param name="css" value="dashboard" />
</jsp:include>

<!-- Page Header -->
<header class="page-header">
  <div class="container">
    <h1 class="page-title">My Account</h1>
    <div class="breadcrumb">
      <a href="${pageContext.request.contextPath}/index.jsp">Home</a>
      <span>/</span>
      <span>My Account</span>
    </div>
  </div>
</header>

<!-- Main Content -->
<main class="container">
  <!-- Simple Messages Section -->
  <c:if test="${not empty sessionScope.errorMessage}">
    <div class="error-message">
      ${sessionScope.errorMessage}
      <% session.removeAttribute("errorMessage"); %>
    </div>
  </c:if>
  <c:if test="${not empty sessionScope.successMessage}">
    <div class="success-message">
      ${sessionScope.successMessage}
      <% session.removeAttribute("successMessage"); %>
    </div>
  </c:if>
  
  <div class="dashboard-container">
    <!-- Sidebar -->
    <aside class="dashboard-sidebar">
      <div class="user-info">
        <div class="user-avatar">
          <i class="fas fa-user-circle"></i>
        </div>
        <div class="user-details">
          <h3>${sessionScope.user.fname} ${sessionScope.user.surname}</h3>
          <p>${sessionScope.user.email}</p>
        </div>
      </div>
      
      <ul class="dashboard-nav">
        <li class="active" data-section="profile-section">
          <a href="#profile-section">
            <i class="fas fa-user"></i> My Profile
          </a>
        </li>
        <li data-section="orders-section">
          <a href="#orders-section">
            <i class="fas fa-shopping-bag"></i> My Orders
          </a>
        </li>
        <li data-section="password-section">
          <a href="#password-section">
            <i class="fas fa-lock"></i> Change Password
          </a>
        </li>
        <li>
          <a href="${pageContext.request.contextPath}/DeleteUserServlet" class="delete-account-link">
            <i class="fas fa-user-times"></i> Delete Account
          </a>
        </li>
        <li>
          <a href="${pageContext.request.contextPath}/LogOutController">
            <i class="fas fa-sign-out-alt"></i> Logout
          </a>
        </li>
      </ul>
    </aside>
    
    <!-- Dashboard Content -->
    <div class="dashboard-content">
      <!-- Profile Section -->
      <section id="profile-section" class="dashboard-section">
        <div class="section-header">
          <h2><i class="fas fa-user"></i> My Profile</h2>
        </div>
        <div class="section-content">
          <form id="profile-form" action="${pageContext.request.contextPath}/UpdateProfileServlet" method="post">
            <!-- Hidden input for full name to satisfy servlet requirement -->
            <input type="hidden" id="fullName" name="name" value="${sessionScope.user.fname} ${sessionScope.user.surname}">
            
            <div class="form-group">
              <label for="profileFname">First Name</label>
              <input type="text" id="profileFname" name="fname" value="${sessionScope.user.fname}" class="form-input" required 
                     onchange="updateFullName()">
            </div>
            
            <div class="form-group">
              <label for="profileSurname">Last Name</label>
              <input type="text" id="profileSurname" name="surname" value="${sessionScope.user.surname}" class="form-input" required
                     onchange="updateFullName()">
            </div>
            
            <div class="form-group">
              <label for="profileUsername">Username</label>
              <input type="text" id="profileUsername" name="username" value="${sessionScope.user.username}" class="form-input readonly" readonly>
              <small class="input-hint">Username cannot be changed</small>
            </div>
            
            <div class="form-group">
              <label for="profileEmail">Email</label>
              <input type="email" id="profileEmail" name="email" value="${sessionScope.user.email}" class="form-input" required>
            </div>
            
            <div class="form-group">
              <label for="profilePhone">Phone Number</label>
              <input type="tel" id="profilePhone" name="phoneNumber" value="${sessionScope.user.phoneNumber}" class="form-input">
            </div>
            
            <div class="form-group">
              <label for="address">Address</label>
              <textarea id="address" name="address" class="form-input" rows="3">${sessionScope.user.address}</textarea>
            </div>
            
            <button type="submit" class="btn-primary">Update Profile</button>
          </form>
        </div>
      </section>
      
      <!-- Orders Section -->
      <section id="orders-section" class="dashboard-section">
        <div class="section-header">
          <h2><i class="fas fa-shopping-bag"></i> My Orders</h2>
        </div>
        <div class="section-content">
          <div class="orders-container">
            <c:choose>
              <c:when test="${empty orders}">
                <div class="empty-state">
                  <i class="fas fa-shopping-bag"></i>
                  <h3>No orders yet</h3>
                  <p>You haven't placed any orders yet. Start shopping to place your first order!</p>
                  <a href="${pageContext.request.contextPath}/GetCategoriesServlet" class="btn-primary">Shop Now</a>
                </div>
              </c:when>
              <c:otherwise>
                <div class="table-container">
                  <table class="orders-table">
                    <thead>
                      <tr>
                        <th>Order ID</th>
                        <th>Date</th>
                        <th>Total</th>
                        <th>Action</th>
                      </tr>
                    </thead>
                    <tbody>
                      <c:forEach var="order" items="${orders}">
                        <tr>
                          <td>#${order.orderId}</td>
                          <td><fmt:formatDate value="${order.orderDate}" pattern="MMM d, yyyy" /></td>
                          <td>$<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00" /></td>
                          
                          <td>
                            <a href="${pageContext.request.contextPath}/OrderServlet?action=view&orderId=${order.orderId}" class="btn-link">View Details</a>
                          </td>
                        </tr>
                      </c:forEach>
                    </tbody>
                  </table>
                </div>
                
              </c:otherwise>
            </c:choose>
          </div>
        </div>
      </section>
      
      <!-- Change Password Section -->
      <section id="password-section" class="dashboard-section">
        <div class="section-header">
          <h2><i class="fas fa-lock"></i> Change Password</h2>
        </div>
        <div class="section-content">
          <form id="password-form" action="${pageContext.request.contextPath}/ChangePasswordServlet" method="post">
            <div class="form-group">
              <label for="currentPassword">Current Password</label>
              <input type="password" id="currentPassword" name="currentPassword" class="form-input" required>
            </div>
            
            <div class="form-group">
              <label for="newPassword">New Password</label>
              <input type="password" id="newPassword" name="newPassword" class="form-input" required>
            </div>
            
            <div class="form-group">
              <label for="confirmNewPassword">Confirm New Password</label>
              <input type="password" id="confirmNewPassword" name="confirmNewPassword" class="form-input" required>
            </div>
            
            <button type="submit" class="btn-primary">Change Password</button>
          </form>
        </div>
      </section>
    </div>
  </div>
</main>

<!-- Include Footer -->
<jsp:include page="/pages/includes/Footer.jsp">
    <jsp:param name="js" value="dashboard" />
</jsp:include>

<style>
/* Simple styling for messages */
.error-message {
  background-color: #ffebee;
  color: #c62828;
  padding: 10px 15px;
  margin: 10px 0;
  border-left: 4px solid #c62828;
  border-radius: 4px;
}

.success-message {
  background-color: #e8f5e9;
  color: #2e7d32;
  padding: 10px 15px;
  margin: 10px 0;
  border-left: 4px solid #2e7d32;
  border-radius: 4px;
}
</style>

<script>
document.addEventListener('DOMContentLoaded', function() {
    // Get all dashboard sections
    const sections = document.querySelectorAll('.dashboard-section');
    
    // Get all navigation items
    const navItems = document.querySelectorAll('.dashboard-nav li[data-section]');
    
    // Initially hide all sections except the active one
    hideAllSectionsExceptActive();
    
    // Setup the full name updater function
    window.updateFullName = function() {
        const fnameField = document.getElementById('profileFname');
        const surnameField = document.getElementById('profileSurname');
        const fullNameField = document.getElementById('fullName');
        
        if (fnameField && surnameField && fullNameField) {
            fullNameField.value = fnameField.value + ' ' + surnameField.value;
        }
    };
    
    // Add change event listeners to name fields to update the hidden field
    const fnameField = document.getElementById('profileFname');
    const surnameField = document.getElementById('profileSurname');
    
    if (fnameField) {
        fnameField.addEventListener('input', updateFullName);
    }
    
    if (surnameField) {
        surnameField.addEventListener('input', updateFullName);
    }
    
    // Add click event to navigation items
    navItems.forEach(item => {
        item.addEventListener('click', function(e) {
            // Prevent default action if clicking on the link
            if (e.target.tagName === 'A' || e.target.parentElement.tagName === 'A') {
                e.preventDefault();
            }
            
            const sectionId = this.getAttribute('data-section');
            
            // Remove active class from all nav items
            navItems.forEach(navItem => {
                navItem.classList.remove('active');
            });
            
            // Add active class to clicked nav item
            this.classList.add('active');
            
            // Show the selected section and hide others
            showSection(sectionId);
        });
    });
    
    // Function to hide all sections except the active one
    function hideAllSectionsExceptActive() {
        // Find active nav item
        const activeNavItem = document.querySelector('.dashboard-nav li.active');
        
        if (activeNavItem) {
            const activeSectionId = activeNavItem.getAttribute('data-section');
            
            // Hide all sections except the active one
            sections.forEach(section => {
                if (section.id !== activeSectionId) {
                    section.style.display = 'none';
                }
            });
        }
    }
    
    // Function to show a specific section and hide others
    function showSection(sectionId) {
        sections.forEach(section => {
            if (section.id === sectionId) {
                section.style.display = 'block';
            } else {
                section.style.display = 'none';
            }
        });
    }
});
</script>