<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- Base CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
<!-- Include Header -->
<jsp:include page="/pages/includes/Header.jsp">
    <jsp:param name="title" value="Login" />
    <jsp:param name="css" value="login" />
</jsp:include>

<!-- Display success message only when account is deleted -->
<c:if test="${sessionScope.accountDeleted eq 'true'}">
    <div id="successAlert" class="success-alert">
        Your account has been successfully deleted
        <% session.removeAttribute("accountDeleted"); %>
    </div>
</c:if>

<!-- Display other error/success messages -->
<c:if test="${not empty errorMessage}">
    <div id="errorAlert" class="error-alert">
        ${errorMessage}
    </div>
</c:if>
<c:if test="${not empty successMessage}">
    <div id="successAlert" class="success-alert">
        ${successMessage}
    </div>
</c:if>

<div class="container auth-container">
    <div class="auth-form-container">
        <div class="form-toggles">
            <button id="login-toggle" class="form-toggle active">Login</button>
            <button id="register-toggle" class="form-toggle">Register</button>
        </div>
        
        <!-- Login Form -->
        <form id="login-form" class="auth-form" action="${pageContext.request.contextPath}/LogInController" method="post">
            <h2 class="form-title">Login to Your Account</h2>
                 
            <div class="form-group">
                <label for="usernameOrEmail">Username or Email</label>
                <input type="text" id="usernameOrEmail" name="usernameOrEmail" class="form-input" required>
            </div>
            
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" class="form-input" required>
                <div class="forgot-password">
                    <a href="${pageContext.request.contextPath}/pages/user/ForgotPassword.jsp">Forgot password?</a>
                </div>
            </div>
            
            <div class="form-checkbox">
                <input type="checkbox" id="rememberMe" name="rememberMe" value="yes">
                <label for="rememberMe">Remember me</label>
            </div>
            
            <button type="submit" class="btn-primary btn-full">
                Login
            </button>
        </form>
        
        <!-- Register Form -->
        <form id="register-form" class="auth-form hidden" action="${pageContext.request.contextPath}/RegisterController" method="post">
            <h2 class="form-title">Create an Account</h2>
            
            <div class="form-group">
                <label for="firstName">First Name</label>
                <input type="text" id="firstName" name="fname" class="form-input" required>
            </div>

            
            <div class="form-group">
                <label for="surname">Surname</label>
                <input type="text" id="surname" name="surname" class="form-input" required>
            </div>
            
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" class="form-input" required>
            </div>
            
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" class="form-input" required>
            </div>
            
            <div class="form-group">
                <label for="address">Address</label>
                <textarea id="address" name="address" class="form-input"></textarea>
            </div>
            
            <div class="form-group">
                <label for="phone">Phone Number</label>
                <input type="tel" id="phone" name="phone_number" class="form-input" required>
            </div>
            
            <div class="form-group">
                <label for="registerPassword">Password</label>
                <input type="password" id="registerPassword" name="password" class="form-input" required>
            </div>
            
            <div class="form-group">
                <label for="confirmPassword">Confirm Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" class="form-input" required>
            </div>
            
            <div class="form-checkbox">
                <input type="checkbox" id="terms" name="terms" required>
                <label for="terms">
                    I agree to the <a href="${pageContext.request.contextPath}/pages/user/Terms.jsp">Terms of Service</a> and <a href="${pageContext.request.contextPath}/pages/user/Privacy.jsp">Privacy Policy</a>
                </label>
            </div>
            
            <button type="submit" class="btn-primary btn-full">
                Create Account
            </button>
        </form>
    </div>
</div>

<jsp:include page="/pages/includes/Footer.jsp">
    <jsp:param name="js" value="login" />
</jsp:include>
    
<script>
document.addEventListener('DOMContentLoaded', function() {
    // Get the toggle buttons
    const loginToggle = document.getElementById('login-toggle');
    const registerToggle = document.getElementById('register-toggle');
    
    // Get the forms
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');
    
    // Add click event listeners to the toggle buttons
    loginToggle.addEventListener('click', function() {
        loginForm.classList.remove('hidden');
        registerForm.classList.add('hidden');
        loginToggle.classList.add('active');
        registerToggle.classList.remove('active');
    });
    
    registerToggle.addEventListener('click', function() {
        loginForm.classList.add('hidden');
        registerForm.classList.remove('hidden');
        loginToggle.classList.remove('active');
        registerToggle.classList.add('active');
    });
    
    // Auto-hide alerts after 5 seconds (5000 milliseconds)
    const errorAlert = document.getElementById('errorAlert');
    const successAlert = document.getElementById('successAlert');
    
    function fadeOutElement(element) {
        if (element) {
            setTimeout(function() {
                element.style.opacity = '1';
                // Start fade out animation
                let opacity = 1;
                const fadeInterval = setInterval(function() {
                    if (opacity <= 0.1) {
                        clearInterval(fadeInterval);
                        element.style.display = 'none';
                    }
                    opacity -= 0.1;
                    element.style.opacity = opacity;
                }, 100);
            }, 5000);
        }
    }
    
    fadeOutElement(errorAlert);
    fadeOutElement(successAlert);
});
</script>

<style>
/* Styles for error and success messages */
.error-alert {
    background-color: #ffebee;
    color: #c62828;
    padding: 10px 15px;
    margin: 10px auto;
    max-width: 80%;
    border-left: 4px solid #c62828;
    border-radius: 4px;
    transition: opacity 1s ease;
}

.success-alert {
    background-color: #e8f5e9;
    color: #2e7d32;
    padding: 10px 15px;
    margin: 10px auto;
    max-width: 80%;
    border-left: 4px solid #2e7d32;
    border-radius: 4px;
    transition: opacity 1s ease;
}
</style>